package com.aryan.dbcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbConfig {
    private String url;
    private String username;
    private String password;
    private String database;
}
