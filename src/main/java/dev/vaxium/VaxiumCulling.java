package dev.vaxium;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class VaxiumCulling {
    private VaxiumCulling() {}

    /** x/y/z = camera position (as passed to EntityRenderDispatcher#shouldRender). */
    public static boolean shouldCullEntity(Entity e, double cx, double cy, double cz) {
        VaxiumConfig cfg = VaxiumConfig.get();
        if (!cfg.enabled) return false;

        // never hide things the player cares about / that are visually huge
        if (e instanceof PlayerEntity || e.isGlowing() || e.hasPassengers() || e.hasVehicle()) return false;
        if (e.getWidth() > 3.0f || e.getHeight() > 5.0f) return false; // dragon, wither, ghast...
        if (e == MinecraftClient.getInstance().getCameraEntity()) return false;

        double dx = e.getX() - cx, dy = e.getY() - cy, dz = e.getZ() - cz;
        double d2 = dx * dx + dy * dy + dz * dz;

        double max = isSmall(e) ? cfg.smallEntityMaxDistance : cfg.entityMaxDistance;
        return d2 > max * max;
    }

    private static boolean isSmall(Entity e) {
        return e instanceof ItemEntity || e instanceof ExperienceOrbEntity
                || e instanceof ItemFrameEntity || e instanceof ArmorStandEntity;
    }

    public static boolean shouldCullBlockEntity(BlockEntity be) {
        VaxiumConfig cfg = VaxiumConfig.get();
        if (!cfg.enabled) return false;
        // beacon beams and end portals are meant to be seen from afar
        if (be instanceof BeaconBlockEntity || be instanceof EndPortalBlockEntity) return false;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.gameRenderer == null) return false;
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        double max = cfg.blockEntityMaxDistance;
        return be.getPos().getSquaredDistance(cam.x, cam.y, cam.z) > max * max;
    }
}
