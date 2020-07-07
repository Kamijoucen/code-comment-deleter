package com.kamijoucen.code_deleter.parse;

import java.io.CharArrayReader;

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


    public Lexical(String content) {
        this.content = content;
        this.offset = 0;
        this.state = State.NORMAL;
    }

    //

    public String parse() {
        StringBuilder newContent = new StringBuilder();

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
                        skipNormal(newContent);
                        match = false;
                        break;
                    case STRING:
                        skipString(newContent);
                        match = false;
                        break;
                    case COMMENT:
                        skipComment(newContent);
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
        return newContent.toString();
    }

    private int forward() {
        return ++offset;
    }


    private void skipNormal(StringBuilder builder) {
    }

    private void skipString(StringBuilder builder) {

    }

    private void skipComment(StringBuilder builder) {
    }

}
