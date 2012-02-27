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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import net.matmas.extensionmanager.ExtensionManager;
import net.matmas.extensionmanager.annotations.InitializeAfter;
import net.matmas.mainframe.MainFrame;
import net.matmas.mainmenu.MenuItemExtensionPoint;
import net.matmas.mainmenu.Quit;
import net.matmas.toolbar.ToolBarItemExtensionPoint;
import net.matmas.tools.GraphicsTools;

/**
 *
 * @author matmas
 */
@InitializeAfter({Quit.class})
public class About implements MenuItemExtensionPoint, ToolBarItemExtensionPoint {

	private Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showOptionDialog(
				ExtensionManager.getInstanceOf(MainFrame.class),
				"demo editor" + "\n\n" +
				"Copyright (c) 2010-2012 by Martin Riesz. All rights reserved.\n" +
				"For more information, visit https://github.com/matmas/java-plugin-system\n\n" +
				"Warning: This computer program is protected by copyright law\n" +
				"and international treaties. Unauthorized reproduction or distribution\n" +
				"may result in severe civil and criminal penalties, and will be\n" +
				"prosecuted to the maximum extend possible under the law.",
				"About",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				new String[] {"OK"},
				"OK");
		}
	};

	public About() {
		String name = "About...";
		action.putValue(Action.NAME, name);
		action.putValue(Action.SMALL_ICON, GraphicsTools.getIcon("About16.gif"));
		action.putValue(Action.SHORT_DESCRIPTION, name);
	}

	public String getMenuPath() {
		return "Help";
	}

	public Action getAction() {
		return action;
	}

	public boolean placeLeftSeparator() {
		return true;
	}
}
