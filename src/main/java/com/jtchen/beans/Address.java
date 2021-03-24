package com.jtchen.beans;

import java.util.Objects;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 0:29
 */
public class Address {

	private char x;
	private int y;

	public Address(char x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Address)) return false;
		Address address = (Address) o;
		return x == address.x && y == address.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public char getX() {
		return x;
	}

	public void setX(char x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Address{");
		sb.append("x=").append(x);
		sb.append(", y=").append(y);
		sb.append('}');
		return sb.toString();
	}
}
