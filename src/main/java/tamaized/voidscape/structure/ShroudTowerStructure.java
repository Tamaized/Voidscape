package tamaized.voidscape.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.ShroudRiftEntity;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemComponentDirectory;
import tamaized.voidscape.registry.structure.ModStructurePieces;
import tamaized.voidscape.registry.structure.ModStructures;

public class ShroudTowerStructure extends AnchoredStructure {

	public static final MapCodec<ShroudTowerStructure> CODEC = simpleCodec(ShroudTowerStructure::new);
	private static final Identifier TEMPLATE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "shroud_tower");

	@Autowired
	private static ModStructures structures;

	@Autowired
	private static ModStructurePieces structurePieces;

	@Autowired
	private static ModEntities entities;

	@Autowired
	private static ModItemComponentDirectory items;

	public ShroudTowerStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	protected Identifier getTemplate() {
		return TEMPLATE;
	}

	@Override
	protected void generatePieces(StructurePiecesBuilder piecesBuilder, BlockPos pos, Rotation rotation, GenerationContext context) {
		Pieces.addPieces(context.structureTemplateManager(), pos, rotation, TEMPLATE, piecesBuilder);
	}

	@Override
	protected StructurePlaceSettings makeSettings(Rotation rotation) {
		return Pieces.Piece.makeSettings(rotation);
	}

	@Override
	public StructureType<?> type() {
		return structures.SHROUD_TOWER.get();
	}

	public static class Pieces {

		public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, Identifier location, StructurePieceAccessor accessor) {
			accessor.addPiece(new Piece(manager, location, pos, rotation));
		}

		public static class Piece extends TemplateStructurePiece {

			public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
				this(context.structureTemplateManager(), tag);
			}

			public Piece(StructureTemplateManager manager, Identifier location, BlockPos pos, Rotation rotation) {
				super(structurePieces.SHROUD_TOWER_MAIN.get(), 0, manager, location, location.toString(), makeSettings(rotation), pos);
			}

			public Piece(StructureTemplateManager manager, CompoundTag tag) {
				super(structurePieces.SHROUD_TOWER_MAIN.get(), tag, manager, (_) -> makeSettings(Rotation.valueOf(tag.getStringOr("Rot", "none"))));
			}

			private static StructurePlaceSettings makeSettings(Rotation rotation) {
				return new StructurePlaceSettings()
					.setRotation(rotation)
					.setMirror(Mirror.NONE)
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
			}

			@Override
			protected void handleDataMarker(String dataName, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {
				switch (dataName) {
					case "rift_bottom", "rift_top" -> createRift(serverLevelAccessor, blockPos);
				}
			}

			private void createRift(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos) {
				ShroudRiftEntity rift = entities.SHROUD_RIFT.get().create(serverLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
				if (rift == null)
					return;
				rift.snapTo(blockPos.below(), 0F, 0F);
				serverLevelAccessor.addFreshEntityWithPassengers(rift);
			}

			@Override
			protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
				super.addAdditionalSaveData(context, tag);
				tag.putString("Rot", this.placeSettings.getRotation().name());
			}
		}

	}

}
