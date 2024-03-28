package com.example.medihealth.retrofitcustom;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public LocalTime read(JsonReader in) throws IOException {
        String timeString = in.nextString();
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}

