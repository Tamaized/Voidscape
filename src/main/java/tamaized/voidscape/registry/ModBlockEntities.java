package tamaized.voidscape.registry;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.block.entity.DefuserBlockEntity;
import tamaized.voidscape.block.entity.GerminatorBlockEntity;
import tamaized.voidscape.block.entity.InfuserBlockEntity;
import tamaized.voidscape.block.entity.LiquifierBlockEntity;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntities implements RegistryClass {

	private static final DeferredRegister<BlockEntityType<?>> REGISTERY = RegUtil.create(Registries.BLOCK_ENTITY_TYPE);

	public static final Supplier<BlockEntityType<LiquifierBlockEntity>> LIQUIFIER = REGISTERY
			.register("liquifier", () -> BlockEntityType.Builder.of(LiquifierBlockEntity::new, ModBlocks.MACHINE_LIQUIFIER.get()).build(null));

	public static final Supplier<BlockEntityType<DefuserBlockEntity>> DEFUSER = REGISTERY
			.register("defuser", () -> BlockEntityType.Builder.of(DefuserBlockEntity::new, ModBlocks.MACHINE_DEFUSER.get()).build(null));

	public static final Supplier<BlockEntityType<GerminatorBlockEntity>> GERMINATOR = REGISTERY
			.register("germinator", () -> BlockEntityType.Builder.of(GerminatorBlockEntity::new, ModBlocks.MACHINE_GERMINATOR.get()).build(null));

	public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER = REGISTERY
			.register("infuser", () -> BlockEntityType.Builder.of(InfuserBlockEntity::new, ModBlocks.MACHINE_INFUSER.get()).build(null));

	@Override
	public void init(IEventBus bus) {
		bus.addListener(RegisterCapabilitiesEvent.class, event -> {
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LIQUIFIER.get(), (object, context) -> object.items);
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, LIQUIFIER.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, DEFUSER.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GERMINATOR.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, INFUSER.get(), (object, context) -> object.fluids);
		});
	}

}
