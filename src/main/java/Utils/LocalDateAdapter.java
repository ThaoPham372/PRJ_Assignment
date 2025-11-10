package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public LocalDateAdapter() {
        super();
    }

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(FORMATTER.format(value));
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in == null) return null;
        String str = null;
        try {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            str = in.nextString();
            if (str == null || str.isEmpty()) return null;
            return LocalDate.parse(str, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
