package com.lwkl.valuate.stream;

import lombok.Data;

@Data
public class LexerStream {
    private char[] source;
    private int position;
    private int length;

    public LexerStream(String source) {
        this.length = source.length();
        this.source = source.toCharArray();
    }

    public char readCharacter() {
        char character = this.source[this.position];
        this.position += 1;
        return character;
    }

    public void rewind(int amount) {
        this.position -= amount;
    }

    public boolean canRead() {
        return this.position < this.length;
    }
}
