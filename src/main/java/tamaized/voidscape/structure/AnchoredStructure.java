package tamaized.voidscape.structure;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class AnchoredStructure extends Structure {

	private static final int MAX_STEP_DOWN = 5;
	private static final int SUPPORT_DEPTH = 2;
	private static final int HEAD_ROOM = 1;
	private static final int MIN_SUPPORT = 7;
	private static final int MIN_CLEAR = 4;
	private static final int SAMPLES_PER_AXIS = 3;

	protected AnchoredStructure(StructureSettings settings) {
		super(settings);
	}

	protected abstract Identifier getTemplate();

	protected abstract void generatePieces(StructurePiecesBuilder piecesBuilder, BlockPos pos, Rotation rotation, GenerationContext context);

	protected abstract StructurePlaceSettings makeSettings(Rotation rotation);

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		Optional<StructureTemplate> template = context.structureTemplateManager().get(getTemplate());
		if (template.isEmpty())
			return Optional.empty();
		Rotation rotation = Rotation.getRandom(context.random());
		BlockPos origin = context.chunkPos().getWorldPosition();
		BoundingBox footprint = template.get().getBoundingBox(makeSettings(rotation), origin);
		int height = template.get().getSize().getY();
		NoiseColumn[] columns = sampleColumns(context, footprint);
		IntList anchors = findAnchors(context, columns, height);
		if (anchors.isEmpty())
			return Optional.empty();
		BlockPos pos = origin.atY(anchors.getInt(context.random().nextInt(anchors.size())));
		return Optional.of(new GenerationStub(pos, piecesBuilder -> this.generatePieces(piecesBuilder, pos, rotation, context)));
	}

	private NoiseColumn[] sampleColumns(GenerationContext context, BoundingBox footprint) {
		NoiseColumn[] columns = new NoiseColumn[SAMPLES_PER_AXIS * SAMPLES_PER_AXIS];
		int spanX = footprint.maxX() - footprint.minX();
		int spanZ = footprint.maxZ() - footprint.minZ();
		int index = 0;
		for (int stepX = 0; stepX < SAMPLES_PER_AXIS; stepX++) {
			for (int stepZ = 0; stepZ < SAMPLES_PER_AXIS; stepZ++) {
				columns[index++] = context.chunkGenerator().getBaseColumn(
					footprint.minX() + spanX * stepX / (SAMPLES_PER_AXIS - 1),
					footprint.minZ() + spanZ * stepZ / (SAMPLES_PER_AXIS - 1),
					context.heightAccessor(),
					context.randomState()
				);
			}
		}
		return columns;
	}

	private IntList findAnchors(GenerationContext context, NoiseColumn[] columns, int height) {
		Predicate<BlockState> opaque = Heightmap.Types.WORLD_SURFACE_WG.isOpaque();
		IntList anchors = new IntArrayList();
		int lowest = context.heightAccessor().getMinY() + MAX_STEP_DOWN + SUPPORT_DEPTH + 1;
		int highest = context.heightAccessor().getMaxY() - height + 1;
		int target = height + HEAD_ROOM;
		for (int y = lowest; y <= highest; y++) {
			if (countClear(columns, opaque, y, target) < MIN_CLEAR)
				continue;
			if (countSupported(columns, opaque, y) >= MIN_SUPPORT)
				anchors.add(y);
		}
		return anchors;
	}

	private int countClear(NoiseColumn[] columns, Predicate<BlockState> opaque, int y, int target) {
		int clear = 0;
		for (NoiseColumn column : columns) {
			if (isClear(column, opaque, y, target))
				clear++;
		}
		return clear;
	}

	private boolean isClear(NoiseColumn column, Predicate<BlockState> opaque, int y, int target) {
		for (int level = 0; level < target; level++) {
			if (opaque.test(column.getBlock(y + level)))
				return false;
		}
		return true;
	}

	private int countSupported(NoiseColumn[] columns, Predicate<BlockState> opaque, int y) {
		int supported = 0;
		for (NoiseColumn column : columns) {
			if (isSupported(column, opaque, y))
				supported++;
		}
		return supported;
	}

	private boolean isSupported(NoiseColumn column, Predicate<BlockState> opaque, int y) {
		for (int drop = 1; drop <= MAX_STEP_DOWN; drop++) {
			if (isThickFloor(column, opaque, y - drop))
				return true;
		}
		return false;
	}

	private boolean isThickFloor(NoiseColumn column, Predicate<BlockState> opaque, int floor) {
		for (int depth = 0; depth <= SUPPORT_DEPTH; depth++) {
			if (!opaque.test(column.getBlock(floor - depth)))
				return false;
		}
		return true;
	}

}
