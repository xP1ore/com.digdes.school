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
        if (parseData.containsKey("SELECT")) {
            if (parseData.get("SELECT").equals(1)) {
                System.out.println(table.getRows());
            } else if (parseData.containsKey("conditionMap")) {


            } else {
                List<String> selectColumns = new ArrayList<>();
                for (Map.Entry<String, Object> entry : parseData.entrySet()) {
                    selectColumns.add(entry.getKey());
                }
                System.out.println(select(selectColumns));
            }
        } else if (parseData.containsKey("DELETE")) {
            if (parseData.get("DELETE").equals(1)) {
                table.removeTable();
            } else if (parseData.containsKey("conditionMap")) {

            } else {
                delete((int)parseData.get("id"));
            }

        } else if (parseData.containsKey("UPDATE")) {
            if (parseData.containsKey("conditionMap")) {

            } else {
                int rowIndex = (int)parseData.get("id");
                parseData.remove("id");
                update(rowIndex, parseData);
            }
        } else if (parseData.containsKey("INSERT")) {
            if (parseData.containsKey("conditionMap")) {
                throw new RuntimeException("Invalid query");
            } else {
                insert(parseData);
            }
        }
        //if (parseData.containsKey("SELECT") && parseData.containsValue("1")) {
         //   System.out.println(table.getRows());
        //}
        //if (parseData.containsKey("DELETE") && parseData.containsValue("1")) {
          //  table.removeTable();
        //}



    }

}
