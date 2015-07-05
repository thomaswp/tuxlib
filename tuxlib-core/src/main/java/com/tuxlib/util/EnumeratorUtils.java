package com.tuxlib.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EnumeratorUtils {
	public static <T, U extends T> Iterable<U> where(final Iterable<T> iterable, final Predicate<T> predicate) {
		return new Iterable<U>() {
			@Override
			public Iterator<U> iterator() {
				return new Iterator<U>() {
					
					U popped;
					Iterator<T> iterator = iterable.iterator();

					@SuppressWarnings("unchecked")
					@Override
					public boolean hasNext() {
						if (popped != null) return true;
						while (iterator.hasNext()) {
							T item = iterator.next();
							if (predicate.isTrue(item)) {
								popped = (U) item;
								return true;
							}
						}
						return false;
					}

					@Override
					public U next() {
						if (!hasNext()) throw new NoSuchElementException();
						U item = popped;
						popped = null;
						return item;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
	
	public static <T, U> Iterable<U> select(final Iterable<T> iterable, final Selector<T,U> selector) {
		return new Iterable<U>() {
			@Override
			public Iterator<U> iterator() {
				return new Iterator<U>() {

					Iterator<T> iterator = iterable.iterator();
					
					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public U next() {
						return selector.select(iterator.next());
					}

					@Override
					public void remove() {
						iterator.remove();
					}
					
				};
			}
		};
	}
	
	public static <T> boolean any(Iterable<T> iterable, Predicate<T> predicate) {
		Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			if (predicate.isTrue(iterator.next())) return true;
		}
		return false;
	}
	
	public interface Predicate<T> {
		boolean isTrue(T item);
	}
	
	public interface Selector<T,U> {
		U select(T item);
	}
}
