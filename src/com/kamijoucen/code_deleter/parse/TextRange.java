package com.kamijoucen.code_deleter.parse;

public class TextRange {
    public final int offset;
    public final int length;

    public TextRange(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }
}
