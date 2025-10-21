package dev.kvnmtz.structurespawnareafix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.kvnmtz.structurespawnareafix.StructureSpawnAreaFixMod;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("structure-spawn-area-fix.json");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Structure.class, new StructureTypeAdapter())
            .registerTypeAdapter(MobCategory.class, new MobCategoryTypeAdapter())
            .setPrettyPrinting()
            .create();

    public static Registry<Structure> STRUCTURE_REGISTRY;

    private static final List<SpawnAreaExpansionEntry> entries = new ArrayList<>();

    public static void load(Registry<Structure> structureRegistry) {
        STRUCTURE_REGISTRY = structureRegistry;

        if (Files.notExists(CONFIG_PATH)) {
            createDefault();
        }

        try (var reader = new FileReader(CONFIG_PATH.toFile())) {
            var listType = new TypeToken<ArrayList<SpawnAreaExpansionEntry>>() {
            }.getType();
            List<SpawnAreaExpansionEntry> loadedExpansions = GSON.fromJson(reader, listType);
            entries.clear();
            entries.addAll(loadedExpansions);
        } catch (Exception e) {
            StructureSpawnAreaFixMod.LOGGER.error("Failed to load config", e);
        }
    }

    private static void createDefault() {
        try (var writer = new FileWriter(CONFIG_PATH.toFile())) {
            var shipwreck = STRUCTURE_REGISTRY.get(BuiltinStructures.SHIPWRECK.location());
            List<SpawnAreaExpansionEntry> defaultExpansions = new ArrayList<>();
            defaultExpansions.add(new SpawnAreaExpansionEntry(shipwreck, MobCategory.MONSTER, 12,
                    BiomeReference.parse("#minecraft:has_structure/shipwreck")));
            GSON.toJson(defaultExpansions, writer);
        } catch (IOException e) {
            StructureSpawnAreaFixMod.LOGGER.error("Failed to create default config", e);
        }
    }

    public static List<SpawnAreaExpansionEntry> getEntries() {
        return entries;
    }
}
