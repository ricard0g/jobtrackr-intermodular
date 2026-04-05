package org.ricardo.jobtrackr.util;

import com.google.gson.Gson;

public class JsonUtil {
    private static final Gson GSON = new Gson();


    // Metodo Generico donde hago uso de la Inferencia de Tipo que ofrece Java para inferir el tipo de los argumentos que hagan uso del metodo
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
