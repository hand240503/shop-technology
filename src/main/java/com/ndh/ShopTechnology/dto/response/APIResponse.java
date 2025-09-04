package com.ndh.ShopTechnology.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    private Map<String, Object> additionalFields = new HashMap<>();

    @JsonAnySetter
    public void addField(String key, Object value) {
        additionalFields.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public static APIResponse doResponse(Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments must be even (key-value pairs).");
        }

        APIResponse response = new APIResponse();

        for (int i = 0; i < args.length; i += 2) {
            String key = (String) args[i];
            Object value = args[i + 1];
            response.addField(key, value);
        }

        return response;
    }
}
