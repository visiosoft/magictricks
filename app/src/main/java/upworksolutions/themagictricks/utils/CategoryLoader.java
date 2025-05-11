package upworksolutions.themagictricks.utils;

import android.content.Context;
import android.util.Log;

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
    private static final String TAG = "CategoryLoader";

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
            List<MagicCategory> categories = gson.fromJson(jsonObject.get("categories"), listType);
            
            if (categories == null) {
                Log.e(TAG, "Failed to parse categories from JSON");
                return new ArrayList<>();
            }
            
            return categories;
        } catch (IOException e) {
            Log.e(TAG, "Error loading categories from JSON", e);
            return new ArrayList<>();
        }
    }
} 