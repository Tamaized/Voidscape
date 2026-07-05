package tamaized.voidscape.registry.blockentity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.entity.*;
import tamaized.voidscape.registry.block.FunctionalBlocks;
import tamaized.voidscape.registry.block.MachineBlocks;

import java.util.function.Supplier;

@Component
public class ModBlockEntities {

	@Autowired
	private BlockEntityTypeFactory factory;

	@Autowired
	private FunctionalBlocks functionalBlocks;

	@Autowired
	private MachineBlocks machineBlocks;

	public final Supplier<BlockEntityType<LiquifierBlockEntity>> LIQUIFIER = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "liquifier", () -> factory.create(
		LiquifierBlockEntity::new,
		machineBlocks.MACHINE_LIQUIFIER.get()
	));

	public final Supplier<BlockEntityType<DefuserBlockEntity>> DEFUSER = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "defuser", () -> factory.create(
		DefuserBlockEntity::new,
		machineBlocks.MACHINE_DEFUSER.get()
	));

	public final Supplier<BlockEntityType<GerminatorBlockEntity>> GERMINATOR = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "germinator", () -> factory.create(
		GerminatorBlockEntity::new,
		machineBlocks.MACHINE_GERMINATOR.get()
	));

	public final Supplier<BlockEntityType<WellBlockEntity>> WELL = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "well", () -> factory.create(
		WellBlockEntity::new,
		machineBlocks.MACHINE_WELL.get()
	));

	public final Supplier<BlockEntityType<CoopBlockEntity>> COOP = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "coop", () -> factory.create(
		CoopBlockEntity::new,
		machineBlocks.MACHINE_COOP.get()
	));

	public final Supplier<BlockEntityType<HatcheryBlockEntity>> HATCHERY = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "hatchery", () -> factory.create(
		HatcheryBlockEntity::new,
		machineBlocks.MACHINE_HATCHERY.get()
	));

	public final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "infuser", () -> factory.create(
		InfuserBlockEntity::new,
		machineBlocks.MACHINE_INFUSER.get()
	));

	public final Supplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "collector", () -> factory.create(
		CollectorBlockEntity::new,
		machineBlocks.MACHINE_COLLECTOR.get()
	));

	public final Supplier<BlockEntityType<VeryDrippyDripstoneBlockEntity>> VERY_DRIPPY_DRIPSTONE = RegUtil.register(Registries.BLOCK_ENTITY_TYPE, "very_drippy_dripstone", () -> factory.create(
		VeryDrippyDripstoneBlockEntity::new,
		functionalBlocks.VERY_DRIPPY_DRIPSTONE.get()
	));

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(RegisterCapabilitiesEvent.class, event -> {
			LiquifierBlockEntity.registerCaps(event);

			DefuserBlockEntity.registerCaps(event);

			GerminatorBlockEntity.registerCaps(event);

			WellBlockEntity.registerCaps(event);

			CoopBlockEntity.registerCaps(event);

			HatcheryBlockEntity.registerCaps(event);

			InfuserBlockEntity.registerCaps(event);

			CollectorBlockEntity.registerCaps(event);
		});
	}

}
