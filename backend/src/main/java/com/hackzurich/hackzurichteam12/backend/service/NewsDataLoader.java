package com.hackzurich.hackzurichteam12.backend.service;


import com.hackzurich.hackzurichteam12.backend.api.ZipExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NewsDataLoader {
    @Value("${coronascare.importtype}")
    private String importType;

    @Value("${coronascare.datasource}")
    private String datasource;

    private final ZipExtractionService zipExtractionService;
    private final NewsArticleService newsArticleService;
    private final NewsArticleSerializer newsArticleSerializer;

    public static final String IMPORT_TYPE_ARCHIVE = "archive";
    public static final String IMPORT_TYPE_FILE = "file";

    public NewsDataLoader(ZipExtractionService zipExtractionService, NewsArticleService newsArticleService, NewsArticleSerializer newsArticleSerializer) {
        this.zipExtractionService = zipExtractionService;
        this.newsArticleService = newsArticleService;
        this.newsArticleSerializer = newsArticleSerializer;
    }

    @PostConstruct()
    public void loadDataFromSource() throws IOException {
        log.warn("Loading Testdata");
        List<File> files = new ArrayList<>();
        if(importType.equalsIgnoreCase(IMPORT_TYPE_ARCHIVE)) {
            files.addAll(zipExtractionService.readZip(datasource));
        } else if(importType.equalsIgnoreCase(IMPORT_TYPE_FILE)) {
            files.add(new ClassPathResource(datasource).getFile());
        } else {
            log.error("unknown import type "+ importType);
        }
        newsArticleService.filterEnrichAndPerist(newsArticleSerializer.readAllFiles(files).stream());
    }
}
