package tamaized.voidscape.registry;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
	public static final RegistryObject<Item> ETHEREAL_ESSENCE = REGISTRY.register("ethereal_essence", () -> new Item(RegUtil.ItemProps.VOIDIC_CRYSTAL.get()) {
		@Override
		public ActionResultType useOn(ItemUseContext context) {
			if (Voidscape.checkForVoidDimension(context.getLevel()) && context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.BEDROCK)) {
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
				context.getItemInHand().shrink(1);
				context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1F, 0.5F + context.getLevel().getRandom().nextFloat() * 0.5F);
				if (context.getLevel() instanceof ServerWorld)
					for (int i = 0; i < 50; i++)
						((ServerWorld) context.getLevel()).sendParticles(ParticleTypes.WITCH, context.
								getClickedPos().getX() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getY() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(), 0, 0, 0, 0, 1F);
				return ActionResultType.SUCCESS;
			}
			return super.useOn(context);
		}
	});
	public static final RegistryObject<Item> TENDRIL = REGISTRY.register("tendril", () -> new Item(RegUtil.ItemProps.VOIDIC_CRYSTAL.get()));

	static void classload() {

	}

}
