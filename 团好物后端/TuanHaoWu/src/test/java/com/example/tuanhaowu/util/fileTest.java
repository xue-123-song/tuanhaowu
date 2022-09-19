package com.example.tuanhaowu.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class fileTest {

    @Test
    public void testUploadFile() throws Exception {
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
        String fileName = "test.txt";
        String path = filePath +  "\\" + fileName;
        byte[] bytes = {'a'};
        FileUtil.uploadFile(bytes,filePath,fileName);
        byte[] readAllBytes = Files.readAllBytes(Paths.get(path));
        String content = new String(readAllBytes, StandardCharsets.UTF_8);
        assertEquals("a",content);
    }
}
