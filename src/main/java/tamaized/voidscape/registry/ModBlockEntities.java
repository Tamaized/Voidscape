package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.entity.*;
import tamaized.voidscape.registry.block.FunctionalBlocks;
import tamaized.voidscape.registry.block.MachineBlocks;
import tamaized.voidscape.registry.util.BlockEntityTypeFactory;

import java.util.function.Supplier;

@Component
public class ModBlockEntities {

	@Autowired
	private BlockEntityTypeFactory factory;

	@Autowired
	private FunctionalBlocks functionalBlocks;

	@Autowired
	private MachineBlocks machineBlocks;

	private final DeferredRegister<BlockEntityType<?>> REGISTRY = RegUtil.create(Registries.BLOCK_ENTITY_TYPE);

	public final Supplier<BlockEntityType<LiquifierBlockEntity>> LIQUIFIER = REGISTRY.register("liquifier", () -> factory.create(
		LiquifierBlockEntity::new,
		machineBlocks.MACHINE_LIQUIFIER.get()
	));

	public final Supplier<BlockEntityType<DefuserBlockEntity>> DEFUSER = REGISTRY.register("defuser", () -> factory.create(
		DefuserBlockEntity::new,
		machineBlocks.MACHINE_DEFUSER.get()
	));

	public final Supplier<BlockEntityType<GerminatorBlockEntity>> GERMINATOR = REGISTRY.register("germinator", () -> factory.create(
		GerminatorBlockEntity::new,
		machineBlocks.MACHINE_GERMINATOR.get()
	));

	public final Supplier<BlockEntityType<WellBlockEntity>> WELL = REGISTRY.register("well", () -> factory.create(
		WellBlockEntity::new,
		machineBlocks.MACHINE_WELL.get()
	));

	public final Supplier<BlockEntityType<CoopBlockEntity>> COOP = REGISTRY.register("coop", () -> factory.create(
		CoopBlockEntity::new,
		machineBlocks.MACHINE_COOP.get()
	));

	public final Supplier<BlockEntityType<HatcheryBlockEntity>> HATCHERY = REGISTRY.register("hatchery", () -> factory.create(
		HatcheryBlockEntity::new,
		machineBlocks.MACHINE_HATCHERY.get()
	));

	public final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER = REGISTRY.register("infuser", () -> factory.create(
		InfuserBlockEntity::new,
		machineBlocks.MACHINE_INFUSER.get()
	));

	public final Supplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = REGISTRY.register("collector", () -> factory.create(
		CollectorBlockEntity::new,
		machineBlocks.MACHINE_COLLECTOR.get()
	));

	public final Supplier<BlockEntityType<VeryDrippyDripstoneBlockEntity>> VERY_DRIPPY_DRIPSTONE = REGISTRY.register("very_drippy_dripstone", () -> factory.create(
		VeryDrippyDripstoneBlockEntity::new,
		functionalBlocks.VERY_DRIPPY_DRIPSTONE.get()
	));

	@PostConstruct // TODO: add to each block entity class to let them register themselves
	private void init(IEventBus bus) {
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
