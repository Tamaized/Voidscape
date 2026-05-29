package tamaized.voidscape.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.voidscape.util.UnsafeUtil;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredLazyHolder<R, T extends R> extends DeferredHolder<R, T> {

	@Autowired
	private static UnsafeUtil unsafeUtil;

	@SuppressWarnings("unchecked")
	public static <R, T extends R> DeferredLazyHolder<R, T> create(Supplier<DeferredHolder<R, T>> deferred) {
		DeferredLazyHolder<R, T> lazy = unsafeUtil.newInstance(DeferredLazyHolder.class);
		lazy.delegate = deferred;
		return lazy;
	}

	private Supplier<DeferredHolder<R, T>> delegate;

	@SuppressWarnings("DataFlowIssue")
	private DeferredLazyHolder() {
		super(null);
	}

	@Override
	public T value() {
		return delegate.get().value();
	}

	@Override
	public Identifier getId() {
		return delegate.get().getId();
	}

	@Override
	public ResourceKey<R> getKey() {
		return delegate.get().getKey();
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) {
		return delegate.get().equals(obj);
	}

	@Override
	public int hashCode() {
		return delegate.get().hashCode();
	}

	@Override
	public String toString() {
		return delegate.get().toString();
	}

	@Override
	public boolean isBound() {
		return delegate.get().isBound();
	}

	@Override
	public boolean is(Identifier id) {
		return delegate.get().is(id);
	}

	@Override
	public boolean is(ResourceKey<R> key) {
		return delegate.get().is(key);
	}

	@Override
	public boolean is(Predicate<ResourceKey<R>> filter) {
		return delegate.get().is(filter);
	}

	@Override
	public boolean is(TagKey<R> tag) {
		return delegate.get().is(tag);
	}

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public boolean is(Holder<R> holder) {
		return delegate.get().is(holder);
	}

	@Override
	public <Z> @Nullable Z getData(DataMapType<R, Z> type) {
		return delegate.get().getData(type);
	}

	@Override
	public Stream<TagKey<R>> tags() {
		return delegate.get().tags();
	}

	@Override
	public Either<ResourceKey<R>, R> unwrap() {
		return delegate.get().unwrap();
	}

	@Override
	public Optional<ResourceKey<R>> unwrapKey() {
		return delegate.get().unwrapKey();
	}

	@Override
	public boolean canSerializeIn(HolderOwner<R> owner) {
		return delegate.get().canSerializeIn(owner);
	}

	@Override
	public Holder<R> getDelegate() {
		return delegate.get().getDelegate();
	}
}
