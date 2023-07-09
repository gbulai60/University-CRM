package com.university.ui.data.service;

import com.google.gson.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Type;

public class PageableDeserializer implements JsonDeserializer<Pageable> {
    @Override
    public Pageable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int pageNumber = jsonObject.get("pageNumber").getAsInt();
        int pageSize = jsonObject.get("pageSize").getAsInt();
        return PageRequest.of(pageNumber, pageSize);
    }
}
