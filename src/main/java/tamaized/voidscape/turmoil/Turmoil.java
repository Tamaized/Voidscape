package tamaized.voidscape.turmoil;

public class Turmoil {/*

	public static State STATE = State.CLOSED;

	static {
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, (Consumer<AttachCapabilitiesEvent<Entity>>) event -> {
			if (event.getObject() instanceof LivingEntity)
				event.addCapability(Turmoil.AttachedTurmoilData.ID, new ICapabilityProvider() {
					private LazyOptional<?> instance;

					@Nonnull
					@Override
					public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
						return cap == Turmoil.ITurmoilData.CAPABILITY ?

								instance == null ?

										(instance = LazyOptional.of(() -> Objects.requireNonNull(cap.getDefaultInstance()))).cast() :

										instance.cast() :

								LazyOptional.empty();
					}
				});
		});
		MinecraftForge.EVENT_BUS.addListener(

				(Consumer<LivingEvent.LivingUpdateEvent>) event ->

						event.getEntity().getCapability(ITurmoilData.CAPABILITY).ifPresent(cap ->

								Arrays.stream(cap.tickers()).forEach(t ->

										t.tick(event.getEntity())))

		);
	}

	public enum State {
		CLOSED, OPEN, TELEPORTING
	}

	public interface ITurmoilData {

		@CapabilityInject(ITurmoilData.class)
		Capability<ITurmoilData> CAPABILITY = Voidscape.getNull();

		Optional<ITurmoil> get(ResourceLocation id);

		ITurmoil.ITickHandler[] tickers();

		ITurmoil.IStorageHandler[] storage();

		Optional<ITurmoil.INetworkHandler> network(ResourceLocation id);

		interface ITurmoil {

			ResourceLocation id();

			default <T extends ITurmoil> Optional<T> cast(Class<T> c) {
				return c.isInstance(this) ? Optional.of(c.cast(this)) : Optional.empty();
			}

			interface ITickHandler extends ITurmoil {

				void tick(Entity parent);

			}

			interface IStorageHandler extends ITurmoil {

				CompoundNBT write(CompoundNBT nbt, Direction side);

				void read(CompoundNBT nbt, Direction side);

			}

			interface INetworkHandler extends ITurmoil {

				void write(PacketBuffer buffer);

				void read(PacketBuffer buffer);

				default boolean handle(LogicalSide side) {
					return side == LogicalSide.CLIENT;
				}

				default void sendToServer() {
					Voidscape.NETWORK.sendToServer(new CommonPacketTurmoilData(this));
				}

				default void sendToClients(Entity parent) {
					Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> parent), new CommonPacketTurmoilData(this));
				}

			}

		}

		interface Storage extends Capability.IStorage<ITurmoilData> {

			@Nullable
			@Override
			default INBT writeNBT(Capability<ITurmoilData> capability, ITurmoilData instance, Direction side) {
				CompoundNBT nbt = new CompoundNBT();
				Arrays.stream(instance.storage()).forEach(h -> nbt.put(h.id().toString(), h.write(nbt, side)));
				return nbt;
			}

			@Override
			default void readNBT(Capability<ITurmoilData> capability, ITurmoilData instance, Direction side, INBT nbt) {
				if (nbt.getId() == Constants.NBT.TAG_COMPOUND)
					Arrays.stream(instance.storage()).forEach(h -> h.read(((CompoundNBT) nbt).getCompound(h.id().toString()), side));
			}
		}

	}

	public static final class AttachedTurmoilData implements ITurmoilData {

		public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoil");
		private static final List<Supplier<? extends ITurmoil>> REGISTRY = new ArrayList<>();
		private final Map<ResourceLocation, ITurmoil> instances = new HashMap<>();

		{
			REGISTRY.forEach(s -> {
				ITurmoil inst = s.get();
				if (!instances.containsKey(inst.id()))
					instances.put(inst.id(), inst);
				else
					Voidscape.LOGGER.fatal("Duplicate ID! ({})", inst.id());
			});
		}

		public static void register(Supplier<? extends ITurmoil> factory) {
			REGISTRY.add(factory);
		}

		@Override
		public Optional<ITurmoil> get(ResourceLocation id) {
			return Optional.ofNullable(instances.get(id));
		}

		@Override
		public ITurmoil.ITickHandler[] tickers() {
			return instances.values().stream().filter(ITurmoil.ITickHandler.class::isInstance).map(ITurmoil.ITickHandler.class::cast).toArray(ITurmoil.ITickHandler[]::new);
		}

		@Override
		public ITurmoil.IStorageHandler[] storage() {
			return instances.values().stream().filter(ITurmoil.IStorageHandler.class::isInstance).map(ITurmoil.IStorageHandler.class::cast).toArray(ITurmoil.IStorageHandler[]::new);
		}

		@Override
		public Optional<ITurmoil.INetworkHandler> network(ResourceLocation id) {
			return instances.values().stream().filter(o -> o instanceof ITurmoil.INetworkHandler && o.id().equals(id)).map(ITurmoil.INetworkHandler.class::cast).findAny();
		}
	}*/

}
