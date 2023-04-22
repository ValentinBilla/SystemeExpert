package com.company;

import java.util.Arrays;
import java.util.StringJoiner;


public class Regle {
    private final String[] conditions;
    private final String conclusion;

    public Regle(String[] schema) {
        conditions = Arrays.copyOf(schema, schema.length-1);
        conclusion = schema[schema.length-1];
    }

    public String[] getConditions() {
        return conditions;
    }

    public String getConclusion() {
        return conclusion;
    }

    public String[] getSchema() {
        String[] schema = new String[conditions.length + 1];

        for(int i=0; i<conditions.length; i++) {
            schema[i] = conditions[i];
        }
        schema[schema.length-1] = conclusion;

        return schema;
    }
    
    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" & ", "", " => " + conclusion);
        
        for (String condition: conditions) {
            sj.add(condition);
        }

        return sj.toString();
    }

}