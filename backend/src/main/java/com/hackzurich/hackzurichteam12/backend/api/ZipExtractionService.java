package com.hackzurich.hackzurichteam12.backend.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;

public interface ZipExtractionService {
    List<File> readZip(String zippath) throws IOException;
}
