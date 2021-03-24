package com.jtchen.beans;

import java.util.Objects;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/23 22:21
 */
public class Chess {
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	private Address address;
	private int color;


	public Chess(char xaxis, int yaxis, int color) {
		address = new Address(xaxis, yaxis);
		this.color = color;
	}
	public Chess(Address address, int color) {
		this.address = address;
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Chess)) return false;
		Chess chess = (Chess) o;
		return color == chess.color && address.equals(chess.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, color);
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Chess{");
		sb.append("address=").append(address);
		sb.append(", color=").append(color == 0 ? "black" : "white");
		sb.append('}');
		return sb.toString();
	}
}
