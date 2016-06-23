package iunius118.mods.tolaserblade.client.renderer;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import iunius118.mods.tolaserblade.tileentity.TileEntityRenderItem;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.util.Constants.NBT;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class RenderItemLaserBlade extends TileEntitySpecialRenderer<TileEntityRenderItem> {

	@Override
	public void renderTileEntityAt(TileEntityRenderItem te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(te != null) return;

		Minecraft mc = Minecraft.getMinecraft();
		IBakedModel model = mc.getRenderItem().getItemModelMesher().getModelManager().getModel(ToLaserBlade.ModelLocations.mrlItemLaserBlade);

		if (model instanceof ModelLaserBlade) {
			ModelLaserBlade modelLaserBlade = (ModelLaserBlade)model;
			doRender(modelLaserBlade);
		}
	}

	public void doRender(ModelLaserBlade model) {
		float partialTicks = Animation.getPartialTickTime();

		VertexBuffer renderer = Tessellator.getInstance().getBuffer();
		int colorCore = 0xFFFFFFFF;
		int colorHalo = 0xFFFF0000;
		boolean isSubColor = false;
		NBTTagCompound nbt = model.itemStack.getTagCompound();

		// Load blade colors from ItemStack NBT.
		if (nbt != null) {
			if (nbt.hasKey(ItemLaserBlade.KEY_COLOR_CORE, NBT.TAG_INT)) {
				colorCore = nbt.getInteger(ItemLaserBlade.KEY_COLOR_CORE);
			}

			if (nbt.hasKey(ItemLaserBlade.KEY_COLOR_HALO, NBT.TAG_INT)) {
				colorHalo = nbt.getInteger(ItemLaserBlade.KEY_COLOR_HALO);
			}

			if (nbt.hasKey(ItemLaserBlade.KEY_SUB_COLOR, NBT.TAG_BYTE)) {
				isSubColor = nbt.getBoolean(ItemLaserBlade.KEY_SUB_COLOR);
			}
		}

		GlStateManager.popMatrix();

		// Transform by Camera type.
		switch (model.cameraTransformType) {
		case FIRST_PERSON_LEFT_HAND:
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(1.0D, 1.25D, 1.0D);
			GlStateManager.translate(0.0F, -0.6F, 0.0F);
			GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
			break;
		case FIRST_PERSON_RIGHT_HAND:
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
			GlStateManager.scale(1.0D, 1.25D, 1.0D);
			GlStateManager.translate(0.0F, -0.6F, 0.0F);
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			break;
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(2.0D, 2.0D, 2.0D);
			GlStateManager.translate(0.0F, -0.45F, 0.0F);
			break;
		case FIXED:
			GlStateManager.scale(0.9D, 0.9D, 0.9D);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(0.0F, -0.75F, -0.04F);
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			break;
		default:
			GlStateManager.scale(0.9D, 0.9D, 0.9D);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
			GlStateManager.translate(0.0F, -0.75F, -0.04F);
			GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
		}

		// Enable Back-face Culling.
		GlStateManager.enableCull();
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);

		// Draw hilt.
		renderQuads(renderer, model.mapQuads.get("Hilt"), -1);

		// Enable Add-color.
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		RenderHelper.disableStandardItemLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		// Draw blade.
		if (isSubColor) {
			// Draw core with Sub-color.
			GL14.glBlendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
			renderQuads(renderer, model.mapQuads.get("Blade_core"), colorCore);
			GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		} else {
			// Draw core with Add-color.
			renderQuads(renderer, model.mapQuads.get("Blade_core"), colorCore);
		}

		renderQuads(renderer, model.mapQuads.get("Blade_halo_1"), colorHalo);
		renderQuads(renderer, model.mapQuads.get("Blade_halo_2"), colorHalo);

		// Disable Add-color.
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopAttrib();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		// Render Enchantment effect.
		if (model.itemStack.hasEffect()) {
			renderEffect(model.mapQuads.get("Hilt"));
		}

		// Disable Culling.
		GlStateManager.disableCull();

		GlStateManager.pushMatrix();
	}

	public void renderEffect(List<BakedQuad> quads) {
		VertexBuffer renderer = Tessellator.getInstance().getBuffer();

		// Render Enchantment effect for hilt.
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);

		renderQuads(renderer, quads, -8372020);

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);

		renderQuads(renderer, quads, -8372020);

		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color) {
		int size = quads.size();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		// Render Quads.
		for (int i = 0; i < size; ++i) {
			LightUtil.renderQuadColor(renderer, quads.get(i), color);
		}

		Tessellator.getInstance().draw();
	}

}
