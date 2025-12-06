package com.aryan.dbcrawler.service;

import com.aryan.dbcrawler.model.DbConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    
    @Value("${db.url:jdbc:mysql://localhost:3306/test}")
    private String dbUrl;
    
    @Value("${db.username:root}")
    private String dbUsername;
    
    @Value("${db.password:}")
    private String dbPassword;
    
    @Value("${db.database:test}")
    private String database;
    
    public DbConfig getDbConfig() {
        return new DbConfig(dbUrl, dbUsername, dbPassword, database);
    }
}
