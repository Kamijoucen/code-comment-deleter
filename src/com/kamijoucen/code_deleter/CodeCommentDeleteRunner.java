package com.kamijoucen.code_deleter;

import com.kamijoucen.code_deleter.util.LoopFile;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CodeCommentDeleteRunner {

    private CodeCommentDeleteRunner() {
    }

    private static final ThreadPoolExecutor executor;

    static {
        int coreNum = Runtime.getRuntime().availableProcessors() * 2;
        executor = new ThreadPoolExecutor(
                coreNum,
                coreNum,
                0,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(128)
        );
    }

    public static void run(String path) {
        List<String> files = new LoopFile(path).getAbsolutePaths();

        CountDownLatch latch = new CountDownLatch(files.size());
        for (String file : files) {
            DeleteTask task = new DeleteTask(file, latch);
            executor.submit(task);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
