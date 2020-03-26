package com.shukla.tech.hospitalapplication;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

class DataConverters {
    @TypeConverter
    public static AddData fromAddDataString(String value) {
        Type listType = new TypeToken<AddData>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromAddDataArrayList(AddData list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
