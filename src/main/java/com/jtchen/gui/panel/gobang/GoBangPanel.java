package com.jtchen.gui.panel.gobang;

import com.jtchen.beans.Address;
import com.jtchen.beans.Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static com.jtchen.gui.GoBangFrame.DEFAULT_HEIGHT;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/23 19:38
 */
public class GoBangPanel extends JPanel {
	private static final int size = DEFAULT_HEIGHT; // panel大小
	private static final int BOARD_WEIGTH = size - 40;
	private static final int BOARD_HEIGHT = size - 80;
	private static final double LINE_SPACING = (double) BOARD_HEIGHT / 14;
	private static final double ROW_SPACING = (double) BOARD_WEIGTH / 14;
	private int mouseX = 0;
	private int mouseY = 0;
	private final List<Chess> chessList = new ArrayList<>();


	public GoBangPanel() {
		defaultSetting();
		addKeyListener();
	}

	// 添加棋子
	public void addChess(Chess chess) {
		chessList.add(chess);
		repaint();
	}

	// 删除棋子
	public void removeChess(Chess chess) {
		chessList.remove(chess);
		repaint();
	}

	// 删除棋子
	public void removeAllChess() {
		chessList.clear();
		repaint();
	}

	// 添加监听事件
	private void addKeyListener() {
		addMouseMotionListener(new DrawPreselectionBoxListener());
	}

	// 默认设置
	private void defaultSetting() {
		setBackground(Color.orange);
		setPreferredSize(new Dimension(size, size));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		// 画出边框
		paintBorder(g2d);
		// 画出棋盘
		paintLine(g2d);
		// 画几个重要的点
		drawPoint(g2d);
		// 画预选框
		drawPreselectionBox(g2d);
		// 画出棋子
		drawChess(g2d);
	}

	// 画出边框
	private void paintBorder(Graphics2D g2d) {
		Rectangle2D.Double rect = new Rectangle2D.Double(20, 20, BOARD_WEIGTH, BOARD_HEIGHT);
		Stroke stroke = g2d.getStroke();

		g2d.setStroke(new BasicStroke(2.5f));
		g2d.draw(rect);

		g2d.setStroke(stroke);
	}

	// 画出棋盘的标记点
	private void drawPoint(Graphics2D g2d) {
		drawPoint(g2d, 8, 'd', 4, Color.black);
		drawPoint(g2d, 8, 'l', 4, Color.black);
		drawPoint(g2d, 8, 'l', 12, Color.black);
		drawPoint(g2d, 8, 'h', 8, Color.black);
		drawPoint(g2d, 8, 'd', 12, Color.black);
	}

	/**
	 * @param g2d    画板
	 * @param radius 点之半径
	 * @param ch     横坐标
	 * @param i      纵坐标
	 * @param color  点之颜色
	 */
	private void drawPoint(Graphics2D g2d, double radius, char ch, int i, Color color) {
		Color color1 = g2d.getColor();

		Point2D point = parseXY(ch, i);
		Ellipse2D.Double rect = new Ellipse2D.Double(point.getX() - radius / 2, point.getY() - radius / 2, radius, radius);
		g2d.draw(rect);
		g2d.setColor(color);
		g2d.fill(rect);

		g2d.setColor(color1);
	}

	private void drawChess(Graphics2D g2d) {
		for (Chess chess : chessList) {
			drawChess(g2d, chess);
		}
	}

	private void drawChess(Graphics2D g2d, Chess chess) {
		Color color = chess.getColor() == Chess.WHITE ? Color.white : Color.black;
		Address address = chess.getAddress();
		drawPoint(g2d, 30, address.getX(), address.getY(), color);
	}

	private void paintLine(Graphics2D g2d) {
		// 画出行, 列
		Line2D.Double line;
		for (int i = 1; i <= 14; i++) {
			line = new Line2D.Double(20, 20 + i * LINE_SPACING,
					20 + BOARD_WEIGTH, 20 + i * LINE_SPACING);
			g2d.draw(line);
			line = new Line2D.Double(20 + i * ROW_SPACING, 20,
					20 + i * ROW_SPACING, 20 + BOARD_HEIGHT);
			g2d.draw(line);
		}
	}

	// 抽象坐标转化为实际坐标
	private Point2D parseXY(char ch, int i) {
		double x = (ch - 'a') * ROW_SPACING + 20;
		double y = (i - 1) * LINE_SPACING;
		y = BOARD_HEIGHT - y + 20;

		return new GoBangPoint(x, y);
	}

	// 画预选框
	private void drawPreselectionBox(Graphics2D g2d) {
		Address mouseXY = parseMouseXY(mouseX, mouseY);
		char ch = mouseXY.getX();
		int i = mouseXY.getY();

		Color color = g2d.getColor();
		Stroke stroke = g2d.getStroke();

		Point2D point2D = parseXY(ch, i);
		double x = point2D.getX();
		double y = point2D.getY();

		Rectangle2D.Double rect = new Rectangle2D.Double(x - ROW_SPACING / 2,
				y - LINE_SPACING / 2, ROW_SPACING, LINE_SPACING);

		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 1.0f, new float[]{10, 10}, 1));
		g2d.setColor(Color.red);
		g2d.draw(rect);

		g2d.setStroke(stroke);
		g2d.setColor(color);
	}

	public Address parseMouseXY(int x, int y) {
		char ch = (char) ('a' + (x - 7) / ROW_SPACING);
		int i = (int) ((BOARD_HEIGHT - (y - 30)) / LINE_SPACING) + 1;

		return new Address(ch, i);
	}


	private static class GoBangPoint extends Point2D {
		private double x;
		private double y;

		public GoBangPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public double getX() {
			return x;
		}

		@Override
		public double getY() {
			return y;
		}

		@Override
		public void setLocation(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	private class DrawPreselectionBoxListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			mouseX = x;
			mouseY = y;

			repaint();
		}
	}

}
