package iunius118.mods.tolaserblade.client.gui;

import java.util.ArrayList;
import java.util.Set;

import iunius118.mods.tolaserblade.Config;
import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGuiFactory implements IModGuiFactory
{

    @Override
    public void initialize(Minecraft minecraftInstance)
    {
    }

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        ArrayList<IConfigElement> elements = new ArrayList<>();

        for (Property property : Config.config.getCategory(Configuration.CATEGORY_GENERAL).getOrderedValues())
        {
            elements.add(new ConfigElement(property));
        }

        return new GuiConfig(parentScreen, elements, ToLaserBlade.MOD_ID, false, false, ToLaserBlade.MOD_NAME);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

}
