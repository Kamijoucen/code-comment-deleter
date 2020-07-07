package com.kamijoucen.code_deleter.parse;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

public class Lexical {

    public enum State {

        NORMAL,
        STRING,
        IN_COMMENT,
        COMMENT
    }

    private String content;

    private State state;

    private int offset;

    private List<TextRange> commentRanges;


    public Lexical(String content) {
        this.content = content;
        this.offset = 0;
        this.state = State.NORMAL;
        this.commentRanges = new ArrayList<>();
    }

    //

    public String parse() {

        while (offset < content.length()) {
            switch (content.charAt(offset)) {
                case '\"':
                    this.state = State.STRING;
                    break;
                case '/':
                    this.state = State.IN_COMMENT;
                    break;
                default:
                    this.state = State.NORMAL;
                    break;
            }

            boolean match = false;

            do {

                switch (state) {
                    case NORMAL:
                        skipNormal();
                        match = false;
                        break;
                    case STRING:
                        skipString();
                        match = false;
                        break;
                    case COMMENT:
                        skipComment();
                        match = false;
                        break;
                    case IN_COMMENT:
                        if (content.startsWith("//", offset)
                                || content.startsWith("/*", offset)) {
                            this.state = State.COMMENT;
                        }
                        match = true;
                        break;
                }

            } while (match);
        }
        return null;
    }

    private int forward() {
        return ++offset;
    }


    private void skipNormal() {

    }

    private void skipString() {
        forward(); // eat first "
        while (offset < content.length()
                && content.charAt(offset) != '\"') {
            forward();
            if (content.charAt(offset) == '\\') {
                forward();
            }
        }
        forward(); // eat last "
    }

    private void skipComment() {
    }

}
