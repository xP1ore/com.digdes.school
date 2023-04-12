package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    final List<Token> tokens;
    private int index;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    private Token consume(TokenType type) {
        if (index < tokens.size() && tokens.get(index).type == type) {
            return tokens.get(index++);
        }
        throw new RuntimeException("Unexpected token: " + tokens.get(index));
    }

    public Map<String, Object> Parse() {
        if (tokens.size() == 0) {
            throw new RuntimeException("No tokens to parse");
        }
        Map<String, Object> parseData;
        Token token = consume(TokenType.IDENTIFIER);
        switch (token.value) {
            case "INSERT" -> parseData = parseInsert();
            case "UPDATE" -> parseData = parseUpdate();
            case "DELETE" -> parseData = parseDelete();
            case "SELECT" -> parseData = parseSelect();
            default -> throw new RuntimeException("Invalid query type");
        };
        return parseData;
    }

    public Map<String, Object> parseInsert() {
        Map<String, Object> parseData;
        try {
            Token token = consume(TokenType.IDENTIFIER);
            if (token.value.equals("VALUES")) {
                parseData = parseValues();
                if (parseData.containsKey("conditionMap")) {
                    throw new RuntimeException("Invalid query type");
                }
                parseData.put("INSERT", 0);
            } else {
                throw new RuntimeException("Invalid query type");
            }

        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid query type");
        }
        return parseData;
    }

    public Map<String, Object> parseUpdate() {
        Map<String, Object> parseData = new HashMap<>();
        try {
            Token token = consume(TokenType.IDENTIFIER);
            if (token.value.equals("VALUES")) {
                parseData = parseValues();
                parseData.put("UPDATE", 0);
            } else {
                throw new RuntimeException("Invalid query type");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return parseData;
    }

    public Map<String, Object> parseDelete() {
        Map<String, Object> parseData = new HashMap<>();
        Token token = consume(TokenType.IDENTIFIER);
        try {
            if (tokens.size() == 1 && token.value.equals("DELETE")) {
                // удалить всю таблицу
                parseData.put("DELETE", 1);
            } else {
                parseData = parseValues();
                parseData.put("DELETE", 0);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return parseData;
    }

    public Map<String, Object> parseSelect() {
        Map<String, Object> parseData = new HashMap<>();
        Token token = consume(TokenType.IDENTIFIER);
        try {
            if (tokens.size() == 1 && token.value.equals("SELECT")) {
                //вывести всю таблицу
                parseData.put("SELECT", 1);
            } else {
                parseData = parseValues();
                parseData.put("SELECT", 0);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return parseData;
    }

    public Map<String, Object> parseValues() {

        Long id = null;
        String lastName = null;
        Long age = null;
        Double cost = null;
        Boolean active = null;

        List<Object> conditions = new ArrayList<>();
        Map<String, Object> parseData = new HashMap<>();

        try {
            this.index++;

            Token token = consume(TokenType.STRING_VALUE);
            if (token.value.equals("lastName")) {
                try {
                    if (!(token.value.matches("\\s+"))) {
                        lastName = this.tokens.get(index + 2).toString();
                        index = index + 2;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }

            token = consume(TokenType.NUMBER);
            if (token.value.equals("id")) {
                try {
                    if (token.value.matches("\\d*")) {
                        id = Long.parseLong(this.tokens.get(index + 2).toString());
                        index = index + 2;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (token.value.equals("age")) {
                try {
                    if (!(token.value.matches("\\d*"))) {
                        age = Long.parseLong(this.tokens.get(index + 2).toString());
                        index = index + 2;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (token.value.equals("cost")) {
                try {
                    if (token.value.matches("[0-9]+(\\.[0-9]+)?")) {
                        cost = Double.parseDouble(this.tokens.get(index + 2).toString());
                        index = index + 2;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }

            token = consume(TokenType.BOOLEAN);
            if (token.value.equals("active")) {
                try {
                    if (token.value.matches("TRUE|FALSE")) {
                        active = Boolean.parseBoolean(this.tokens.get(index + 2).toString());
                        index = index + 2;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }

            token = consume(TokenType.IDENTIFIER);
            if (token.value.equals("WHERE")) {
                conditions = parseWhere();
            }


        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        parseData.put("id", id);
        parseData.put("lastName", lastName);
        parseData.put("age", age);
        parseData.put("cost", cost);
        parseData.put("active", active);
        if (!conditions.isEmpty()) {
            parseData.put("conditions", conditions);
        }
        return parseData;
    }

    public List<Object> parseWhere() {
        // map столбец->условный оператор
        Map<String, String> conditionMap = new HashMap<>();
        List<Object> conditions = new ArrayList<>();
        try {
            Token token = consume(TokenType.OPERATOR);
            for (int i = this.index; i < tokens.size(); i++) {
                if (token.type == TokenType.OPERATOR) {
                    switch (token.value) {
                        case "=" -> conditions.add(new CondPack("=", tokens.get(i-1).toString(), tokens.get(i+1)));
                        case "!=" -> conditions.add(new CondPack("!=", tokens.get(i-1).toString(), tokens.get(i+1)));
                        //case "like" -> conditions.add(new CondPack("like", tokens.get(i-1).toString(), tokens.get(i+1)));
                        //case "ilike" -> conditions.add(new CondPack("ilike", tokens.get(i-1).toString(), tokens.get(i+1)));
                        case ">=" -> conditions.add(new CondPack(">=", tokens.get(i-1).toString(), tokens.get(i+1)));
                        case "<=" -> conditions.add(new CondPack("<=", tokens.get(i-1).toString(), tokens.get(i+1)));
                        case "<" -> conditions.add(new CondPack("<", tokens.get(i-1).toString(), tokens.get(i+1)));
                        case ">" -> conditions.add(new CondPack(">", tokens.get(i-1).toString(), tokens.get(i+1)));
                        //case "and" -> conditions.add(new CondPack("and", tokens.get(i-1).toString(), tokens.get(i+1)));
                        //case "or" -> conditions.add(new CondPack("or", tokens.get(i-1).toString(), tokens.get(i+1)));
                        default -> conditions.add(new CondPack("Invalid operator", tokens.get(i-1).toString(), tokens.get(i+1)));
                    };
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return conditions;
    }
}
