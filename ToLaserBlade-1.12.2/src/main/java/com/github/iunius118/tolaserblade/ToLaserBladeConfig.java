package com.github.iunius118.tolaserblade;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ToLaserBladeConfig {

    public static Configuration config;

    public static Property propIsEnabledLaserBlade3DModel;

    public static void loadConfig(FMLPreInitializationEvent event)
    {
        config = new Configuration( event.getSuggestedConfigurationFile() );
        config.load();

        propIsEnabledLaserBlade3DModel = ToLaserBladeConfig.config.get(Configuration.CATEGORY_GENERAL,
                "enableLaserBlade3DModel", ToLaserBlade.isEnabledLaserBlade3DModel,
                "Enable Laser Blade to use 3D Model.").setLanguageKey("tolaserblade.config.enableLaserBlade3DModel");

        ToLaserBlade.isEnabledLaserBlade3DModel = propIsEnabledLaserBlade3DModel.getBoolean();

        config.save();
    }

 }
