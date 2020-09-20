package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.model.RawNewsArticle;
import net.sf.jsefa.Deserializer;
import net.sf.jsefa.csv.CsvIOFactory;
import net.sf.jsefa.csv.config.CsvConfiguration;
import net.sf.jsefa.csv.lowlevel.config.QuoteMode;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NewsArticleSerializer {

    Deserializer deserializer;

    {
        final CsvConfiguration csvConfiguration = new CsvConfiguration();
        System.out.println(csvConfiguration.getLineBreak());
        deserializer = CsvIOFactory.createFactory(csvConfiguration, RawNewsArticle.class).createDeserializer();
    }

    public List<RawNewsArticle> readNewsArticlesFromFile(File file) {
        List<RawNewsArticle> result = new ArrayList<>();
        try {
                deserializer.open(new InputStreamReader(new FileInputStream(file)));
                while (deserializer.hasNext()){
                    try {
                        result.add(deserializer.next());
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return result;
    }

    public List<RawNewsArticle> readAllFiles(List<File> files) {
        return files.stream().map(this::readNewsArticlesFromFile).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
