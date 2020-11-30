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
    List<Photo> photos = new ArrayList<Photo>();
    @Override
    public List<Photo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray photos_j = json.getAsJsonObject().get("photos").getAsJsonArray();

        for (int i=0; i < photos_j.size(); i++) {
            int id = photos_j.get(i).getAsJsonObject().get("id").getAsInt();
            String url = photos_j.get(i).getAsJsonObject().get("url").getAsString();
            String large = photos_j.get(i).getAsJsonObject().get("src").getAsJsonObject().get("large").getAsString();
            String large2x = photos_j.get(i).getAsJsonObject().get("src").getAsJsonObject().get("large2x").getAsString();
            String medium = photos_j.get(i).getAsJsonObject().get("src").getAsJsonObject().get("medium").getAsString();

            Photo currentPhoto = new Photo(id, url, large, large2x, medium);

            if(!photos.contains(currentPhoto))
                photos.add(currentPhoto);
        }

        return photos;
    }
}
