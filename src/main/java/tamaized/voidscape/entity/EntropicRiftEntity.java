package tamaized.voidscape.entity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import javax.annotation.Nullable;

@Configurable
public class EntropicRiftEntity extends AbstractRiftEntity implements IEntityWithComplexSpawn {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Nullable
	private Player target;
	private boolean shouldDissipate = false;

	public EntropicRiftEntity(EntityType<?> type, Level level) {
		this(type, level, null);
	}

	public EntropicRiftEntity(EntityType<?> type, Level level, @Nullable Player target) {
		super(type, level);
		this.target = target;
	}

	public EntropicRiftEntity markForDissipation() {
		shouldDissipate = true;
		return this;
	}

	@Override
	public boolean shouldRender(@Nullable Player player) {
		return player == null || player.equals(target);
	}

	@Override
	public void tick() {
		super.tick();
		if (shouldDissipate && (target == null || target.getData(dataAttachments.INSANITY).getParanoia() < 450) || tickCount > 20 * 30) {
			discard();
			return;
		}

		if (!(level() instanceof ServerLevel serverLevel))
			return;

		tryCloseRift(
			serverLevel,
			items.materialItems().ASTRAL_CRYSTAL.get(),
			items.materialItems().ENTROPIC_CHAIN.get(),
			() -> serverLevel.getPlayers(player -> !player.isSpectator() && player.position().closerThan(position(), 16.0) && player.isAlive())
				.forEach(player -> {
					advancementTriggers.ENTROPIC_RIFT_CLOSE_TRIGGER.get().trigger(player);
					player.getData(dataAttachments.INSANITY).setParanoia(0);
				})
		);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putBoolean("shouldDissipate", shouldDissipate);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		shouldDissipate = valueInput.getBooleanOr("shouldDissipate", true);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		buffer.writeVarInt(target == null ? -1 : target.getId());
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
		if (level().getEntity(additionalData.readVarInt()) instanceof Player player)
			target = player;
	}
}
