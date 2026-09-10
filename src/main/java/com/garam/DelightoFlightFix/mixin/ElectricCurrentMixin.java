package com.garam.DelightoFlightFix.mixin;

import com.cloudmeow.delightoflight.entity.ElectricCurrent;
import com.garam.DelightoFlightFix.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ElectricCurrent.class)
public abstract class ElectricCurrentMixin
{
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private List<LivingEntity> filterShockTargets(Level level, Class<LivingEntity> clazz, AABB aabb)
    {
        return level.getEntitiesOfClass(clazz, aabb).stream()
                .filter(entity -> !Config.isShockBlacklisted(entity))
                .toList();
    }
}
