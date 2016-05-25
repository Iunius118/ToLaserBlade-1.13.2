package iunius118.mods.tolaserblade.client.model;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.model.TRSRTransformation;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

public class ModelLaserBlade implements IPerspectiveAwareModel {

	public IBakedModel originalModel;
	public IBakedModel guiModel;

	public ItemStack itemStack;
	public World world;
	public EntityLivingBase entity;

	public TransformType cameraTransformType;

	public IBlockState state;
	public EnumFacing side;
	public long rand;

	public List<BakedQuad> quadsBlade;
	public List<BakedQuad> quadsHilt;
	public float partialTicks;

	public boolean isRenderingEffect = false;

	public ModelLaserBlade(IBakedModel bakedModelIn, IBakedModel bakedGUIModelIn) {
		originalModel = bakedModelIn;
		guiModel = bakedGUIModelIn;
		quadsBlade = getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Blade"}));
		quadsHilt = getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Hilt"}));
	}

	public ModelLaserBlade(IBakedModel bakedModelIn, List<BakedQuad> ListBladeQuads, List<BakedQuad> ListHiltQuads, IBakedModel bakedGUIModelIn) {
		originalModel = bakedModelIn;
		guiModel = bakedGUIModelIn;
		quadsBlade = ListBladeQuads;
		quadsHilt = ListHiltQuads;
	}

	public void doRender() {
		partialTicks = Animation.getPartialTickTime();

		VertexBuffer renderer = Tessellator.getInstance().getBuffer();
		int sizeBlade = quadsBlade.size();
		int sizeHilt = quadsHilt.size();
		int color = -1;

		switch (cameraTransformType) {
		case FIRST_PERSON_LEFT_HAND:
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(1.0D, 1.25D, 1.0D);
			GlStateManager.translate(0.0F, -0.6F, 0.0F);
			break;
		case FIRST_PERSON_RIGHT_HAND:
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
			GlStateManager.scale(1.0D, 1.25D, 1.0D);
			GlStateManager.translate(0.0F, -0.6F, 0.0F);
			break;
		case THIRD_PERSON_LEFT_HAND:
			GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(2.0D, 2.0D, 2.0D);
			GlStateManager.translate(0.0F, -0.45F, 0.0F);
			break;
		case THIRD_PERSON_RIGHT_HAND:
			GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(2.0D, 2.0D, 2.0D);
			GlStateManager.translate(0.0F, -0.45F, 0.0F);
			break;
		default:
			GlStateManager.scale(0.9D, 0.9D, 0.9D);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
			GlStateManager.translate(0.0F, -0.75F, -0.04F);
			color = itemStack.hasEffect() ? -1 : -4144960;
		}

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (int i = 0; i < sizeHilt; ++i) {
			LightUtil.renderQuadColor(renderer, quadsHilt.get(i), -1);
		}

		Tessellator.getInstance().draw();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		RenderHelper.disableStandardItemLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (int i = 0; i < sizeBlade; ++i) {
			LightUtil.renderQuadColor(renderer, quadsBlade.get(i), color);
		}

		Tessellator.getInstance().draw();

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopAttrib();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		if (itemStack.hasEffect()) {
			doRenderEffect();
		}
	}

	public void doRenderEffect() {
		VertexBuffer renderer = Tessellator.getInstance().getBuffer();
		int sizeBlade = quadsBlade.size();
		int sizeHilt = quadsHilt.size();

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

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (int i = 0; i < sizeHilt; ++i) {
			LightUtil.renderQuadColor(renderer, quadsHilt.get(i), -8372020);
		}

		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (int i = 0; i < sizeHilt; ++i) {
			LightUtil.renderQuadColor(renderer, quadsHilt.get(i), -8372020);
		}

		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public List<BakedQuad> getPartQuads(IBakedModel bakedModelIn, List<String> visibleGroups) {
		List<BakedQuad> quads = Collections.EMPTY_LIST;

		if (bakedModelIn instanceof OBJBakedModel) {
			OBJModel obj = ((OBJBakedModel)bakedModelIn).getModel();
			OBJState objStateBlade = new OBJState(visibleGroups, true);
			IBakedModel bakedModel = obj.bake(objStateBlade, DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>() {

				@Override
				public TextureAtlasSprite apply(ResourceLocation location) {
					Minecraft mc = Minecraft.getMinecraft();
					return mc.getTextureMapBlocks().getAtlasSprite(location.toString());
				}

			});
			quads = bakedModel.getQuads(null, null, 0);
		}

		return quads;
	}

	public void handleItemState(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLivingBaseIn) {
		itemStack = itemStackIn;
		world = worldIn;
		entity = entityLivingBaseIn;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState blockStateIn, EnumFacing enumFacingIn, long longRand) {
		if (enumFacingIn == null && !isRenderingEffect) {
			state = blockStateIn;
			side = enumFacingIn;
			rand = longRand;

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer renderer = tessellator.getBuffer();
			tessellator.draw();
			GlStateManager.popMatrix();

			doRender();

			GlStateManager.pushMatrix();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

			isRenderingEffect = true;
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return guiModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return guiModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return guiModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return guiModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return guiModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Collections.EMPTY_LIST) {

			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
				if (originalModel instanceof ModelLaserBlade) {
					ModelLaserBlade model = (ModelLaserBlade)originalModel;
					ModelLaserBlade newModel = new ModelLaserBlade(model.originalModel, model.quadsBlade, model.quadsHilt, model.guiModel);
					newModel.handleItemState(stack, world, entity);
					return newModel;
				}

				return originalModel;
			}

		};
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformTypeIn) {
		Matrix4f matrix;

		if (guiModel != null && guiModel instanceof IPerspectiveAwareModel) {
			matrix = ((IPerspectiveAwareModel) guiModel).handlePerspective(transformTypeIn).getValue();
		} else {
			matrix = TRSRTransformation.identity().getMatrix();
		}

		cameraTransformType = transformTypeIn;

		return Pair.of(this, matrix);
	}

}
