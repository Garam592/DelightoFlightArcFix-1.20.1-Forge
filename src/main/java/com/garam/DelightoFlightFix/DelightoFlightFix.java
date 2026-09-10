package com.garam.DelightoFlightFix;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The mod id must match mod_id in gradle.properties and the modId in META-INF/mods.toml
@Mod(DelightoFlightFix.MODID)
public class DelightoFlightFix
{
    public static final String MODID = "delighto_flight_fix";

    private static final Logger LOGGER = LogUtils.getLogger();

    public DelightoFlightFix(FMLJavaModLoadingContext context)
    {
        // Config with the shock blacklist
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("Delight o' Flight Fix loaded");
    }
}
