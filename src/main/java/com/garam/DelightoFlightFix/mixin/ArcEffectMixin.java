package com.garam.DelightoFlightFix.mixin;

import com.cloudmeow.delightoflight.effect.ArcEffect;
import com.garam.DelightoFlightFix.Config;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArcEffect.class)
public abstract class ArcEffectMixin
{
    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void delightoFlightFix$skipBlacklistedHolder(LivingEntity entity, int amplifier, CallbackInfo ci)
    {
        // If the Arc effect holder itself is blacklisted, don't spawn its ElectricCurrent at all.
        if (Config.isShockBlacklisted(entity))
        {
            ci.cancel();
        }
    }
}
