package com.house.finder.housefinder.bean.util;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.house.finder.housefinder.bean.Casa;

public class CasaComparator {
	
	public static boolean equals(Casa casa1, Casa casa2) {
		
		return new EqualsBuilder()
				.append(casa1.getTitolo(), casa2.getTitolo())
				.append(casa1.getNumLocali(), casa2.getNumLocali())
				.append(casa1.getMetriQuadri(), casa2.getMetriQuadri())
				.append(casa1.getNumBagni(), casa2.getNumBagni())
				.append(casa1.getPiano(), casa2.getPiano())
				.append(casa1.getAgenzia(), casa2.getAgenzia())
				.isEquals();
	}
}
