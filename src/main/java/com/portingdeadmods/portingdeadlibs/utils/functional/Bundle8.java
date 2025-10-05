package com.portingdeadmods.portingdeadlibs.utils.functional;

public record Bundle8<T1, T2, T3, T4, T5, T6, T7, T8>(
		T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
) implements Bundle {

	public Bundle8() { this(null, null, null, null, null, null, null, null); }

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Bundle8<T1, T2, T3, T4, T5, T6, T7, T8> populate(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle8<>((T1) value, null, null, null, null, null, null, null);
			case 2 -> new Bundle8<>(null, (T2) value, null, null, null, null, null, null);
			case 3 -> new Bundle8<>(null, null, (T3) value, null, null, null, null, null);
			case 4 -> new Bundle8<>(null, null, null, (T4) value, null, null, null, null);
			case 5 -> new Bundle8<>(null, null, null, null, (T5) value, null, null, null);
			case 6 -> new Bundle8<>(null, null, null, null, null, (T6) value, null, null);
			case 7 -> new Bundle8<>(null, null, null, null, null, null, (T7) value, null);
			case 8 -> new Bundle8<>(null, null, null, null, null, null, null, (T8) value);
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
			case 6 -> v6;
			case 7 -> v7;
			case 8 -> v8;
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
			case 6 -> v6 != null;
			case 7 -> v7 != null;
			case 8 -> v8 != null;
			default -> false;
		};
	}

	@SuppressWarnings("unchecked")
	public Bundle8<T1, T2, T3, T4, T5, T6, T7, T8> with(Object value, int n) {
		return switch (n) {
			case 1 -> new Bundle8<>((T1) value, v2, v3, v4, v5, v6, v7, v8);
			case 2 -> new Bundle8<>(v1, (T2) value, v3, v4, v5, v6, v7, v8);
			case 3 -> new Bundle8<>(v1, v2, (T3) value, v4, v5, v6, v7, v8);
			case 4 -> new Bundle8<>(v1, v2, v3, (T4) value, v5, v6, v7, v8);
			case 5 -> new Bundle8<>(v1, v2, v3, v4, (T5) value, v6, v7, v8);
			case 6 -> new Bundle8<>(v1, v2, v3, v4, v5, (T6) value, v7, v8);
			case 7 -> new Bundle8<>(v1, v2, v3, v4, v5, v6, (T7) value, v8);
			case 8 -> new Bundle8<>(v1, v2, v3, v4, v5, v6, v7, (T8) value);
			default -> this;
		};
	}
}

