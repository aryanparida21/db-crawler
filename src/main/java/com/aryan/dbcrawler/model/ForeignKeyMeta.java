package com.aryan.dbcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyMeta {
    private String fkName;
    private String columnName;
    private String referencedTable;
    private String referencedColumn;
}
