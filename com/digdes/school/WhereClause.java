package com.digdes.school;

import java.util.HashMap;
import java.util.Map;

public class WhereClause {
    Map<String, Condition> columnConditions;

    public WhereClause() {
        this.columnConditions = new HashMap<>();
    }

    public void addCondition(String columnName, Condition condition) {
        this.columnConditions.put(columnName, condition);
    }

    public boolean filterRow(Map<String, Object> row) {
        for (Map.Entry<String, Condition> entry : this.columnConditions.entrySet()) {
            String columnName = entry.getKey();
            Condition condition = entry.getValue();
            if (row.get(columnName) == null)
                return false;
            if (!condition.check(row.get(columnName))) {
                return false;
            }
        }
        return true;
    }
}

