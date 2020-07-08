package com.kamijoucen.code_deleter.util;

import java.io.*;

public class FileUtil {

    public static String read(String fileName) {
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.isFile() || !file.exists()) {
            return null;
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            char[] tempBuf = new char[1024];

            StringBuilder buffer = new StringBuilder();
            int len = -1;
            while ((len = bufferedReader.read(tempBuf)) != -1) {
                buffer.append(tempBuf, 0, len);
            }
            return buffer.toString();
        } catch (IOException e) {
            // todo
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public static void cover(String filePath, String newContent) {
//        System.out.println("\n--------------------------------------------------------------\n");
        System.out.println(filePath + " DONE!");
//        System.out.println(newContent);
    }

}
