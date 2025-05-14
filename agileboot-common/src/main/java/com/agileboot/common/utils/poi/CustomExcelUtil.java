package com.agileboot.common.utils.poi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.agileboot.common.annotation.ExcelColumn;
import com.agileboot.common.annotation.ExcelSheet;
import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode.Internal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.ReflectUtil.getFieldValue;

/**
 * 自定义Excel 导入导出工具
 *
 * @author valarchie
 */
@Slf4j
public class CustomExcelUtil {

    private CustomExcelUtil() {
    }

    public static <T> void writeToResponse(List<T> list, Class<T> clazz, HttpServletResponse response) {
        try {
            writeToOutputStream(list, clazz, response.getOutputStream());
        } catch (IOException e) {
            throw new ApiException(e, Internal.EXCEL_PROCESS_ERROR, e.getMessage());
        }
    }

    public static <T> List<T> readFromRequest(Class<T> clazz, MultipartFile file) {
        try {
            return readFromInputStream(clazz, file.getInputStream());
        } catch (IOException e) {
            // 注意如果是捕获到的错误 一定要放进ApiException当中
            throw new ApiException(e, Internal.EXCEL_PROCESS_ERROR, e.getMessage());
        }
    }

    public static <T> void writeToOutputStream(List<T> list, Class<T> clazz, OutputStream outputStream) {

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        ExcelSheet sheetAnno = clazz.getAnnotation(ExcelSheet.class);

        if (sheetAnno != null) {
            // 默认的sheetName是 sheet1
            writer.renameSheet(sheetAnno.name());
        }

        Field[] fields = clazz.getDeclaredFields();

        //自定义标题别名
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                writer.addHeaderAlias(field.getName(), annotation.name());
            }
        }

        List<Map<String, Object>> mapList = handleValue(list, fields);

        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        // 合并单元格后的标题行，使用默认标题样式
        // writer.merge(4, "一班成绩单"); 一次性写出内容，使用默认样式，强制输出标题
        writer.write(mapList, true);
        writer.flush(outputStream, true);
    }

    private static <T> List<Map<String, Object>> handleValue(List<T> list, Field[] fields) {

        List<Map<String, Object>> mapList = new ArrayList<>(list.size());
        for (T row : list) {
            Map<String, Object> map = BeanUtil.beanToMap(row);
            for (Field field : fields) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                if (annotation != null) {

                    // 处理每个字段的值
                    Object value = getFieldValue(row, field);
                    if (value != null) {
                        // 处理日期格式
                        if (value instanceof java.util.Date) {
                            String format = StringUtils.isBlank(annotation.dateFormat()) ? DatePattern.NORM_DATETIME_PATTERN : annotation.dateFormat();
                            value = DateUtil.format((java.util.Date) value, format);
                            map.put(field.getName(), value);
                        }

                        // 处理映射值
                        String mapping = annotation.mapping();
                        if (!mapping.isEmpty()) {
                            String[] mappings = mapping.split(";");
                            for (String mapItem : mappings) {
                                String[] keyValue = mapItem.split(":");
                                if (keyValue.length == 2 && keyValue[0].equals(value.toString())) {
                                    value = keyValue[1];
                                    break;
                                }
                            }
                            map.put(field.getName(), value);
                        }
                    }

                }

            }
            mapList.add(map);
        }

        return mapList;
    }



    public static <T> List<T> readFromInputStream(Class<T> clazz, InputStream inputStream) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 去除掉excel中的html标签语言  避免xss攻击
        reader.setCellEditor(new TrimXssEditor());

        Field[] fields = clazz.getDeclaredFields();

        //自定义标题别名
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                reader.addHeaderAlias(annotation.name(), field.getName());
            }
        }

        return reader.read(0, 1, clazz);
    }



}
