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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.ExtensionManager;
import net.matmas.mainframe.MainFrame;

/**
 *
 * @author matmas
 */
public class MainMenu implements BasicExtensionPoint {

	private JMenuBar menuBar = new JMenuBar();

	public MainMenu() {
		MainFrame mainFrame = ExtensionManager.getInstanceOf(MainFrame.class);
		mainFrame.setJMenuBar(menuBar);

		for (MenuItemExtensionPoint menuItem : ExtensionManager.getAllExtensions(MenuItemExtensionPoint.class)) {
			String path = menuItem.getMenuPath();

			JMenu currentMenu = null;
			for (String nextMenuName : path.split("->")) {
				JMenu nextMenu = getNextMenu(currentMenu, nextMenuName);

				addMenu(currentMenu, nextMenu);
				currentMenu = nextMenu;
			}
			currentMenu.add(menuItem.getAction());
		}
		
		mainFrame.validate();
	}

	private JMenu getNextMenu(JMenu currentMenu, String nextMenuName) {
		JMenu nextMenu = null;

		if (currentMenu == null) {
			for (int i = 0; i < menuBar.getMenuCount(); i++) {
				JMenu menu = menuBar.getMenu(i);
				if (menu.getText().equals(nextMenuName)) {
					nextMenu = menu;
				}
			}
		}
		else {
			for (int i = 0; i < currentMenu.getItemCount(); i++) {
				if (currentMenu.getItem(i) instanceof JMenu) {
					JMenu menu = (JMenu)currentMenu.getItem(i);
					if (menu.getText().equals(nextMenuName)) {
						nextMenu = menu;
					}
				}
			}
		}

		if (nextMenu == null) {
			nextMenu = new JMenu(nextMenuName);
		}
		return nextMenu;
	}

	private void addMenu(JMenu currentMenu, JMenu menuToAdd) {
		if (currentMenu == null) {
			menuBar.add(menuToAdd);
		}
		else {
			currentMenu.add(menuToAdd);
		}
	}
}
