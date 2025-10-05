package com.portingdeadmods.portingdeadlibs.utils.functional;

public final class BundleFactory {
	private BundleFactory() {}

	public static Bundle create(Object value, int n, int size) {
		return switch (size) {
			case 2 -> Bundle2.populate(value, n);
			case 3 -> Bundle3.populate(value, n);
			case 4 -> Bundle4.populate(value, n);
			case 5 -> Bundle5.populate(value, n);
			case 6 -> Bundle6.populate(value, n);
			case 7 -> Bundle7.populate(value, n);
			case 8 -> Bundle8.populate(value, n);
			default -> throw new IllegalArgumentException("Bundle size must be 2..8");
		};
	}
}
