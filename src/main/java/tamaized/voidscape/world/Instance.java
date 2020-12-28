package tamaized.voidscape.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Instance {

	private final World level;
	private int maxPlayers = 1;
	private List<PlayerEntity> players = new ArrayList<>();

	public Instance(World level) {
		this.level = level;
	}

	public Instance maxPlayers(int a) {
		maxPlayers = a;
		return this;
	}

	public Instance snapshot() {
		return this;
	}

	private class EntitySnapshot {
		final EntityType<?> type;
		final CompoundNBT data;

		private EntitySnapshot(Entity entity) {
			type = entity.getType();
			data = entity.serializeNBT();
		}

		@Nullable
		private Entity create() {
			Entity entity = type.create(level);
			if (entity == null)
				return null;
			entity.deserializeNBT(data);
			return entity;
		}
	}

}
