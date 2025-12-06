package com.aryan.dbcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMeta {
    private String columnName;
    private String columnType;
    private boolean nullable;
    private boolean primaryKey;
}
