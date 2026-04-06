package org.ricardo.jobtrackr.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Gson no tiene adaptador nativo de serializacion/deserializacion para campos como LocalDateTime o LocalDate, por ello hay que añadir manualmente los
    // adaptadores, usando estos formatos. Siguiendo la implementacion que aparece en la documentacion de Gson https://google.github.io/gson/UserGuide.html#custom-serialization-and-deserialization
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(DATE_TIME_FORMATTER));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(DATE_FORMATTER));
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DATE_TIME_FORMATTER);
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DATE_FORMATTER);
                }
            })
            .create();

    // Metodo Generico donde hago uso de la Inferencia de Tipo que ofrece Java para inferir el tipo de los argumentos que hagan uso del metodo
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
