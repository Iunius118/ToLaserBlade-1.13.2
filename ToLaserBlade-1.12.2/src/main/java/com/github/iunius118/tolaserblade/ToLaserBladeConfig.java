package com.github.iunius118.tolaserblade;

import net.minecraftforge.common.config.Config;

@Config(modid = ToLaserBlade.MOD_ID, category = "")
public class ToLaserBladeConfig
{

    @Config.Comment("Common settings. If you are playing a multiplayer game, the server-side settings will be used.")
    @Config.LangKey("tolaserblade.configgui.category.commonConfig")
    public static CommonConfig common = new CommonConfig();

    @Config.Comment("Client-side settings.")
    @Config.LangKey("tolaserblade.configgui.category.clientConfig")
    public static ClientConfig client = new ClientConfig();

    public static class CommonConfig
    {

        @Config.Comment("Enable blocking with Laser Blade.")
        @Config.LangKey("tolaserblade.configgui.enableBlockingWithLaserBlade")
        @Config.Name("enableBlockingWithLaserBlade")
        public boolean isEnabledBlockingWithLaserBlade = false;

        @Config.Comment("An integer value that is a factor of mining speed of Laser Blade.")
        @Config.LangKey("tolaserblade.configgui.laserBladeEfficiency")
        @Config.RangeInt(min = 0, max = 128)
        @Config.Name("laserBladeEfficiency")
        public int laserBladeEfficiency = 12;

    }

    @Config(modid = ToLaserBlade.MOD_ID, category = "client")
    public static class ClientConfig
    {

        @Config.Comment("Enable Laser Blade to use 3D Model.")
        @Config.LangKey("tolaserblade.configgui.enableLaserBlade3DModel")
        @Config.Name("enableLaserBlade3DModel")
        public boolean isEnabledLaserBlade3DModel = true;

        @Config.Comment("Select rendering mode of Laser Blade (0: Default, 1: Disable blending). This option is available when enableLaserBlade3DModel is true.")
        @Config.LangKey("tolaserblade.configgui.laserBladeRenderingMode")
        @Config.RangeInt(min = 0, max = 1)
        @Config.Name("laserBladeRenderingMode")
        public int laserBladeRenderingMode = 0;

    }

}
