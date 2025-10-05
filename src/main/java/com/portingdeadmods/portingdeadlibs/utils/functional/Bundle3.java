package com.portingdeadmods.portingdeadlibs.utils.functional;

public record Bundle3<T1, T2, T3>(T1 v1, T2 v2, T3 v3) implements Bundle {

	public Bundle3() { this(null, null, null); }

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3> Bundle3<T1, T2, T3> populate(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle3<>((T1) value, null, null);
			case 2 -> new Bundle3<>(null, (T2) value, null);
			case 3 -> new Bundle3<>(null, null, (T3) value);
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public Object get(int n) {
		return switch (n) {
			case 1 -> v1;
			case 2 -> v2;
			case 3 -> v3;
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public boolean has(int n) {
		return switch (n) {
			case 1 -> v1 != null;
			case 2 -> v2 != null;
			case 3 -> v3 != null;
			default -> false;
		};
	}

	@SuppressWarnings("unchecked")
	public Bundle3<T1, T2, T3> with(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle3<>((T1) value, v2, v3);
			case 2 -> new Bundle3<>(v1, (T2) value, v3);
			case 3 -> new Bundle3<>(v1, v2, (T3) value);
			default -> this;
		};
	}
}

