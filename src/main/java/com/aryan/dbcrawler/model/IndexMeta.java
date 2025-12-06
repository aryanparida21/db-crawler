package com.aryan.dbcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexMeta {
    private String indexName;
    private List<String> columns;
    private boolean unique;
}
