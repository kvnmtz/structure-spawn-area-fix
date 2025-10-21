package dev.kvnmtz.structurespawnareafix.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.kvnmtz.structurespawnareafix.StructureSpawnAreaFixMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.io.IOException;

public class StructureTypeAdapter extends TypeAdapter<Structure> {
    @Override
    public void write(JsonWriter out, Structure value) throws IOException {
        if (Config.STRUCTURE_REGISTRY == null) {
            throw new IOException("Structure registry not available");
        }

        var resourceKey = Config.STRUCTURE_REGISTRY.getResourceKey(value).orElseThrow();
        out.value(resourceKey.location().toString());
    }

    @Override
    public Structure read(JsonReader in) throws IOException {
        if (Config.STRUCTURE_REGISTRY == null) {
            throw new IOException("Structure registry not available");
        }

        var structureKey = in.nextString();
        var structure = Config.STRUCTURE_REGISTRY.get(ResourceLocation.parse(structureKey));
        if (structure == null) {
            StructureSpawnAreaFixMod.LOGGER.error("Failed to find structure for key: {}", structureKey);
            return null;
        }

        return structure;
    }
}
