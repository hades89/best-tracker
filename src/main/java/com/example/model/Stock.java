package com.example.model;

import java.io.Serializable;

//@Entity
//@Table(name = "stock")
public class Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7829615959447617950L;

	//@Id
	//@Column
	private int code;

	//@Column
	private String name;

	//@Column(name = "fcs_symbol")
	private String fcsSymbol;

	//@Column(name = "is_pn17")
	private boolean isPN17;

	//@Column(name = "is_gn3")
	private boolean isGN3;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFcsSymbol() {
		return fcsSymbol;
	}

	public void setFcsSymbol(String fcsSymbol) {
		this.fcsSymbol = fcsSymbol;
	}

	public boolean isPN17() {
		return isPN17;
	}

	public void setPN17(boolean isPN17) {
		this.isPN17 = isPN17;
	}

	public boolean isGN3() {
		return isGN3;
	}

	public void setGN3(boolean isGN3) {
		this.isGN3 = isGN3;
	}

}
