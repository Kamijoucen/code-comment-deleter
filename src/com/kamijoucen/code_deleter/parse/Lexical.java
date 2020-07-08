package com.kamijoucen.code_deleter.parse;

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

    private StringBuilder newContext = new StringBuilder();

    private List<TextRange> commentRanges;


    public Lexical(String content) {
        this.content = content;
        this.offset = 0;
        this.state = State.NORMAL;
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


    private void skipNormal() {

    }

    private void skipString() {
        appendAndForward();
        while (offset < content.length()
                && content.charAt(offset) != '\"') {
            if (content.charAt(offset) == '\\') {
                appendAndForward();
                appendAndForward();
            } else {
                appendAndForward();
            }
        }
        appendAndForward(); // eat last "
    }

    private void skipComment() {
        forward();
        if (content.charAt(offset) == '\\') {
            while (offset < content.length() && content.charAt(offset) != '\n') {
                forward();
            }
            append('\n');
        } else {

        }
    }

    private void forward() {
        ++offset;
    }

    private void append(char ch) {
        newContext.append(ch);
    }

    private void appendContent() {
        append(content.charAt(offset));
    }

    private void appendAndForward() {
        appendContent();
        forward();
    }

}
