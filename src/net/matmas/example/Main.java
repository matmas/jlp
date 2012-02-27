/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.matmas.example;

import net.matmas.extensionmanager.BasicExtensionPoint;
import net.matmas.extensionmanager.ExtensionManager;

/**
 *
 * @author matmas
 */
public class Main implements BasicExtensionPoint {
	public Main() {
		for (FooBarExtensionPoint extension : ExtensionManager.getAllExtensions(FooBarExtensionPoint.class)) {
			System.out.println(extension.getName());
		}
	}
}
