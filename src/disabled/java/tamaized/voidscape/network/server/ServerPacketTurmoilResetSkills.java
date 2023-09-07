package tamaized.voidscape.network.server;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ServerPacketTurmoilResetSkills implements NetworkMessages.IMessage<ServerPacketTurmoilResetSkills> {

	public static final LazyLoadedValue<ItemStack> VOIDIC_CRYSTAL = new LazyLoadedValue<>(() -> new ItemStack(ModItems.VOIDIC_CRYSTAL.get()));

	@Override
	public void handle(@Nullable Player player) {
		if (player != null && player.inventory.contains(VOIDIC_CRYSTAL.get()))
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.getResetCooldown() <= 0)
					cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
						data.resetSkills(stats);
						player.inventory.clearOrCountMatchingItems(stack -> stack.sameItem(VOIDIC_CRYSTAL.get()), 1, player.inventoryMenu.getCraftSlots());
						player.level.playSound(null, player, SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1F, 0.75F + player.getRandom().nextFloat() * 0.5F);
						if (player.level instanceof ServerLevel)
							for (int i = 0; i < 50; i++) {
								Vec3 pos = new Vec3(0.25F + player.getRandom().nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(player.getRandom().nextInt(360))).add(player.getX(), player.getEyeY() - 0.5F + player.getRandom().nextFloat(), player.getZ());
								((ServerLevel) player.level).sendParticles(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
							}
					});
			}));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {

	}

	@Override
	public ServerPacketTurmoilResetSkills fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
