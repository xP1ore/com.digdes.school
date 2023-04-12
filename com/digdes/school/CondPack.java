package com.digdes.school;

public class CondPack {
    public String operator;
    public String column;
    public Object value;

    public CondPack(String operator, String column, Object value) {
        this.operator = operator;
        this.column = column;
        this.value = value;
    }
}
