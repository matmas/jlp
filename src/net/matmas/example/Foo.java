/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.matmas.example;

import net.matmas.extensionmanager.annotations.Before;

/**
 *
 * @author matmas
 */
@Before({Bar.class})
public class Foo implements FooBarExtensionPoint {
	public String getName() {
		return "Foo";
	}
}
