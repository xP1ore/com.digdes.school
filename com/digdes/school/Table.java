package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

    final List<Map<String, Object>> rows;

    public Table() {
        this.rows = new ArrayList<>();
        Map<String, Object> defaultRow = new HashMap<>();
        defaultRow.put("id", "id");
        defaultRow.put("lastName", "lastName");
        defaultRow.put("age", "age");
        defaultRow.put("cost", "cost");
        defaultRow.put("active", "active");

        this.rows.add(defaultRow);
    }

    public void setRow(Map<String, Object> data) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", data.get("id"));
        row.put("lastName", data.get("lastName"));
        row.put("age", data.get("age"));
        row.put("cost", data.get("cost"));
        row.put("active", data.get("active"));

        addRow(row);
    }

    public void addRow(Map<String, Object> row) {
        this.rows.add(row);
    }

    public List<Map<String, Object>> getRows() {
        return this.rows;
    }

    public void removeTable() {
        this.rows.clear();
    }
}
