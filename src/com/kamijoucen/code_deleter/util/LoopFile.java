package com.kamijoucen.code_deleter.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoopFile {

    private String basePath;

    private List<String> paths;

    public LoopFile(String basePath) {
        this.basePath = basePath;
        this.paths = new ArrayList<String>();
    }

    public List<String> getAbsolutePaths() {
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return new ArrayList<String>();
        }
        scanSubDirFilePaths(baseDir);
        return paths;
    }

    private void scanSubDirFilePaths(File file) {
        File[] subFiles = file.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile : subFiles) {
            if (subFile.isDirectory()) {
                scanSubDirFilePaths(subFile);
            } else {
                if (subFile.getName().toLowerCase()
                        .endsWith(".java")) {
                    paths.add(subFile.getAbsolutePath());
                }
            }
        }
    }
}
