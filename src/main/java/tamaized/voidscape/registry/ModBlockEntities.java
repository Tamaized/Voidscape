package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.block.entity.*;

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

	public static final Supplier<BlockEntityType<WellBlockEntity>> WELL = REGISTERY
			.register("well", () -> BlockEntityType.Builder.of(WellBlockEntity::new, ModBlocks.MACHINE_WELL.get()).build(null));

	public static final Supplier<BlockEntityType<CoopBlockEntity>> COOP = REGISTERY
			.register("coop", () -> BlockEntityType.Builder.of(CoopBlockEntity::new, ModBlocks.MACHINE_COOP.get()).build(null));

	public static final Supplier<BlockEntityType<HatcheryBlockEntity>> HATCHERY = REGISTERY
			.register("hatchery", () -> BlockEntityType.Builder.of(HatcheryBlockEntity::new, ModBlocks.MACHINE_HATCHERY.get()).build(null));

	public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER = REGISTERY
			.register("infuser", () -> BlockEntityType.Builder.of(InfuserBlockEntity::new, ModBlocks.MACHINE_INFUSER.get()).build(null));

	public static final Supplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = REGISTERY
			.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, ModBlocks.MACHINE_COLLECTOR.get()).build(null));

	public static final Supplier<BlockEntityType<VeryDrippyDripstoneBlockEntity>> VERY_DRIPPY_DRIPSTONE = REGISTERY
			.register("very_drippy_dripstone", () -> BlockEntityType.Builder.of(VeryDrippyDripstoneBlockEntity::new, ModBlocks.VERY_DRIPPY_DRIPSTONE.get()).build(null));

	@Override
	public void init(IEventBus bus) {
		bus.addListener(RegisterCapabilitiesEvent.class, event -> {
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LIQUIFIER.get(), (object, context) -> object.items);
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, LIQUIFIER.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, DEFUSER.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GERMINATOR.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WELL.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COOP.get(), (object, context) -> object.items);
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COOP.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HATCHERY.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, INFUSER.get(), (object, context) -> object.fluids);

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, COLLECTOR.get(), (object, context) -> object.fluids);
		});
	}

}
