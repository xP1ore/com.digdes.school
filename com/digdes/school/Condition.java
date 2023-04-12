package com.digdes.school;

public interface Condition {
    boolean check(Object other);
}

class EqualCondition implements Condition {

    private final Object value;
    public EqualCondition(Object value) {
        this.value = value;
    }

    @Override
    public boolean check(Object other) {
        return this.value.equals(other);
    }
}

class NotEqualCondition implements Condition {

    private final Object value;
    public NotEqualCondition(Object value) {
        this.value = value;
    }

    @Override
    public boolean check(Object other) {
        return !this.value.equals(other);
    }
}

class NumericCondition implements Condition {

    private final Object value;
    private final String operator;

    public NumericCondition(Object value, String operator) {
        this.value = value;
        this.operator = operator;
    }

    @Override
    public boolean check(Object other) {
        if (other instanceof Number) {
            Number otherNumber = (Number) other;
            Number thisNumber = (Number) this.value;
            switch (this.operator) {
                case "<":
                    return otherNumber.doubleValue() < thisNumber.doubleValue();
                case "<=":
                    return otherNumber.doubleValue() <= thisNumber.doubleValue();
                case ">":
                    return otherNumber.doubleValue() > thisNumber.doubleValue();
                case ">=":
                    return otherNumber.doubleValue() >= thisNumber.doubleValue();
                default:
                    throw new RuntimeException("Invalid operator.");
            }
        } else {
            throw new RuntimeException("Invalid type.");
        }
    }
}
