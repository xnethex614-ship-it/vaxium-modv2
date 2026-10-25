package dev.vaxium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Config file: .minecraft/config/vaxium.json (created on first launch). */
public class VaxiumConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static VaxiumConfig instance;

    public boolean enabled = true;

    /** Normal entities (mobs, minecarts, boats...) further than this are not rendered. */
    public double entityMaxDistance = 64.0;
    /** Cheap-but-numerous entities (items, xp orbs, item frames, armor stands). */
    public double smallEntityMaxDistance = 32.0;
    /** Chests, signs, banners, skulls, etc. further than this are not rendered. */
    public double blockEntityMaxDistance = 48.0;
    /** Hard cap of simultaneously alive particles. 0 = no cap. */
    public int maxParticles = 1500;

    public static VaxiumConfig get() {
        if (instance == null) load();
        return instance;
    }

    private static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("vaxium.json");
        try {
            if (Files.exists(path)) {
                instance = GSON.fromJson(Files.readString(path), VaxiumConfig.class);
            }
        } catch (Exception e) {
            VaxiumClient.LOGGER.warn("Could not read vaxium.json, using defaults", e);
        }
        if (instance == null) instance = new VaxiumConfig();
        try {
            Files.writeString(path, GSON.toJson(instance));
        } catch (IOException e) {
            VaxiumClient.LOGGER.warn("Could not write vaxium.json", e);
        }
    }
}
