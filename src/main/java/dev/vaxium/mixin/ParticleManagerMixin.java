package dev.vaxium.mixin;

import dev.vaxium.VaxiumConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void vaxium$capParticles(Particle particle, CallbackInfo ci) {
        VaxiumConfig cfg = VaxiumConfig.get();
        if (!cfg.enabled || cfg.maxParticles <= 0) return;
        int total = 0;
        for (Queue<Particle> q : particles.values()) total += q.size();
        if (total >= cfg.maxParticles) ci.cancel();
    }
}
