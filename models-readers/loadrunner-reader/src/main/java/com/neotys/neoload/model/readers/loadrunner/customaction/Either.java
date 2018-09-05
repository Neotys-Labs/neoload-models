package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.base.Objects;

public class Either<L, R> {
	
	private final Optional<L> left;
	private final Optional<R> right;
	
	private Either(Optional<L> l, Optional<R> r) {
		left = l;
		right = r;
	}
	
	public static <L, R> Either<L, R> left(L value) {
		return new Either<>(Optional.of(value), Optional.empty());
	}

	public static <L, R> Either<L, R> right(R value) {
		return new Either<>(Optional.empty(), Optional.of(value));
	}	

	public <T> T map(
			Function<? super L, ? extends T> lFunc,
			Function<? super R, ? extends T> rFunc) {
		return left.<T> map(lFunc).orElseGet(() -> right.map(rFunc).get());
	}

	public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc) {
		return new Either<>(left.map(lFunc), right);
	}

	public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
		return new Either<>(left, right.map(rFunc));
	}

	public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
		left.ifPresent(lFunc);
		right.ifPresent(rFunc);
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Either)) {
	      return false;
	    }
	    Either<?, ?> other = (Either<?, ?>) o;
	    return Objects.equal(left, other.left)
	        && Objects.equal(right, other.right);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hashCode(left, right);
	  }
}