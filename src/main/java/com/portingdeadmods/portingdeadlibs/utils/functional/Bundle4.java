package com.portingdeadmods.portingdeadlibs.utils.functional;

public record Bundle4<T1, T2, T3, T4>(
		T1 v1, T2 v2, T3 v3, T4 v4
) implements Bundle {

	public Bundle4() { this(null, null, null, null); }

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3, T4> Bundle4<T1, T2, T3, T4> populate(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle4<>((T1) value, null, null, null);
			case 2 -> new Bundle4<>(null, (T2) value, null, null);
			case 3 -> new Bundle4<>(null, null, (T3) value, null);
			case 4 -> new Bundle4<>(null, null, null, (T4) value);
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public Object get(int n) {
		return switch (n) {
			case 1 -> v1;
			case 2 -> v2;
			case 3 -> v3;
			case 4 -> v4;
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public boolean has(int n) {
		return switch (n) {
			case 1 -> v1 != null;
			case 2 -> v2 != null;
			case 3 -> v3 != null;
			case 4 -> v4 != null;
			default -> false;
		};
	}

	@SuppressWarnings("unchecked")
	public Bundle4<T1, T2, T3, T4> with(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle4<>((T1) value, v2, v3, v4);
			case 2 -> new Bundle4<>(v1, (T2) value, v3, v4);
			case 3 -> new Bundle4<>(v1, v2, (T3) value, v4);
			case 4 -> new Bundle4<>(v1, v2, v3, (T4) value);
			default -> this;
		};
	}
}
