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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.ExtensionManager;

/**
 *
 * @author matmas
 */
public class MainFrame extends JFrame implements BasicExtensionPoint, WindowListener {

	private Preferences preferences = Preferences.userNodeForPackage(this.getClass());

	private enum Setting {
		width, height
	}

	public MainFrame() {
		int width = preferences.getInt(Setting.width.name(), 600);
		int height = preferences.getInt(Setting.height.name(), 500);
		this.setSize(width, height);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				preferences.putInt(Setting.width.name(), getWidth());
				preferences.putInt(Setting.height.name(), getHeight());
				try {
					preferences.flush();
				} catch (BackingStoreException ex) {
					Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}));

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);

		this.setVisible(true);
	}

	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		quitApplication();
	}
	
	public static void quitApplication() {
		for (WindowClosingExtensionPoint windowClosingExtensionPoint : ExtensionManager.getAllExtensions(WindowClosingExtensionPoint.class)) {
			if (!windowClosingExtensionPoint.exitApplicationAllowed()) {
				return;
			}
		}
		System.exit(0);
	}
}
