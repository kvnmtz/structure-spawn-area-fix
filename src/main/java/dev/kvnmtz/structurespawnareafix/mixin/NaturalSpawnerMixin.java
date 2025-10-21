package dev.kvnmtz.structurespawnareafix.mixin;

import dev.kvnmtz.structurespawnareafix.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {

    @Inject(
            method = "mobsAt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/StructureManager;"
                    + "Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/entity/MobCategory;"
                    + "Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Holder;)"
                    + "Lnet/minecraft/util/random/WeightedRandomList;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void expandStructureSpawnArea(ServerLevel level, StructureManager structureManager,
                                                 ChunkGenerator chunkGenerator, MobCategory category,
                                                 BlockPos originalPos, @Nullable Holder<Biome> biomeHolder,
                                                 CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {

        for (var entry : Config.getEntries()) {
            if (entry.category() != category) {
                continue;
            }

            if (entry.biome() != null) {
                if (biomeHolder != null && !entry.biome().matches(biomeHolder)) {
                    continue;
                }
            }

            for (var checkPos : BlockPos.betweenClosed(
                    originalPos.offset(-entry.additionalRadius(), 0, -entry.additionalRadius()),
                    originalPos.offset(entry.additionalRadius(), 0, entry.additionalRadius())
            )) {
                var structureStart = structureManager.getStructureAt(checkPos, entry.structure());
                if (structureStart.isValid()) {
                    var spawnOverrides = structureStart.getStructure().getModifiedStructureSettings().spawnOverrides();
                    var monsterSpawnOverride = spawnOverrides.get(category);

                    if (monsterSpawnOverride != null) {
                        var spawns = monsterSpawnOverride.spawns();
                        if (!spawns.isEmpty()) {
                            cir.setReturnValue(spawns);
                            return;
                        }
                    }
                }
            }
        }
    }
}
