package com.portingdeadmods.portingdeadlibs.utils.functional;

public record Bundle5<T1, T2, T3, T4, T5>(
		T1 v1, T2 v2, T3 v3, T4 v4, T5 v5
) implements Bundle {

	public Bundle5() { this(null, null, null, null, null); }

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3, T4, T5> Bundle5<T1, T2, T3, T4, T5> populate(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle5<>((T1) value, null, null, null, null);
			case 2 -> new Bundle5<>(null, (T2) value, null, null, null);
			case 3 -> new Bundle5<>(null, null, (T3) value, null, null);
			case 4 -> new Bundle5<>(null, null, null, (T4) value, null);
			case 5 -> new Bundle5<>(null, null, null, null, (T5) value);
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public Object get(int n) {
		return switch (n) {
			case 1 -> v1;
			case 2 -> v2;
			case 3 -> v3;
			case 4 -> v4;
			case 5 -> v5;
			default -> throw new IllegalArgumentException("Invalid index " + n);
		};
	}

	@Override public boolean has(int n) {
		return switch (n) {
			case 1 -> v1 != null;
			case 2 -> v2 != null;
			case 3 -> v3 != null;
			case 4 -> v4 != null;
			case 5 -> v5 != null;
			default -> false;
		};
	}

	@SuppressWarnings("unchecked")
	public Bundle5<T1, T2, T3, T4, T5> with(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle5<>((T1) value, v2, v3, v4, v5);
			case 2 -> new Bundle5<>(v1, (T2) value, v3, v4, v5);
			case 3 -> new Bundle5<>(v1, v2, (T3) value, v4, v5);
			case 4 -> new Bundle5<>(v1, v2, v3, (T4) value, v5);
			case 5 -> new Bundle5<>(v1, v2, v3, v4, (T5) value);
			default -> this;
		};
	}
}
