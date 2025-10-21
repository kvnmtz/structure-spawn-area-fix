package dev.kvnmtz.structurespawnareafix.config;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

public record SpawnAreaExpansionEntry(
        @JsonAdapter(StructureTypeAdapter.class) Structure structure,
        @JsonAdapter(MobCategoryTypeAdapter.class) MobCategory category,
        @SerializedName("additional_radius") int additionalRadius,
        @Nullable @JsonAdapter(BiomeReferenceTypeAdapter.class) BiomeReference biome
) {
}
