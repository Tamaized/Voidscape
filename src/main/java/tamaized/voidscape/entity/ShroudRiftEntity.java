package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.registry.*;

@Configurable
public class ShroudRiftEntity extends AbstractRiftEntity {

	private static final int MAX_SERVANTS = 3;
	private static final double SERVANT_CHECK_RADIUS = 16D;
	private static final double SPAWN_RANGE = 4D;
	private static final double REQUIRED_PLAYER_RANGE = 16D;
	private static final int MIN_SPAWN_DELAY = 200;
	private static final int MAX_SPAWN_DELAY = 400;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponents itemComponents;

	private int spawnDelay = 20;

	public ShroudRiftEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (!(level() instanceof ServerLevel serverLevel))
			return;
		Runnable onClose = () -> serverLevel.getPlayers(player -> !player.isSpectator() && player.position().closerThan(position(), 16.0) && player.isAlive())
			.forEach(advancementTriggers.SHROUD_RIFT_CLOSE_TRIGGER.get()::trigger);
		if (tryCloseRift(
			serverLevel,
			items.materialItems().ASTRAL_CRYSTAL.get(),
			items.materialItems().SHROUD_THREAD.get(),
			onClose
		))
			return;
		if (tryCloseRift(
			serverLevel,
			items.toolSetComponentDirectory().spellTomeSet().ASTRAL_TOME.get(),
			items.toolSetComponentDirectory().spellTomeSet().SHROUD_TOME.get(),
			onClose
		))
			return;
		if (serverLevel.getDifficulty() == Difficulty.PEACEFUL || !serverLevel.hasNearbyAlivePlayer(getX(), getY(), getZ(), REQUIRED_PLAYER_RANGE))
			return;
		if (spawnDelay > 0) {
			spawnDelay--;
			return;
		}
		spawnDelay = MIN_SPAWN_DELAY + random.nextInt(MAX_SPAWN_DELAY - MIN_SPAWN_DELAY);
		trySpawnServant(serverLevel);
	}

	private void trySpawnServant(ServerLevel serverLevel) {
		if (serverLevel.getEntitiesOfClass(
			NullServantEntity.class,
			getBoundingBox().inflate(SERVANT_CHECK_RADIUS),
			EntitySelector.NO_SPECTATORS.and(e -> e instanceof NullServantEntity servant && servant.getItemBySlot(EquipmentSlot.CHEST).is(
				items.modArmorSetComponentDirectory().shroudArmorSet().SHROUD_CHEST
			))
		).size() >= MAX_SERVANTS)
			return;
		EntityType<NullServantEntity> type = entities.NULL_SERVANT.get();
		BlockPos spawnPos = BlockPos.containing(
			getX() + (random.nextDouble() - random.nextDouble()) * SPAWN_RANGE,
			getY(),
			getZ() + (random.nextDouble() - random.nextDouble()) * SPAWN_RANGE
		);
		if (!serverLevel.noCollision(type.getSpawnAABB(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D)))
			return;
		NullServantEntity servant = type.spawn(
			serverLevel,
			this::equipServant,
			spawnPos,
			EntitySpawnReason.SPAWNER,
			false,
			false
		);
		if (servant == null)
			return;
		servant.spawnAnim();
		serverLevel.levelEvent(LevelEvent.PARTICLES_MOBBLOCK_SPAWN, blockPosition(), 0);
	}

	private void equipServant(NullServantEntity servant) {
		servant.setItemSlot(EquipmentSlot.HEAD, new ItemStack(items.modArmorSetComponentDirectory().shroudArmorSet().SHROUD_HELMET));
		ItemStack chestStack = new ItemStack(items.modArmorSetComponentDirectory().shroudArmorSet().SHROUD_CHEST);
		chestStack.set(itemComponents.DRACONIC, true);
		servant.setItemSlot(EquipmentSlot.CHEST, chestStack);
		servant.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
		servant.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
		servant.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(items.toolSetComponentDirectory().astralToolSet().ASTRAL_AXE));
	}

	@Override
	protected void readAdditionalSaveData(ValueInput valueInput) {
		spawnDelay = valueInput.getIntOr("spawnDelay", 20);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput valueOutput) {
		valueOutput.putInt("spawnDelay", spawnDelay);
	}
}
