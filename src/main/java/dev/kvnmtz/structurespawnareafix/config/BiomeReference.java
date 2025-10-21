package dev.kvnmtz.structurespawnareafix.config;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public sealed interface BiomeReference {

    boolean matches(@NotNull Holder<Biome> biomeHolder);

    record Direct(ResourceKey<Biome> key) implements BiomeReference {
        @Override
        public boolean matches(@NotNull Holder<Biome> biomeHolder) {
            return biomeHolder.is(key);
        }
    }

    record Tag(TagKey<Biome> tag) implements BiomeReference {
        @Override
        public boolean matches(@NotNull Holder<Biome> biomeHolder) {
            return biomeHolder.is(tag);
        }
    }

    static BiomeReference parse(String reference) {
        if (reference.startsWith("#")) {
            var tagKey = TagKey.create(Registries.BIOME, ResourceLocation.parse(reference.substring(1)));
            return new Tag(tagKey);
        } else {
            var biomeKey = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(reference));
            return new Direct(biomeKey);
        }
    }

    default String toConfigString() {
        if (this instanceof Direct direct) {
            return direct.key().location().toString();
        } else if (this instanceof Tag tag) {
            return "#" + tag.tag().location();
        }
        throw new IllegalStateException("Unknown BiomeReference type: " + this.getClass());
    }
}
