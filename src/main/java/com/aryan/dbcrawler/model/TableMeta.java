package com.aryan.dbcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableMeta {
    private String tableName;
    private List<ColumnMeta> columns;
    private List<ForeignKeyMeta> foreignKeys;
    private List<IndexMeta> indexes;
}
