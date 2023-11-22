package tamaized.voidscape.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModStructures;

import java.util.Optional;

public class CharredStructure extends Structure {

	public static final Codec<CharredStructure> CODEC = simpleCodec(CharredStructure::new);

	public CharredStructure(Structure.StructureSettings p_227526_) {
		super(p_227526_);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		Rotation rotation = Rotation.getRandom(context.random());
		BlockPos pos = new BlockPos(
				context.chunkPos().getWorldPosition().getX(),
				getLowestY(context, 12, 12),//context.random().nextInt((context.heightAccessor().getMaxBuildHeight() - 1) - (context.heightAccessor().getMinBuildHeight() + 1)) + context.heightAccessor().getMinBuildHeight() + 1,
				context.chunkPos().getWorldPosition().getZ()
		);
		return Optional.of(new Structure.GenerationStub(pos, piecesBuilder -> this.generatePieces(piecesBuilder, pos.offset(6, -1, 6), rotation, context)));
	}

	private void generatePieces(StructurePiecesBuilder piecesBuilder, BlockPos pos, Rotation rotation, Structure.GenerationContext context) {
		Pieces.addPieces(context.structureTemplateManager(), pos, rotation, Pieces.TEMPLATE, piecesBuilder);
	}

	@Override
	public StructureType<?> type() {
		return ModStructures.StructureTypes.CHARRED.get();
	}

	public static class Pieces {
		private static final ResourceLocation TEMPLATE = new ResourceLocation(Voidscape.MODID, "charred");

		public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, ResourceLocation location, StructurePieceAccessor accessor) {
			accessor.addPiece(new Piece(manager, location, pos, rotation));
		}

		public static class Piece extends TemplateStructurePiece {

			public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
				this(context.structureTemplateManager(), tag);
			}

			public Piece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
				super(ModStructures.Pieces.CHARRED_MAIN.get(), 0, manager, location, location.toString(), makeSettings(rotation), pos);
			}

			public Piece(StructureTemplateManager manager, CompoundTag tag) {
				super(ModStructures.Pieces.CHARRED_MAIN.get(), tag, manager, (location) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
			}

			private static StructurePlaceSettings makeSettings(Rotation rotation) {
				return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
			}

			@Override
			public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos pos, BlockPos bpos) {
				boundingBox = new BoundingBox(boundingBox.minX(), boundingBox.minY() - 1, boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
				super.postProcess(level, manager, generator, random, boundingBox, pos, bpos);
			}

			@Override
			protected void handleDataMarker(String p_72844_, BlockPos p_72845_, ServerLevelAccessor p_72846_, RandomSource p_72847_, BoundingBox p_72848_) {

			}

			@Override
			protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
				super.addAdditionalSaveData(context, tag);
				tag.putString("Rot", this.placeSettings.getRotation().name());
			}
		}

	}

}
