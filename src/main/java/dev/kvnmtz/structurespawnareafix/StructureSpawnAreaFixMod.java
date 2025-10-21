package dev.kvnmtz.structurespawnareafix;

import dev.kvnmtz.structurespawnareafix.config.Config;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(StructureSpawnAreaFixMod.MOD_ID)
public final class StructureSpawnAreaFixMod {
    public static final String MOD_ID = "structure_spawn_area_fix";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public StructureSpawnAreaFixMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void onServerStarting(ServerStartingEvent event) {
        Config.load(event.getServer().registryAccess().registryOrThrow(Registries.STRUCTURE));
    }
}
