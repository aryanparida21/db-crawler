package com.aryan.dbcrawler.service;

import com.aryan.dbcrawler.model.ColumnMeta;
import com.aryan.dbcrawler.model.TableMeta;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModelGeneratorService {
    
    public Map<String, String> generateModels(List<TableMeta> tables) {
        Map<String, String> models = new HashMap<>();
        
        for (TableMeta table : tables) {
            String className = tableNameToClassName(table.getTableName());
            String modelCode = generateModelCode(className, table);
            models.put(className, modelCode);
        }
        
        return models;
    }
    
    private String generateModelCode(String className, TableMeta table) {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.aryan.models;\n\n");
        sb.append("import lombok.AllArgsConstructor;\n");
        sb.append("import lombok.Data;\n");
        sb.append("import lombok.NoArgsConstructor;\n\n");
        sb.append("@Data\n");
        sb.append("@NoArgsConstructor\n");
        sb.append("@AllArgsConstructor\n");
        sb.append("public class ").append(className).append(" {\n\n");
        
        for (ColumnMeta column : table.getColumns()) {
            String javaType = sqlTypeToJavaType(column.getColumnType());
            String fieldName = columnNameToFieldName(column.getColumnName());
            sb.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n");
        }
        
        sb.append("\n}");
        return sb.toString();
    }
    
    private String tableNameToClassName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }
    
    private String columnNameToFieldName(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }
    
    private String sqlTypeToJavaType(String sqlType) {
        String type = sqlType.toUpperCase();
        
        if (type.contains("INT")) {
            return "Integer";
        } else if (type.contains("BIGINT")) {
            return "Long";
        } else if (type.contains("DECIMAL") || type.contains("FLOAT") || type.contains("DOUBLE")) {
            return "BigDecimal";
        } else if (type.contains("BOOLEAN")) {
            return "Boolean";
        } else if (type.contains("DATE")) {
            return "LocalDate";
        } else if (type.contains("TIME")) {
            return "LocalDateTime";
        } else if (type.contains("BLOB") || type.contains("BINARY")) {
            return "byte[]";
        } else {
            return "String";
        }
    }
}
