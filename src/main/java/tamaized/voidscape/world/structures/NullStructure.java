package tamaized.voidscape.world.structures;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NullStructure extends StructureFeature<NoneFeatureConfiguration> {

	public NullStructure() {
		super(NoneFeatureConfiguration.CODEC, context -> context.chunkPos().x == 0 && context.chunkPos().z == 0 ? Optional.of((builder, generatorContext) -> {
			BlockPos blockpos = new BlockPos(generatorContext.chunkPos().getMinBlockX(), 0, generatorContext.chunkPos().getMinBlockZ());
			Pieces.addPieces(generatorContext.structureManager(), blockpos, Pieces.TEMPLATE_ENTRANCE, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(48, 0, 0), Pieces.TEMPLATE_ENTRANCE_PLATFORM, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(0, 47, 0), Pieces.TEMPLATE_SECTION_ELEMENTS, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(0, 48, 47), Pieces.TEMPLATE_SECTION_ELEMENTS_DOL, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(-47, 62, 0), Pieces.TEMPLATE_SECTION_ELEMENTS_ZOL, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(0, 82, -47), Pieces.TEMPLATE_SECTION_ELEMENTS_YOL, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(0, 94, 0), Pieces.TEMPLATE_SECTION_VIA, builder);
			Pieces.addPieces(generatorContext.structureManager(), blockpos.offset(0, 141, 0), Pieces.TEMPLATE_SECTION_XIA, builder);
		}) : Optional.empty());
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.TOP_LAYER_MODIFICATION;
	}

	@Override
	public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList(MobCategory category) {
		return category == MobCategory.MONSTER ? Lists.newArrayList(new MobSpawnSettings.SpawnerData(ModEntities.NULL_SERVANT.get(), 10, 0, 1)) : new ArrayList<>();
	}

	@Override
	public boolean getDefaultRestrictsSpawnsToInside() {
		return true;
	}

	public static class Pieces {

		public static final StructurePieceType MAIN = RegUtil.registerStructurePiece("NullMain", Piece::new);

		private static final ResourceLocation TEMPLATE_ENTRANCE = new ResourceLocation(Voidscape.MODID, "null/entrance");
		private static final ResourceLocation TEMPLATE_ENTRANCE_PLATFORM = new ResourceLocation(Voidscape.MODID, "null/entrance-platform");
		private static final ResourceLocation TEMPLATE_SECTION_ELEMENTS = new ResourceLocation(Voidscape.MODID, "null/section/elements");
		private static final ResourceLocation TEMPLATE_SECTION_ELEMENTS_DOL = new ResourceLocation(Voidscape.MODID, "null/section/elements/dol");
		private static final ResourceLocation TEMPLATE_SECTION_ELEMENTS_YOL = new ResourceLocation(Voidscape.MODID, "null/section/elements/yol");
		private static final ResourceLocation TEMPLATE_SECTION_ELEMENTS_ZOL = new ResourceLocation(Voidscape.MODID, "null/section/elements/zol");
		private static final ResourceLocation TEMPLATE_SECTION_VIA = new ResourceLocation(Voidscape.MODID, "null/section/via");
		private static final ResourceLocation TEMPLATE_SECTION_XIA = new ResourceLocation(Voidscape.MODID, "null/section/xia");

		public static void addPieces(StructureManager manager, BlockPos pos, ResourceLocation location, StructurePieceAccessor accessor) {
			accessor.addPiece(new Piece(manager, location, pos));
		}

		public static class Piece extends TemplateStructurePiece {

			public Piece(StructureManager manager, ResourceLocation location, BlockPos pos) {
				super(MAIN, 0, manager, location, location.toString(), makeSettings(), pos);
				if (boundingBox.maxY() == 0)
					boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY() + 1, boundingBox.maxZ());
			}

			public Piece(StructureManager manager, CompoundTag tag) {
				super(MAIN, tag, manager, (location) -> makeSettings());
				if (boundingBox.maxY() == 0)
					boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY() + 1, boundingBox.maxZ());
			}

			private static StructurePlaceSettings makeSettings() {
				return (new StructurePlaceSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
			}

			@Override
			public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator generator, Random random, BoundingBox boundingBox, ChunkPos pos, BlockPos bpos) {
				boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY() - 1, boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
				super.postProcess(level, manager, generator, random, boundingBox, pos, bpos);
			}

			@Override
			protected void handleDataMarker(String p_72844_, BlockPos p_72845_, ServerLevelAccessor p_72846_, Random p_72847_, BoundingBox p_72848_) {

			}
		}
	}
}
