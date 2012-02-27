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

package net.matmas.mainmenu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import net.matmas.mainframe.MainFrame;

/**
 *
 * @author matmas
 */
public class Quit implements MenuItemExtensionPoint {

	private Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			MainFrame.quitApplication();
		}
	};

	public Quit() {
		String name = "Quit";
		action.putValue(Action.NAME, name);
		action.putValue(Action.SHORT_DESCRIPTION, name);
		action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Q"));
	}

	public String getMenuPath() {
		return "File";
	}

	public Action getAction() {
		return action;
	}
}
