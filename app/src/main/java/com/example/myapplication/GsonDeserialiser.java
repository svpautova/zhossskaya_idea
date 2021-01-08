package com.example.myapplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonDeserialiser implements JsonDeserializer<List<Photo>> {
    List<Photo> photos = new ArrayList<>();
    @Override
    public List<Photo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray photosJ = json.getAsJsonObject().get("photos").getAsJsonArray();

        for (int i=0; i < photosJ.size(); i++) {
            int id = photosJ.get(i).getAsJsonObject().get("id").getAsInt();
            String photosrc = photosJ.get(i).getAsJsonObject().get("src").getAsJsonObject().get("portrait").getAsString();
            Photo currentPhoto = new Photo(id, photosrc);

            if(!photos.contains(currentPhoto))
                photos.add(currentPhoto);
        }

        return photos;
    }
}
