package upworksolutions.themagictricks.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.model.MagicCategory;

public class CategoryLoader {
    public static List<MagicCategory> loadCategories(Context context) {
        try {
            InputStream is = context.getAssets().open("magic_categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            Type listType = new TypeToken<ArrayList<MagicCategory>>(){}.getType();
            return gson.fromJson(jsonObject.get("categories"), listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 