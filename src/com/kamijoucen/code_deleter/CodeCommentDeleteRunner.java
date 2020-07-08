package com.kamijoucen.code_deleter;

import com.kamijoucen.code_deleter.util.LoopFile;

import java.util.List;
import java.util.concurrent.*;

public class CodeCommentDeleteRunner {

    public static final int TASK_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    private CodeCommentDeleteRunner() {
    }

    private static final ThreadPoolExecutor executor;

    static {
        executor = new ThreadPoolExecutor(
                TASK_SIZE,
                TASK_SIZE,
                0,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(128) {
                    @Override
                    public boolean offer(Runnable runnable) {
                        try {
                            put(runnable);
                            return true;
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            return false;
                        }
                    }
                }
        );
    }

    public static void run(String path) {
        List<String> files = new LoopFile(path).getAbsolutePaths();

        CountDownLatch latch = new CountDownLatch(files.size());
        for (String file : files) {
            DeleteTask task = new DeleteTask(file, latch);
            executor.submit(task);
//            task.run();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
