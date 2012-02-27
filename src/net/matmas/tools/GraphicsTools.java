/*
 * Copyright (C) 2010-2012 Martin Riesz <riesz.martin at gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.matmas.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author matmas
 */
public class GraphicsTools {

	private final static String resourcesDir = "/resources/";

	public static ImageIcon getIcon(String fileName) {
		return new ImageIcon(getCaller().getResource(resourcesDir + fileName));
	}

	public static Cursor getCursor(String fileName, java.awt.Point center) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.getImage(getCaller().getResource(resourcesDir + fileName));
		return tk.createCustomCursor(image, center, fileName);
	}

	public static BufferedImage getBufferedImage(String fileName) {
		try {
			return ImageIO.read(getCaller().getResource(resourcesDir + fileName));
		} catch (IOException ex) {
			Logger.getLogger(GraphicsTools.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
			return new BufferedImage(1, 1, IndexColorModel.TRANSLUCENT);
		}
	}

	private static Class getCaller() {
		Class caller = null;
		try {
			caller = Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		return caller;
	}

	public static void setDashedStroke(Graphics g) {
		final float dash1[] = {4.0f};
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f);
		((Graphics2D)g).setStroke(dashed);
	}

	public static void setDottedStroke(Graphics g) {
		final float dash1[] = {1.0f, 4.0f};
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f);
		((Graphics2D)g).setStroke(dashed);
	}

	public static void setDefaultStroke(Graphics g) {
		final BasicStroke defaultStroke = new BasicStroke();
		((Graphics2D)g).setStroke(defaultStroke);
	}

	public static void drawImageCentered(Graphics g, BufferedImage image, int x, int y) {
		while (!g.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null)) {}
	}

	public enum HorizontalAlignment {
		left,
		right,
		center
	}

	public enum VerticalAlignment {
		top,
		center,
		bottom
	}

	public static void drawString(Graphics g, String str, Point position,
			HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		int x = position.getX();
		int y = position.getY();
		int textWidth = g.getFontMetrics().stringWidth(str);
		final int textHeight = g.getFontMetrics().getAscent();
		int resultX = x;
		int resultY = y;

		if (horizontalAlignment == HorizontalAlignment.left)
			resultX = x;
		else if (horizontalAlignment == HorizontalAlignment.center)
			resultX = x - textWidth / 2;
		else if (horizontalAlignment == HorizontalAlignment.right)
			resultX = x - textWidth;

		if (verticalAlignment == VerticalAlignment.top)
			resultY = y + textHeight;
		else if (verticalAlignment == VerticalAlignment.center)
			resultY = y + textHeight / 2 - 1;
		else if (verticalAlignment == VerticalAlignment.bottom)
			resultY = y;

		Color previousColor = g.getColor();
		g.setColor(new Color(1f, 1f, 1f, 0.7f));
//		g.setColor(new Color(0.7f, 0.7f, 1f, 0.7f)); //debug with this
		g.fillRect(resultX, resultY - textHeight + 1, textWidth, g.getFontMetrics().getHeight() - 1);
		g.setColor(previousColor);
		g.drawString(str, resultX, resultY);
	}

	//Taken from http://forum.java.sun.com/thread.jspa?threadID=378460&tstart=135
	public static void drawArrow(Graphics g, int xCenter, int yCenter, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;
		double aDir = Math.atan2(xCenter - x, yCenter - y);
		//g2d.drawLine(x, y, xCenter, yCenter);
		g2d.setStroke(new BasicStroke(1f));					// make the arrow head solid even if dash pattern has been specified
		Polygon tmpPoly = new Polygon();
		int i1 = 12;
		int i2 = 6;							// make the arrow head the same size regardless of the length length
		tmpPoly.addPoint(x, y);							// arrow tip
		tmpPoly.addPoint(x + xCor(i1, aDir + 0.5), y + yCor(i1, aDir + 0.5));
		tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
		tmpPoly.addPoint(x + xCor(i1, aDir - 0.5), y + yCor(i1, aDir - 0.5));
		tmpPoly.addPoint(x, y);							// arrow tip
		g2d.drawPolygon(tmpPoly);
		g2d.fillPolygon(tmpPoly);						// remove this line to leave arrow head unpainted
	}
	private static int yCor(int len, double dir) {return (int)(len * Math.cos(dir));}
	private static int xCor(int len, double dir) {return (int)(len * Math.sin(dir));}

	public static boolean isPointNearSegment(Point from, Point to, Point testPos, int nearTolerance) {
		Rectangle r = new Rectangle(testPos.getX() - nearTolerance, testPos.getY() - nearTolerance, 2*nearTolerance, 2*nearTolerance);
		return r.intersectsLine(from.getX(), from.getY(), to.getX(), to.getY());
	}

	public static boolean isPointNearPoint(Point from, Point testPos, int nearTolerance) {
		Rectangle r = new Rectangle(from.getX() - nearTolerance, from.getY() - nearTolerance, 2*nearTolerance+1, 2*nearTolerance+1);
		return r.contains(testPos.getPoint());
	}
}
