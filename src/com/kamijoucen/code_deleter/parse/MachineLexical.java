package com.kamijoucen.code_deleter.parse;

public class MachineLexical implements Lexical {

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

    public MachineLexical(String content) {
        this.content = content;
        this.offset = 0;
        this.state = State.NORMAL;
    }

    @Override
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
                        scanNormal();
                        match = false;
                        break;
                    case STRING:
                        scanString();
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
        return newContext.toString();
    }

    private void scanNormal() {
        while (offset < content.length()
                && content.charAt(offset) != '/'
                && content.charAt(offset) != '"') {
            appendAndForward();
        }
    }

    private void scanString() {
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
        if (content.charAt(offset) == '/') {
            while (offset < content.length()
                    && content.charAt(offset) != '\n') {
                forward();
            }
        } else if (content.charAt(offset) == '*') {
            forward();
            while (content.charAt(offset) != '*'
                    || content.charAt(offset + 1) != '/') {
                forward();
            }
            forward(); // eat "
            forward(); // eat /
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
