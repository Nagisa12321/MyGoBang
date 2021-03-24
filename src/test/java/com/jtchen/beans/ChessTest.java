package com.jtchen.beans;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChessTest {

	@Test
	public void chessEqualTest() {

		Chess chess1 = new Chess('a', 1, Chess.BLACK);
		Chess chess2 = new Chess(new Address('a', 1), Chess.WHITE);

		System.out.println(chess1.equals(chess2));

	}

}