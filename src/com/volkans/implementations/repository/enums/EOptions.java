package com.volkans.implementations.repository.enums;

public enum EOptions {
	A("A"),
	B("B"),
	C("C"),
	D("D");	
	
	private String key;

	private EOptions(String key) {
		setKey(key);
	}

	public String getKey() {
		return key;
	}

	private void setKey(String key) {
		this.key = key;
	}
	
}
