package com.agileboot.common.annotation;

import cn.hutool.poi.excel.cell.CellHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 *
 * @author valarchie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    String name() default "";

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 数据映射，格式如下
     * 原始值:映射值;
     * 1:正常;2:禁用;3:删除;4:锁定
     * @return
     */
    String mapping() default "";
}
