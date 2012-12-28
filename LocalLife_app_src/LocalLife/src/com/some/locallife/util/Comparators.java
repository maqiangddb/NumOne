package com.some.locallife.util;

import java.util.Comparator;

import com.some.locallife.data.type.Shop;

public class Comparators {

	private static Comparator<Shop> sShopDistanceComparator = null;

	public static Comparator<Shop> getShopDistanceComparator() {
		if(sShopDistanceComparator == null) {
			sShopDistanceComparator = new Comparator<Shop>() {

				@Override
				public int compare(Shop lhs, Shop rhs) {
					// TODO Auto-generated method stub
					return 0;
				}

			};
		}
		return sShopDistanceComparator;
	}

}
