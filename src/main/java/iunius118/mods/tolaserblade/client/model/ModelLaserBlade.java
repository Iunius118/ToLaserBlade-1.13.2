package iunius118.mods.tolaserblade.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import scala.actors.threadpool.Arrays;

import com.google.common.base.Function;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelLaserBlade implements IPerspectiveAwareModel {

	public IBakedModel originalModel;

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

	public ModelLaserBlade(IBakedModel bakedModelIn) {
		originalModel = bakedModelIn;
		quadsBlade = getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Blade"}));
		quadsHilt = getPartQuads(bakedModelIn, Arrays.asList(new String[]{"Hilt"}));
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
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return originalModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return originalModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Collections.EMPTY_LIST) {

			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
				if (originalModel instanceof ModelLaserBlade) {
					((ModelLaserBlade)originalModel).handleItemState(stack, world, entity);
				}

				return originalModel;
			}

		};
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformTypeIn) {
		Matrix4f matrix;

		if (originalModel != null && originalModel instanceof IPerspectiveAwareModel) {
			matrix = ((IPerspectiveAwareModel) originalModel).handlePerspective(cameraTransformType).getValue();
		} else {
			matrix = TRSRTransformation.identity().getMatrix();
		}

		cameraTransformType = transformTypeIn;

		return Pair.of(this, matrix);
	}

}
