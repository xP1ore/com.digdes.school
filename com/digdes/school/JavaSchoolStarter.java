package com.digdes.school;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    public JavaSchoolStarter() {

    }

    public List<Map<String, Object>> execute(String request) {
        Query query = new Query(new Table());
        query.inputQuery(request);
        return query.table.getRows();
    }
}
