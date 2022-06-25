package tamaized.voidscape.world.structures;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModStructures;

import java.util.Objects;
import java.util.Optional;

public class NullStructure extends Structure {

	public static final Codec<NullStructure> CODEC = RecordCodecBuilder
			.create(instance -> instance.group(Structure.settingsCodec(instance))
			.apply(instance, NullStructure::new)
	);

	public NullStructure(StructureSettings structureSettings) {
		super(structureSettings);
	}

	static boolean isValidBiome(GenerationContext context) {
		int x = context.chunkPos().getMiddleBlockX();
		int z = context.chunkPos().getMiddleBlockZ();
		int y = 1;
		Holder<Biome> holder = context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), context.randomState().sampler());
		return Objects.equals(holder.unwrapKey().map(ResourceKey::location).orElse(new ResourceLocation("wrongbiome")), ModBiomes.NULL.location());
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.TOP_LAYER_MODIFICATION;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 0, context.chunkPos().getMinBlockZ());
		return context.chunkPos().x == 0 && context.chunkPos().z == 0 && isValidBiome(context) ?
			Optional.of(new GenerationStub(blockpos, Either.left(piecesBuilder -> {
				Pieces.addPieces(context.structureTemplateManager(), blockpos, Pieces.TEMPLATE_ENTRANCE, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(48, 0, 0), Pieces.TEMPLATE_ENTRANCE_PLATFORM, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(0, 47, 0), Pieces.TEMPLATE_SECTION_ELEMENTS, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(0, 48, 47), Pieces.TEMPLATE_SECTION_ELEMENTS_DOL, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(-47, 62, 0), Pieces.TEMPLATE_SECTION_ELEMENTS_ZOL, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(0, 82, -47), Pieces.TEMPLATE_SECTION_ELEMENTS_YOL, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(0, 94, 0), Pieces.TEMPLATE_SECTION_VIA, piecesBuilder);
				Pieces.addPieces(context.structureTemplateManager(), blockpos.offset(0, 141, 0), Pieces.TEMPLATE_SECTION_XIA, piecesBuilder);
			}))) : Optional.empty();
	}

	@Override
	public StructureType<?> type() {
		return ModStructures.StructureTypes.NULL.get();
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

		public static void addPieces(StructureTemplateManager manager, BlockPos pos, ResourceLocation location, StructurePieceAccessor accessor) {
			accessor.addPiece(new Piece(manager, location, pos));
		}

		public static class Piece extends TemplateStructurePiece {

			public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
				this(context.structureTemplateManager(), tag);
			}

			public Piece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos) {
				super(MAIN, 0, manager, location, location.toString(), makeSettings(), pos);
				if (boundingBox.maxY() == 0)
					boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY() + 1, boundingBox.maxZ());
			}

			public Piece(StructureTemplateManager manager, CompoundTag tag) {
				super(MAIN, tag, manager, (location) -> makeSettings());
				if (boundingBox.maxY() == 0)
					boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY() + 1, boundingBox.maxZ());
			}

			private static StructurePlaceSettings makeSettings() {
				return (new StructurePlaceSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
			}

			@Override
			public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos pos, BlockPos bpos) {
				boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY() - 1, boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
				super.postProcess(level, manager, generator, random, boundingBox, pos, bpos);
			}

			@Override
			protected void handleDataMarker(String p_72844_, BlockPos p_72845_, ServerLevelAccessor p_72846_, RandomSource p_72847_, BoundingBox p_72848_) {

			}
		}
	}
}
