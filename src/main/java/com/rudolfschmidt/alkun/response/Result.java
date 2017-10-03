package com.rudolfschmidt.alkun.response;

public class Result {

	private final boolean next;

	Result() {
		this.next = false;
	}

	Result(boolean next) {
		this.next = next;
	}

	public boolean isNext() {
		return next;
	}

}
