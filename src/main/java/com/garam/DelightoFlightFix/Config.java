package com.garam.DelightoFlightFix;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SHOCK_BLACKLIST = BUILDER
            .comment(
                    "Entities that are immune to the Delight o' Flight 'Arc' effect's shock damage.",
                    "Each entry is either an entity type id (e.g. 'minecraft:villager') or an entity type",
                    "tag prefixed with '#' (e.g. '#minecraft:raiders'). Invalid entries are ignored."
            )
            .defineListAllowEmpty("shockBlacklist", List.of(
                    "minecraft:villager",
                    "minecraft:wandering_trader",
                    "minecraft:iron_golem",
                    "minecraft:snow_golem",
                    "minecraft:cow",
                    "minecraft:pig",
                    "minecraft:sheep",
                    "minecraft:goat",
                    "minecraft:chicken",
                    "minecraft:cat",
                    "minecraft:wolf",
                    "minecraft:ocelot",
                    "minecraft:horse",
                    "minecraft:donkey",
                    "minecraft:mule",
                    "minecraft:zombie_horse",
                    "minecraft:skeleton_horse",
                    "minecraft:fox",
                    "minecraft:rabbit",
                    "minecraft:llama",
                    "minecraft:trader_llama",
                    "minecraft:camel",
                    "minecraft:panda",
                    "minecraft:polar_bear",
                    "minecraft:turtle",
                    "minecraft:dolphin",
                    "minecraft:frog",
                    "minecraft:tadpole",
                    "minecraft:axolotl",
                    "minecraft:parrot",
                    "minecraft:bee",
                    "minecraft:sniffer",
                    "minecraft:strider",
                    "minecraft:allay",
                    "delighto_flight:aerolope"
            ), Config::validateEntry);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validateEntry(final Object obj)
    {
        return obj instanceof String;
    }

    public static boolean isShockBlacklisted(LivingEntity entity)
    {
        EntityType<?> type = entity.getType();
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
        if (id == null)
            return false;

        for (String entry : SHOCK_BLACKLIST.get())
        {
            String value = entry.trim();
            if (value.isEmpty())
                continue;

            if (value.startsWith("#"))
            {
                ResourceLocation tagId = ResourceLocation.tryParse(value.substring(1));
                if (tagId != null && type.is(TagKey.create(Registries.ENTITY_TYPE, tagId)))
                    return true;
            }
            else
            {
                ResourceLocation entryId = ResourceLocation.tryParse(value);
                if (entryId != null && entryId.equals(id))
                    return true;
            }
        }
        return false;
    }
}
