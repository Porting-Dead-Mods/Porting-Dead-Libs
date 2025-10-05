package com.portingdeadmods.portingdeadlibs.utils.functional;

public sealed interface Bundle permits
		Bundle2, Bundle3, Bundle4, Bundle5, Bundle6, Bundle7, Bundle8 {

	Object get(int n);
	boolean has(int n);
}
