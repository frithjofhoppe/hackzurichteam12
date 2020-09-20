package com.hackzurich.hackzurichteam12.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@CsvDataType
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RawNewsArticle {
    @CsvField(pos = 0)
    private String sourceAbbrivation;
    @CsvField(pos = 1)
    private String sourceText;
    @CsvField(pos = 2, format = "yyyy-MM-dd hh:mm")
    private Date publicationDate;
    @CsvField(pos = 3, noValue = "de", required = true)
    private String language;
    @CsvField(pos = 4)
    private long characterLenght;
    @CsvField(pos = 5)
    private String mainTitle;
    @CsvField(pos = 6)
    private String angleBracket;
    @CsvField(pos = 7)
    private String subtitle;
    @CsvField(pos = 8)
    private String category;
    @CsvField(pos = 9)
    private String annotatedPerson;
    @CsvField(pos = 10)
    private String textStructureJson;

    public boolean containsCorona() {
        if(language.equalsIgnoreCase("de") || language.equalsIgnoreCase("en")){
            String filename = "viruswordlist."+language+".txt";
            try {
                List<String> result = Files.readAllLines(Paths.get(filename));
                return result.stream().anyMatch(keyword -> textStructureJson.toLowerCase().contains(keyword));
            } catch (IOException e) {
                log.error("Unable to read virus wordlist", e);
            }
        }
        return false;
    }
}
