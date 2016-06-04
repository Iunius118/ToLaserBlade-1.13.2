package iunius118.mods.tolaserblade.client.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import iunius118.mods.tolaserblade.item.ItemLaserBlade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.Constants.NBT;

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

	public Map<String, List<BakedQuad>> mapQuads;
	public float partialTicks;

	public boolean isRenderingEffect = false;

	public ModelLaserBlade(IBakedModel bakedModelIn, IBakedModel bakedGUIModelIn) {
		this(bakedModelIn, bakedGUIModelIn, false);
	}

	public ModelLaserBlade(IBakedModel bakedModelIn, IBakedModel bakedGUIModelIn, boolean isInitialized) {
		originalModel = bakedModelIn;
		guiModel = bakedGUIModelIn;

		if (!isInitialized) {
			mapQuads = new HashMap<String, List<BakedQuad>>();
			mapQuads.put("Hilt", getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Hilt"})));
			mapQuads.put("Blade_core", getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Blade_core"})));
			mapQuads.put("Blade_halo_1", getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Blade_halo_1"})));
			mapQuads.put("Blade_halo_2", getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Blade_halo_2"})));
		}
	}

	public void doRender() {
		partialTicks = Animation.getPartialTickTime();

		VertexBuffer renderer = Tessellator.getInstance().getBuffer();
		int colorCore = 0xFFFFFFFF;
		int colorHalo = 0xFFFF0000;
		NBTTagCompound nbt = itemStack.getTagCompound();

		if (nbt != null) {
			if (nbt.hasKey(ItemLaserBlade.KEY_COLOR_CORE, NBT.TAG_INT)) {
				colorCore = nbt.getInteger(ItemLaserBlade.KEY_COLOR_CORE);
			}

			if (nbt.hasKey(ItemLaserBlade.KEY_COLOR_HALO, NBT.TAG_INT)) {
				colorHalo = nbt.getInteger(ItemLaserBlade.KEY_COLOR_HALO);
			}
		}

		switch (cameraTransformType) {
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
		default:
			GlStateManager.scale(0.9D, 0.9D, 0.9D);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, -1.0F);
			GlStateManager.translate(0.0F, -0.75F, -0.04F);
			GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
		}

		GlStateManager.enableCull();
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		renderQuads(renderer, mapQuads.get("Hilt"), -1);

		Tessellator.getInstance().draw();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		RenderHelper.disableStandardItemLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		renderQuads(renderer, mapQuads.get("Blade_core"), colorCore);
		renderQuads(renderer, mapQuads.get("Blade_halo_1"), colorHalo);
		renderQuads(renderer, mapQuads.get("Blade_halo_2"), colorHalo);

		Tessellator.getInstance().draw();

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopAttrib();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.disableCull();

		if (itemStack.hasEffect()) {
			renderEffect();
		}
	}

	public void renderEffect() {
		VertexBuffer renderer = Tessellator.getInstance().getBuffer();
		List<BakedQuad> quadsHilt = mapQuads.get("Hilt");

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

		renderQuads(renderer, quadsHilt, -8372020);

		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		renderQuads(renderer, quadsHilt, -8372020);

		Tessellator.getInstance().draw();

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

		for (int i = 0; i < size; ++i) {
			LightUtil.renderQuadColor(renderer, quads.get(i), color);
		}
	}

	public List<BakedQuad> getPartQuads(IBakedModel bakedModelIn, final List<String> visibleGroups) {
		List<BakedQuad> quads = Collections.EMPTY_LIST;

		if (bakedModelIn instanceof OBJBakedModel) {
			OBJModel obj = ((OBJBakedModel)bakedModelIn).getModel();

			IModelState state = new IModelState() {

				@Override
				public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
					if (part.isPresent()) {
						UnmodifiableIterator<String> parts = Models.getParts(part.get());

						while (parts.hasNext()) {
							if (visibleGroups.contains(parts.next())) {
								return Optional.absent();
							}
						}
					}

					return Optional.of(TRSRTransformation.identity());
				}

			};

			IBakedModel bakedModel = obj.bake(state, DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>() {

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
					ModelLaserBlade newModel = new ModelLaserBlade(model.originalModel, model.guiModel, true);
					newModel.mapQuads = model.mapQuads;
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
