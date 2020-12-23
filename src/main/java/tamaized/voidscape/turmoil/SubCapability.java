package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.common.ClientPacketSubCapSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SubCapability {

	@CapabilityInject(ISubCap.class)
	public static final Capability<ISubCap> CAPABILITY = Voidscape.getNull();

	static {
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, (Consumer<AttachCapabilitiesEvent<Entity>>) event -> {
			if (event.getObject() instanceof LivingEntity)
				event.addCapability(AttachedSubCap.ID, new ICapabilitySerializable() {

					private ISubCap instance = CAPABILITY.getDefaultInstance();

					@Nonnull
					@Override
					public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
						return CAPABILITY.orEmpty(cap, LazyOptional.of(() -> instance));
					}

					@Override
					public INBT serializeNBT() {
						INBT tag = CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
						return tag == null ? new CompoundNBT() : tag;
					}

					@Override
					public void deserializeNBT(INBT nbt) {
						CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
					}
				});
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<LivingEvent.LivingUpdateEvent>) event ->

				event.getEntity().getCapability(SubCapability.CAPABILITY).ifPresent(cap ->

						Arrays.stream(cap.tickers()).forEach(t ->

								t.tick(event.getEntity())))

		);
		MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerEvent.Clone>) event -> event.getPlayer().getCapability(CAPABILITY).ifPresent(cap -> event.getOriginal().getCapability(CAPABILITY).ifPresent(o -> cap.clone(o, event.isWasDeath()))));
	}

	public interface ISubCap {

		<C extends ISubCapData> Optional<C> get(SubCapKey<C> key);

		ISubCapData.ITickHandler[] tickers();

		ISubCapData.IStorageHandler[] storage();

		void clone(ISubCap old, boolean death);

		Optional<ISubCapData.INetworkHandler> network(ResourceLocation id);

		interface ISubCapData {

			void clone(ISubCapData old, boolean death);

			interface ITickHandler extends ISubCapData {

				void tick(Entity parent);

			}

			interface IHasID extends ISubCapData {

				ResourceLocation id();

			}

			interface IStorageHandler extends IHasID {

				CompoundNBT write(CompoundNBT nbt, @Nullable Direction side);

				void read(CompoundNBT nbt, @Nullable Direction side);

			}

			interface INetworkHandler extends IHasID {

				void write(PacketBuffer buffer);

				void read(PacketBuffer buffer);

				default boolean handle(LogicalSide side) {
					return side == LogicalSide.CLIENT;
				}

				default void sendToClient(ServerPlayerEntity parent) {
					Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> parent), new ClientPacketSubCapSync(this));
				}

				default void sendToClients(Entity parent) {
					Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> parent), new ClientPacketSubCapSync(this));
				}

			}

			interface All extends ITickHandler, IStorageHandler, INetworkHandler {

			}

		}

		interface Storage extends Capability.IStorage<ISubCap> {

			@Nullable
			@Override
			default INBT writeNBT(Capability<ISubCap> capability, ISubCap instance, @Nullable Direction side) {
				CompoundNBT nbt = new CompoundNBT();
				Arrays.stream(instance.storage()).forEach(h -> nbt.put(h.id().toString(), h.write(new CompoundNBT(), side)));
				return nbt;
			}

			@Override
			default void readNBT(Capability<ISubCap> capability, ISubCap instance, @Nullable Direction side, INBT nbt) {
				if (nbt.getId() == Constants.NBT.TAG_COMPOUND)
					Arrays.stream(instance.storage()).forEach(h -> h.read(((CompoundNBT) nbt).getCompound(h.id().toString()), side));
			}
		}

		class SubCapKey<C extends ISubCapData> {
			private final Class<C> cast;
			private final Supplier<C> factory;

			public SubCapKey(Class<C> cast, Supplier<C> factory) {
				this.cast = cast;
				this.factory = factory;
			}
		}

	}

	public static final class AttachedSubCap implements ISubCap {

		public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "data");

		private static final List<SubCapKey<?>> REGISTRY = new ArrayList<>();
		private final Map<SubCapKey<?>, ISubCapData> instances = new HashMap<>();

		{
			REGISTRY.forEach(key -> instances.putIfAbsent(key, key.factory.get()));
		}

		public static <C extends ISubCapData> SubCapKey<C> register(Class<C> cast, Supplier<C> factory) {
			SubCapKey<C> key = new SubCapKey<>(cast, factory);
			REGISTRY.add(key);
			return key;
		}

		@Override
		public <C extends ISubCapData> Optional<C> get(SubCapKey<C> key) {
			ISubCapData o = instances.get(key);
			return key.cast.equals(o.getClass()) ? Optional.of(key.cast.cast(o)) : Optional.empty();
		}

		@Override
		public ISubCapData.ITickHandler[] tickers() {
			return instances.values().stream().filter(ISubCapData.ITickHandler.class::isInstance).map(ISubCapData.ITickHandler.class::cast).toArray(ISubCapData.ITickHandler[]::new);
		}

		@Override
		public ISubCapData.IStorageHandler[] storage() {
			return instances.values().stream().filter(ISubCapData.IStorageHandler.class::isInstance).map(ISubCapData.IStorageHandler.class::cast).toArray(ISubCapData.IStorageHandler[]::new);
		}

		@Override
		public void clone(ISubCap old, boolean death) {
			instances.forEach((k, v) -> old.get(k).ifPresent(o -> v.clone(o, death)));
		}

		@Override
		public Optional<ISubCapData.INetworkHandler> network(ResourceLocation id) {
			return instances.values().stream().
					filter(data -> data instanceof ISubCapData.INetworkHandler).
					map(ISubCapData.INetworkHandler.class::cast).
					filter(data -> data.id().equals(id)).
					findAny();
		}
	}

}
