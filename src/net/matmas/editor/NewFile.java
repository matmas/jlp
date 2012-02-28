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

package net.matmas.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.matmas.extensionmanager.annotations.Before;
import net.matmas.mainmenu.MenuItemExtensionPoint;
import net.matmas.mainmenu.Quit;
import net.matmas.toolbar.ToolBarItemExtensionPoint;
import net.matmas.tools.GraphicsTools;

/**
 *
 * @author matmas
 */
@Before({Quit.class})
public class NewFile implements MenuItemExtensionPoint, ToolBarItemExtensionPoint {

	private Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			
		}
	};

	public NewFile() {
		String name = "New";
		action.putValue(Action.NAME, name);
		action.putValue(Action.SMALL_ICON, GraphicsTools.getIcon("New16.gif"));
		action.putValue(Action.SHORT_DESCRIPTION, name);
		action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
	}

	public String getMenuPath() {
		return "File";
	}

	public Action getAction() {
		return action;
	}

	public boolean placeLeftSeparator() {
		return false;
	}

}
