package dev.vaxium;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaxiumClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Vaxium");

    @Override
    public void onInitializeClient() {
        VaxiumConfig cfg = VaxiumConfig.get();
        LOGGER.info("Vaxium loaded (enabled={}, entities={}m, block entities={}m, particles<={})",
                cfg.enabled, cfg.entityMaxDistance, cfg.blockEntityMaxDistance, cfg.maxParticles);
    }
}
