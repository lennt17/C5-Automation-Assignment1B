package com.vmo.utils.common;

import java.util.Comparator;

public class Compare implements Comparator<Item> {
	public int compare(Item a, Item b) {
		int aSize = a.getProperty();
		int bSize = b.getProperty();
		if (aSize == bSize) {
			return 0;
		} else {
			return Integer.compare(aSize, bSize);
		}
	}
}