package com.lwkl.valuate.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MapParameters {

    private Map<String, Object> parameters;


    public Object get(String name) throws Exception {

        if (!parameters.containsKey(name)) {
            throw new Exception("No parameter '" + name + "' found.");
        }

        return parameters.get(name);
    }
}
