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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.ExtensionManager;
import net.matmas.mainframe.MainFrame;

/**
 *
 * @author matmas
 */
public class TabbedPane extends JTabbedPane implements BasicExtensionPoint {

	public TabbedPane() {
		MainFrame mainFrame = ExtensionManager.getInstanceOf(MainFrame.class);
		mainFrame.add(this, BorderLayout.CENTER);
		this.addTab("test", new JPanel());
		this.addTab("test2", new JPanel());
		
	}
	
}
