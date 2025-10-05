package com.portingdeadmods.portingdeadlibs.utils.functional;

public record Bundle2<T1, T2>(T1 v1, T2 v2) implements Bundle {

	public Bundle2() { this(null, null); }

	@SuppressWarnings("unchecked")
	public static <T1, T2> Bundle2<T1, T2> populate(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle2<>((T1) value, null);
			case 2 -> new Bundle2<>(null, (T2) value);
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public Object get(int n) {
		return switch (n) {
			case 1 -> v1;
			case 2 -> v2;
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public boolean has(int n) {
		return switch (n) {
			case 1 -> v1 != null;
			case 2 -> v2 != null;
			default -> false;
		};
	}

	@SuppressWarnings("unchecked")
	public Bundle2<T1, T2> with(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle2<>((T1) value, v2);
			case 2 -> new Bundle2<>(v1, (T2) value);
			default -> this;
		};
	}
}

