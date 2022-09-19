package com.example.tuanhaowu.util;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

    //上传文件的工具类
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        System.out.println(targetFile);
        if(!targetFile.exists()){
            targetFile.mkdirs();
            System.out.println("创建");
        }
        FileOutputStream out = new FileOutputStream(filePath+"\\"+fileName);
        System.out.println(out.getFD());
        out.write(file);
        out.flush();
        out.close();
    }
}
