package dev.vaxium.mixin;

import dev.vaxium.VaxiumCulling;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void vaxium$distanceCull(E entity, Frustum frustum, double x, double y, double z,
                                                        CallbackInfoReturnable<Boolean> cir) {
        if (VaxiumCulling.shouldCullEntity(entity, x, y, z)) {
            cir.setReturnValue(false);
        }
    }
}
