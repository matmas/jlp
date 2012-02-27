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

package net.matmas.mainframe;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.annotations.Disabled;
import net.matmas.extensionmanager.annotations.InitializeBefore;

/**
 *
 * @author matmas
 */
@Disabled
@InitializeBefore({MainFrame.class})
public class LookAndFeel implements BasicExtensionPoint {

	public LookAndFeel() {
		UIManager.getDefaults().put("ToolTip.hideAccelerator", Boolean.TRUE);
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		try {
			String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
			if (systemLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
			else {
				UIManager.setLookAndFeel(systemLookAndFeel);
			}
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		} catch (UnsupportedLookAndFeelException ex) {
		}
	}
}
