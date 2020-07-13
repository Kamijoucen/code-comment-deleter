package com.kamijoucen.code_deleter.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinLoopFile {

    private String basePath;

    private List<String> paths;

    public ForkJoinLoopFile(String basePath) {
        this.basePath = basePath;
        this.paths = new ArrayList<String>();
    }

    public List<String> getAbsolutePaths() {
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return new ArrayList<String>();
        }
        ForkJoinPool pool = new ForkJoinPool(Constants.TASK_SIZE + 1);
        this.paths = pool.invoke(new ITtak(basePath));
        return this.paths;
    }

    private class ITtak extends RecursiveTask<List<String>> {

        public final String path;

        public ITtak(String path) {
            this.path = path;
        }

        @Override
        protected List<String> compute() {
            File file = new File(path);

            File[] subFiles = file.listFiles();
            if (subFiles == null || subFiles.length == 0) {
                return new ArrayList<String>();
            }

            List<ITtak> subTask = new ArrayList<ITtak>();

            List<String> result = new ArrayList<String>();

            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    subTask.add(new ITtak(subFile.getAbsolutePath()));
                } else {
                    if (subFile.getName().toLowerCase().endsWith(".java")) {
                        result.add(subFile.getAbsolutePath());
                    }
                }
            }

            for (ITtak iTtak : invokeAll(subTask)) {
                result.addAll(iTtak.join());
            }
            return result;
        }
    }

}
