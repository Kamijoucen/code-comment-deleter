package com.kamijoucen.code_deleter;

import com.kamijoucen.code_deleter.util.Constants;
import com.kamijoucen.code_deleter.util.ForkJoinLoopFile;

import java.util.List;
import java.util.concurrent.*;

public class CodeCommentDeleteRunner {

    private CodeCommentDeleteRunner() {
    }

    private static final ThreadPoolExecutor executor;

    static {
        executor = new ThreadPoolExecutor(
                Constants.TASK_SIZE,
                Constants.TASK_SIZE,
                0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(128) {
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
        List<String> files = new ForkJoinLoopFile(path).getAbsolutePaths();

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
