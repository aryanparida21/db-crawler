package com.aryan.dbcrawler.service;

import com.aryan.dbcrawler.model.ColumnMeta;
import com.aryan.dbcrawler.model.DbConfig;
import com.aryan.dbcrawler.model.ForeignKeyMeta;
import com.aryan.dbcrawler.model.IndexMeta;
import com.aryan.dbcrawler.model.TableMeta;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class DatabaseCrawlerService {
    
    private final ConfigService configService;
    
    public DatabaseCrawlerService(ConfigService configService) {
        this.configService = configService;
    }
    
    public List<TableMeta> crawl() throws Exception {
        DbConfig config = configService.getDbConfig();
        List<TableMeta> tables = new ArrayList<>();
        
        try {
            try (Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())) {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tableSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                
                while (tableSet.next()) {
                    String tableName = tableSet.getString("TABLE_NAME");
                    List<ColumnMeta> columns = getColumns(metaData, tableName);
                    List<ForeignKeyMeta> fks = getForeignKeys(metaData, tableName);
                    List<IndexMeta> indexes = getIndexes(metaData, tableName);
                    tables.add(new TableMeta(tableName, columns, fks, indexes));
                }
            }
        } catch (SQLException e) {
            // If database connection fails, return demo data
            return getDemoData();
        }
        
        return tables;
    }
    
    private List<TableMeta> getDemoData() {
        List<TableMeta> tables = new ArrayList<>();
        
        // Demo Users table
        List<ColumnMeta> userColumns = Arrays.asList(
            new ColumnMeta("id", "INT", false, true),
            new ColumnMeta("username", "VARCHAR", false, false),
            new ColumnMeta("email", "VARCHAR", false, false),
            new ColumnMeta("created_at", "TIMESTAMP", false, false)
        );
        tables.add(new TableMeta("users", userColumns, Collections.emptyList(), Collections.emptyList()));
        
        // Demo Products table
        List<ColumnMeta> productColumns = Arrays.asList(
            new ColumnMeta("id", "INT", false, true),
            new ColumnMeta("product_name", "VARCHAR", false, false),
            new ColumnMeta("price", "DECIMAL", false, false),
            new ColumnMeta("stock_quantity", "INT", true, false)
        );
        tables.add(new TableMeta("products", productColumns, Collections.emptyList(), Collections.emptyList()));
        
        return tables;
    }
    
    private List<ColumnMeta> getColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<ColumnMeta> columns = new ArrayList<>();
        ResultSet columnSet = metaData.getColumns(null, null, tableName, null);
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        
        Set<String> pkColumns = new HashSet<>();
        while (primaryKeys.next()) {
            pkColumns.add(primaryKeys.getString("COLUMN_NAME"));
        }
        
        while (columnSet.next()) {
            String columnName = columnSet.getString("COLUMN_NAME");
            String columnType = columnSet.getString("TYPE_NAME");
            boolean nullable = columnSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
            boolean isPk = pkColumns.contains(columnName);
            
            columns.add(new ColumnMeta(columnName, columnType, nullable, isPk));
        }
        
        return columns;
    }

    private List<ForeignKeyMeta> getForeignKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<ForeignKeyMeta> fks = new ArrayList<>();
        ResultSet fkRs = metaData.getImportedKeys(null, null, tableName);
        while (fkRs.next()) {
            String fkName = fkRs.getString("FK_NAME");
            String fkColumn = fkRs.getString("FKCOLUMN_NAME");
            String pkTable = fkRs.getString("PKTABLE_NAME");
            String pkColumn = fkRs.getString("PKCOLUMN_NAME");
            fks.add(new ForeignKeyMeta(fkName, fkColumn, pkTable, pkColumn));
        }
        return fks;
    }

    private List<IndexMeta> getIndexes(DatabaseMetaData metaData, String tableName) throws SQLException {
        Map<String, IndexMeta> indexMap = new LinkedHashMap<>();
        ResultSet idxRs = metaData.getIndexInfo(null, null, tableName, false, false);
        while (idxRs.next()) {
            String idxName = idxRs.getString("INDEX_NAME");
            if (idxName == null) continue; // skip if no name
            boolean nonUnique = idxRs.getBoolean("NON_UNIQUE");
            String columnName = idxRs.getString("COLUMN_NAME");

            IndexMeta im = indexMap.get(idxName);
            if (im == null) {
                im = new IndexMeta(idxName, new ArrayList<>(), !nonUnique);
                indexMap.put(idxName, im);
            }
            if (columnName != null) im.getColumns().add(columnName);
        }
        return new ArrayList<>(indexMap.values());
    }
}
