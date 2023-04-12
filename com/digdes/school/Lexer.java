package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final Pattern pattern = Pattern.compile("(?i)([a-zA-Z][a-zA-Z0-9]*)|('[^']*')|(\\\\s+)|([.,])|((<|>|" +
            "<=|>=|=|!=|like|ilike|and|or))|([0-9]+(\\.[0-9]+)?)|(true|false)");

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(1)));
            } else if (matcher.group(2) != null) {
                String res = matcher.group(2);
                tokens.add(new Token(TokenType.STRING_VALUE, res.substring(1, res.length() - 1)));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(TokenType.WHITESPACE, matcher.group(3)));
            } else if (matcher.group(4) != null) {
                if (matcher.group(4).equals(".")) {
                    tokens.add(new Token(TokenType.DOT, matcher.group(4)));
                }
            } else if (matcher.group(5) != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group(5)));
            } else if (matcher.group(6) != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group(6)));
            } else if (matcher.group(7) != null) {
                tokens.add(new Token(TokenType.BOOLEAN, matcher.group(7)));
            }
        }
        return tokens;
    }
}
