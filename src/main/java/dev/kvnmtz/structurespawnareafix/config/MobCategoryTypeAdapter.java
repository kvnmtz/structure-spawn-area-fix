package dev.kvnmtz.structurespawnareafix.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.kvnmtz.structurespawnareafix.StructureSpawnAreaFixMod;
import net.minecraft.world.entity.MobCategory;

import java.io.IOException;

public class MobCategoryTypeAdapter extends TypeAdapter<MobCategory> {
    @Override
    public void write(JsonWriter out, MobCategory value) throws IOException {
        out.value(value.getName());
    }

    @Override
    public MobCategory read(JsonReader in) throws IOException {
        var categoryName = in.nextString();

        var mobCategory = MobCategory.byName(categoryName);
        //noinspection ConstantValue - this can indeed be null
        if (mobCategory == null) {
            StructureSpawnAreaFixMod.LOGGER.error("Invalid mob category: {}", categoryName);
        }

        return mobCategory;
    }
}
