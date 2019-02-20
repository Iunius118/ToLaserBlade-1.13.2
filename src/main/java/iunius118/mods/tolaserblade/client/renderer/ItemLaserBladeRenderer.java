package iunius118.mods.tolaserblade.client.renderer;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.util.Constants.NBT;

@SuppressWarnings("deprecation") // for ItemCameraTransforms
@OnlyIn(Dist.CLIENT)
public class ItemLaserBladeRenderer extends TileEntityItemStackRenderer {
	@Override
	public void renderByItem(ItemStack itemStackIn) {
		Minecraft mc = Minecraft.getInstance();
		IBakedModel model = mc.getItemRenderer().getItemModelMesher().getModelManager().getModel(ToLaserBlade.MRL_ITEM_LASER_BLADE);

		if (model instanceof ModelLaserBlade) {
			ModelLaserBlade modelLaserBlade = (ModelLaserBlade) model;
			doRender(modelLaserBlade);
		}
	}

	public void doRender(ModelLaserBlade model) {
		BufferBuilder renderer = Tessellator.getInstance().getBuffer();
		int colorCore = 0xFFFFFFFF;
		int colorHalo = 0xFFFF0000;
		boolean isSubColorCore = false;
		boolean isSubColorHalo = false;
		NBTTagCompound nbt = model.itemStack.getTag();

		// Load blade colors from ItemStack NBT.
		if (nbt != null) {
			if (nbt.contains(ItemLaserBlade.KEY_COLOR_CORE, NBT.TAG_INT)) {
				colorCore = nbt.getInt(ItemLaserBlade.KEY_COLOR_CORE);
			}

			if (nbt.contains(ItemLaserBlade.KEY_COLOR_HALO, NBT.TAG_INT)) {
				colorHalo = nbt.getInt(ItemLaserBlade.KEY_COLOR_HALO);
			}

			if (nbt.contains(ItemLaserBlade.KEY_IS_SUB_COLOR_CORE, NBT.TAG_BYTE)) {
				isSubColorCore = nbt.getBoolean(ItemLaserBlade.KEY_IS_SUB_COLOR_CORE);
			}

			if (nbt.contains(ItemLaserBlade.KEY_IS_SUB_COLOR_HALO, NBT.TAG_BYTE)) {
				isSubColorHalo = nbt.getBoolean(ItemLaserBlade.KEY_IS_SUB_COLOR_HALO);
			}
		}

		TransformType cameraTransformType = model.cameraTransformType;

		// Transform by Camera type.
		transform(cameraTransformType);

		// Enable Back-face Culling.
		if (cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			GlStateManager.enableCull();
			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		}

		// Draw hilt.
		renderQuads(renderer, model.mapQuads.get("Hilt"), -1);

		// Enable bright rendering.
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		RenderHelper.disableStandardItemLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, 240.0F, 240.0F);

		// Draw bright part of hilt.
		renderQuads(renderer, model.mapQuads.get("Hilt_bright"), -1);

		// Enable Add-color.
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);

		// Draw blade core.
		if (isSubColorCore) {
			// Draw core with Sub-color.
			GL14.glBlendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
		}

		renderQuads(renderer, model.mapQuads.get("Blade_core"), colorCore);

		// Draw blade halo.
		if (!isSubColorCore && isSubColorHalo) {
			// Draw halo with Sub-color.
			GL14.glBlendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
		} else if (isSubColorCore && !isSubColorHalo) {
			GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		}

		renderQuads(renderer, model.mapQuads.get("Blade_halo_1"), colorHalo);
		renderQuads(renderer, model.mapQuads.get("Blade_halo_2"), colorHalo);

		if (isSubColorHalo) {
			GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		}

		// Disable Add-color and bright rendering.
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopAttrib();

		// Render Enchantment effect.
		if (model.itemStack.hasEffect()) {
			renderEffect(model.mapQuads.get("Hilt"));
			renderEffect(model.mapQuads.get("Hilt_bright"));
		}

		// Disable Culling.
		if (cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND || cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			GlStateManager.disableCull();
		}
	}

	public static final Map<TransformType, float[]> transformMatrices;
	static {
		transformMatrices = new HashMap<>();
		transformMatrices.put(TransformType.FIRST_PERSON_LEFT_HAND, new float[] { -3.090862E-8F, 3.090862E-8F, -1.0F, 0.0F, 0.8838835F, 0.8838835F, 0.0F, 0.0F, 0.70710677F, -0.70710677F, -4.371139E-8F, 0.0F, -0.030330122F, -0.030330122F, 0.5F, 1.0F });
		transformMatrices.put(TransformType.FIRST_PERSON_RIGHT_HAND, new float[] { -3.090862E-8F, 3.090862E-8F, -1.0F, 0.0F, 0.8838835F, 0.8838835F, 0.0F, 0.0F, 0.70710677F, -0.70710677F, -4.371139E-8F, 0.0F, -0.030330122F, -0.030330122F, 0.5F, 1.0F });
		transformMatrices.put(TransformType.THIRD_PERSON_LEFT_HAND, new float[] { -3.244294E-8F, 4.633332E-8F, -1.294F, 0.0F, 0.94637173F, 0.8825059F, 7.871984E-9F, 0.0F, 0.8825059F, -0.94637173F, -5.6012073E-8F, 0.0F, 0.035000555F, 0.030994587F, 0.5F, 1.0F });
		transformMatrices.put(TransformType.THIRD_PERSON_RIGHT_HAND, new float[] { -3.244294E-8F, 4.633332E-8F, -1.294F, 0.0F, 0.94637173F, 0.8825059F, 7.871984E-9F, 0.0F, 0.8825059F, -0.94637173F, -5.6012073E-8F, 0.0F, 0.035000555F, 0.030994587F, 0.5F, 1.0F });
		transformMatrices.put(TransformType.FIXED, new float[] { -5.0862745E-8F, -2.7817755E-8F, -0.9F, 0.0F, 0.63639605F, 0.63639605F, -5.5635514E-8F, 0.0F, 0.63639605F, -0.63639605F, -1.6295264E-8F, 0.0F, 0.022702962F, 0.022702962F, 0.46400005F, 1.0F });
		transformMatrices.put(TransformType.NONE, new float[] { -2.7817755E-8F, 2.7817755E-8F, -0.9F, 0.0F, 0.63639605F, 0.63639605F, 0.0F, 0.0F, 0.63639605F, -0.63639605F, -3.934025E-8F, 0.0F, 0.022702962F, 0.022702962F, 0.5F, 1.0F });
	}

	private static final FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);

	public void transform(TransformType cameraTransformType) {
		matrixBuf.clear();
		float[] matrix = transformMatrices.get(cameraTransformType);

		if (matrix == null) {
			matrix = transformMatrices.get(TransformType.NONE);
		}

		matrixBuf.put(matrix);

		/* // Calculate transformation matrix (for debugging)
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		switch (cameraTransformType)
		{
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
		    GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
		    GlStateManager.scale(1.0D, 1.25D, 1.0D);
		    GlStateManager.translate(0.0F, -0.6F, 0.0F);
		    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		    break;
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
		    GlStateManager.rotate(-55.0F, 0.0F, 0.0F, 1.0F);
		    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		    GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
		    GlStateManager.scale(1.294D, 1.294D, 1.294D);
		    GlStateManager.translate(0.0F, -0.45F, 0.0F);
		    GlStateManager.translate(0.0F, -0.06F, 0.02F);
		    break;
		case FIXED:
		    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		    GlStateManager.scale(0.9D, 0.9D, 0.9D);
		    GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
		    GlStateManager.translate(0.0F, -0.75F, 0.04F);
		    GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
		    break;
		default:
		    GlStateManager.scale(0.9D, 0.9D, 0.9D);
		    GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
		    GlStateManager.translate(0.0F, -0.75F, 0.0F);
		    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		}
		matrixBuf.clear();
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, matrixBuf);
		GlStateManager.popMatrix();
		matrix = new float[16];
		matrixBuf.get(matrix);
		// */

		matrixBuf.flip();
		GlStateManager.multMatrixf(matrixBuf);
	}

	public void renderEffect(List<BakedQuad> quads) {
		BufferBuilder renderer = Tessellator.getInstance().getBuffer();

		// Render Enchantment effect for hilt.
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(GL11.GL_EQUAL);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getInstance().getTextureManager().bindTexture(ItemRenderer.RES_ITEM_GLINT);
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.scalef(8.0F, 8.0F, 8.0F);
		float f = (float) (Util.milliTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translatef(f, 0.0F, 0.0F);
		GlStateManager.rotatef(-50.0F, 0.0F, 0.0F, 1.0F);

		renderQuads(renderer, quads, 0xFF8040CC);

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scalef(8.0F, 8.0F, 8.0F);
		float f1 = (float) (Util.milliTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translatef(-f1, 0.0F, 0.0F);
		GlStateManager.rotatef(10.0F, 0.0F, 0.0F, 1.0F);

		renderQuads(renderer, quads, 0xFF8040CC);

		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(true);
		Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color) {
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		// Render Quads.
		for (BakedQuad quad : quads) {
			LightUtil.renderQuadColor(renderer, quad, color);
			Vec3i vec3i = quad.getFace().getDirectionVec();
			renderer.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
		}

		Tessellator.getInstance().draw();
	}
}
