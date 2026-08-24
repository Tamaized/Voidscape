package tamaized.voidscape.datagen.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record DirectReferenceHolder<T>(ResourceKey<T> _value) implements Holder<T> {

	public static <T> DirectReferenceHolder<T> of(ResourceKey<T> key) {
		return new DirectReferenceHolder<>(key);
	}

	@Override
	public boolean isBound() {
		return false;
	}

	@Override
	public boolean areComponentsBound() {
		return false;
	}

	@Override
	public DataComponentMap components() {
		return DataComponentMap.EMPTY;
	}

	@Override
	public boolean is(Identifier Identifier) {
		return Identifier.equals(_value.identifier());
	}

	@Override
	public boolean is(ResourceKey<T> key) {
		return key.equals(_value);
	}

	@Override
	public boolean is(TagKey<T> p_205719_) {
		return false;
	}

	@Deprecated
	@Override
	public boolean is(Holder<T> p_316277_) {
		return false;
	}

	@Override
	public boolean is(Predicate<ResourceKey<T>> predicate) {
		return predicate.test(_value);
	}

	@Override
	public Either<ResourceKey<T>, T> unwrap() {
		return Either.left(this._value);
	}

	@Override
	public Optional<ResourceKey<T>> unwrapKey() {
		return Optional.of(_value);
	}

	@Override
	public T value() {
		throw new NullPointerException("Trying to access unbound value: " + this._value);
	}

	@Override
	public Kind kind() {
		return Kind.REFERENCE;
	}

	public String toString() {
		return "DirectReference{" + this._value + "}";
	}

	@Override
	public boolean canSerializeIn(HolderOwner<T> p_256328_) {
		return true;
	}

	@Override
	public Stream<TagKey<T>> tags() {
		return Stream.of();
	}

}
