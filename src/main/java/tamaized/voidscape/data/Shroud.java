package tamaized.voidscape.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.network.client.ClientPacketNetworkedAttachmentSync;
import tamaized.voidscape.registry.ModAttributes;

@Configurable
public class Shroud extends NetworkedDataAttachment implements ValueIOSerializable {

	@Autowired
	private ModAttributes attributes;

	private float shields = 0F;
	private int rechargeTime = 0;

	private float prevMaxHp = 0F;

	private boolean clientRefresh = true;

	public Shroud(String id) {
		super(id);
	}

	public void tick(Entity e) {
		if (!(e instanceof LivingEntity parent) || parent.level().isClientSide())
			return;

		if (parent.getMaxHealth() < prevMaxHp) {
			prevMaxHp = parent.getMaxHealth();
			shields = Math.min(shields, calculateShields(parent));
		}

		if (rechargeTime-- <= 0) {
			float prevShields = shields;
			shields = calculateShields(parent);
			if (shields <= 0F)
				shields = 0F;
			else if (shields > prevShields)
				parent.level().playSound(
					null,
					parent,
					SoundEvents.RESPAWN_ANCHOR_CHARGE,
					SoundSource.PLAYERS,
					0.25F,
					0.5F + parent.getRandom().nextFloat() * 1.5F
				);
			resetTimer();
			clientRefresh = true;
		}

		if (clientRefresh) {
			sendToClients(parent);
		} else if (parent.tickCount % (20 * 5) == 0) {
			sendToClients(parent);
		}
	}

	private float calculateShields(LivingEntity parent) {
		return parent.getMaxHealth() * (float) parent.getAttributeValue(attributes.SHROUDED);
	}

	public float onDamage(LivingEntity parent, float amount) {
		if (shields > 0F && amount > 0F) {
			float blocked = Math.min(shields, amount);
			shields -= blocked;
			resetTimer();
			parent.level().playSound(
				null,
				parent,
				SoundEvents.AMETHYST_CLUSTER_BREAK,
				SoundSource.PLAYERS,
				0.5F,
				0.5F + parent.getRandom().nextFloat() * 1.5F
			);
			clientRefresh = true;
			return amount - blocked;
		}

		return amount;
	}

	private void resetTimer() {
		rechargeTime = 20 * 15;
	}

	public float getShields() {
		return shields;
	}

	@Override
	public void serialize(ValueOutput output) {
		output.putFloat("shields", shields);
		output.putInt("rechargeTime", rechargeTime);
	}

	@Override
	public void deserialize(ValueInput input) {
		shields = input.getFloatOr("shields", 0F);
		rechargeTime = input.getIntOr("rechargeTime", 0);
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(shields);
		buffer.writeInt(rechargeTime);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		shields = buffer.readFloat();
		rechargeTime = buffer.readInt();
	}

	private void sendToClients(Entity parent) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(parent, new ClientPacketNetworkedAttachmentSync(this, parent));
	}
}
