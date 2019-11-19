package com.house.finder.housefinder.rest.data;

import java.io.Serializable;
import java.util.List;

public class ScartaCasaRequest implements Serializable{
	
	private static final long serialVersionUID = 1595679250158499385L;
	
	public List<ScartaCasa> scarta;

	public List<ScartaCasa> getScarta() {
		return scarta;
	}

	public void setScarta(List<ScartaCasa> scarta) {
		this.scarta = scarta;
	}

}
