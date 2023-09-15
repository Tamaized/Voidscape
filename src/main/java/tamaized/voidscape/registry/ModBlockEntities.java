package tamaized.voidscape.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.block.entity.BlockEntityDefuser;
import tamaized.voidscape.block.entity.BlockEntityLiquifier;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntities implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	private static final DeferredRegister<BlockEntityType<?>> REGISTERY = RegUtil.create(ForgeRegistries.BLOCK_ENTITY_TYPES);

	public static final RegistryObject<BlockEntityType<BlockEntityLiquifier>> LIQUIFIER = REGISTERY
			.register("liquifier", () -> BlockEntityType.Builder.of(BlockEntityLiquifier::new, ModBlocks.MACHINE_LIQUIFIER.get()).build(null));

	public static final RegistryObject<BlockEntityType<BlockEntityDefuser>> DEFUSER = REGISTERY
			.register("defuser", () -> BlockEntityType.Builder.of(BlockEntityDefuser::new, ModBlocks.MACHINE_DEFUSER.get()).build(null));

}
