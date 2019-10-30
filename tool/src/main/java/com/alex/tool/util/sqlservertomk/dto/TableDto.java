package com.alex.tool.util.sqlservertomk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDto {

    private String IsNullable;
    private String DataType;
    private String Length;
    private String KeyColumn;
    private String AutoIncrement;
    private String ColumnName;
    private String Description;
}