package com.example.myapplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonDeserialiser implements JsonDeserializer<List<String>> {
    List<String> photos = new ArrayList<>();
    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray photosJ = json.getAsJsonObject().get("photos").getAsJsonArray();

        for (int i=0; i < photosJ.size(); i++) {
            String photo = photosJ.get(i).getAsJsonObject().get("src").getAsJsonObject().get("portrait").getAsString();

            if(!photos.contains(photo))
                photos.add(photo);
        }
        return photos;
    }
}
