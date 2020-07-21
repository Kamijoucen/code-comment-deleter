package com.kamijoucen.code_deleter;

import com.kamijoucen.code_deleter.parse.Lexical;
import com.kamijoucen.code_deleter.parse.MachineLexical;
import com.kamijoucen.code_deleter.util.FileUtil;

import java.util.concurrent.CountDownLatch;

public class DeleteTask implements Runnable {

    private String sourcePath;
    private CountDownLatch latch;

    public DeleteTask(String sourcePath, CountDownLatch latch) {
        this.sourcePath = sourcePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            String contnet = FileUtil.read(sourcePath);
            if (contnet != null && contnet.length() != 0) {
                String content = new MachineLexical(contnet).parse();
                FileUtil.cover(sourcePath, content);
            }
        } finally {
            latch.countDown();
        }
    }
}
