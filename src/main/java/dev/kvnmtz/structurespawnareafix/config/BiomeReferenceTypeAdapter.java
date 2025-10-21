package dev.kvnmtz.structurespawnareafix.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BiomeReferenceTypeAdapter extends TypeAdapter<BiomeReference> {
    @Override
    public void write(JsonWriter out, BiomeReference value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toConfigString());
        }
    }

    @Override
    public BiomeReference read(JsonReader in) throws IOException {
        var reference = in.nextString();
        return BiomeReference.parse(reference);
    }
}
