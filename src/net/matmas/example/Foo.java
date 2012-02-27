/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.matmas.example;

import net.matmas.extensionmanager.annotations.InitializeBefore;

/**
 *
 * @author matmas
 */
@InitializeBefore({Bar.class})
public class Foo implements FooBarExtensionPoint {
	public String getName() {
		return "Foo";
	}
}
