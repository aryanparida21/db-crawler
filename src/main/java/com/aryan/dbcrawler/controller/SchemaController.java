package com.aryan.dbcrawler.controller;

import com.aryan.dbcrawler.model.TableMeta;
import com.aryan.dbcrawler.service.DatabaseCrawlerService;
import com.aryan.dbcrawler.service.ModelGeneratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SchemaController {

    private final DatabaseCrawlerService crawler;
    private final ModelGeneratorService generator;

    public SchemaController(DatabaseCrawlerService crawler, ModelGeneratorService generator) {
        this.crawler = crawler;
        this.generator = generator;
    }

    @GetMapping("/schema")
    public List<TableMeta> getSchema() throws Exception {
        return crawler.crawl();
    }

    @GetMapping("/models")
    public Map<String, String> getModels() throws Exception {
        List<TableMeta> tables = crawler.crawl();
        return generator.generateModels(tables);
    }
}
