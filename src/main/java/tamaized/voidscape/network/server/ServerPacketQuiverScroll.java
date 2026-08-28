package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.QuiverContents;
import tamaized.voidscape.item.QuiverItem;
import tamaized.voidscape.registry.ModItemComponents;

public record ServerPacketQuiverScroll(int menuSlotIndex, int direction) implements CustomPacketPayload {

	public static final Type<ServerPacketQuiverScroll> ID = new Type<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "c2s_quiver_scroll"));

	public static final StreamCodec<FriendlyByteBuf, ServerPacketQuiverScroll> CODEC = StreamCodec.ofMember(ServerPacketQuiverScroll::write, ServerPacketQuiverScroll::new);

	@Autowired
	private static ModItemComponents components;

	private ServerPacketQuiverScroll(FriendlyByteBuf packet) {
		this(packet.readVarInt(), packet.readByte());
	}

	public void write(FriendlyByteBuf packet) {
		packet.writeVarInt(menuSlotIndex());
		packet.writeByte(Integer.signum(direction()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ServerPacketQuiverScroll packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			AbstractContainerMenu menu = player.containerMenu;
			if (packet.menuSlotIndex() < 0 || packet.menuSlotIndex() >= menu.slots.size())
				return;
			Slot slot = menu.slots.get(packet.menuSlotIndex());
			if (!slot.allowModification(player))
				return;
			ItemStack stack = slot.getItem();
			if (!(stack.getItem() instanceof QuiverItem))
				return;
			QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
			if (contents == null)
				return;
			QuiverContents.Mutable mutable = contents.toMutableCopy();
			mutable.rotate(packet.direction());
			stack.set(components.QUIVER_CONTENTS, mutable.toImmutable());
		});
	}
}
