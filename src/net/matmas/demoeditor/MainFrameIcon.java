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

package net.matmas.demoeditor;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.ExtensionManager;
import net.matmas.mainframe.MainFrame;
import net.matmas.tools.GraphicsTools;

/**
 *
 * @author matmas
 */
public class MainFrameIcon implements BasicExtensionPoint {

	public MainFrameIcon() {
		MainFrame mainFrame = ExtensionManager.getInstanceOf(MainFrame.class);
		List<Image> icons = new LinkedList<Image>();
		icons.add(GraphicsTools.getBufferedImage("icon16.png"));
		icons.add(GraphicsTools.getBufferedImage("icon32.png"));
		icons.add(GraphicsTools.getBufferedImage("icon48.png"));
		mainFrame.setIconImages(icons);
	}

}
