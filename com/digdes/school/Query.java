package com.digdes.school;

import java.security.Key;
import java.util.*;

public class Query {
    final Table table;

    public Query(Table table) {
        this.table = table;
    }

    public void insert(Map<String, Object> values) {
        this.table.setRow(values);
    }

    public void delete(int rowIndex) {
        this.table.getRows().remove(rowIndex);
    }

    public void update(int rowIndex, Map<String, Object> values) {
        Map<String, Object> row = this.table.getRows().get(rowIndex);
        for (String key : values.keySet()) {
            row.put(key, values.get(key));
        }
    }

    public List<Map<String, Object>> select(List<String> columns) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : this.table.getRows()) {
            Map<String, Object> selectedRow = new HashMap<>();
            for (String column: columns) {
                selectedRow.put(column, row.get(column));
            }
            result.add(selectedRow);
        }
        return result;
    }

    public void inputQuery(String input) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer.tokenize(input));
        Map<String, Object> parseData = parser.Parse();
        WhereClause whereClause = new WhereClause();
        if (parseData.containsKey("SELECT")) {
            if (parseData.get("SELECT").equals(1)) {
                System.out.println(table.getRows());
            } else if (parseData.containsKey("conditions")) {
                   // List<Object> conditions = new ArrayList<>();
                    //conditions = Arrays.asList(parseData.get("conditions"));
                   // for (Object obj : conditions) {
                        //whereClause.addCondition();
                    //}
            }
            else {
                parseData.remove("SELECT");
                List<String> selectColumns = new ArrayList<>();
                for (Map.Entry<String, Object> entry : parseData.entrySet()) {
                    selectColumns.add(entry.getKey());
                }
                System.out.println(select(selectColumns));
            }
        } else if (parseData.containsKey("DELETE")) {
            if (parseData.get("DELETE").equals(1)) {
                table.removeTable();
            } else if (parseData.containsKey("conditions")) {

            }
            else {
                delete((int)parseData.get("id"));
            }

        } else if (parseData.containsKey("UPDATE")) {
            if (parseData.containsKey("conditions")) {

            }
            else {
                parseData.remove("UPDATE");
                int rowIndex = (int)parseData.get("id");
                parseData.remove("id");
                update(rowIndex, parseData);
            }
        } else if (parseData.containsKey("INSERT")) {
            if (parseData.containsKey("conditions")) {
                throw new RuntimeException("Invalid query");
            } else {
                parseData.remove("INSERT");
                insert(parseData);
            }
        }
        



    }

}
