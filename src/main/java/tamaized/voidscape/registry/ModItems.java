package tamaized.voidscape.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketTurmoilResetSkills;

import java.util.UUID;

public class ModItems {

	static final DeferredRegister<Item> REGISTRY = RegUtil.create(ForgeRegistries.ITEMS);
	public static final RegistryObject<Item> VOIDIC_CRYSTAL = REGISTRY.register("voidic_crystal", () -> new Item(RegUtil.ItemProps.VOIDIC_CRYSTAL.get()) {
		@Override
		public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
			if (playerIn.getGameProfile().getId().equals(UUID.fromString("16fea09e-314e-4955-88c2-6b552ecf314a"))) {
				if (worldIn.isClientSide())
					Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilResetSkills());
				return ActionResult.success(playerIn.getItemInHand(handIn));
			}
			return super.use(worldIn, playerIn, handIn);
		}
	});

	static void classload() {

	}

}
