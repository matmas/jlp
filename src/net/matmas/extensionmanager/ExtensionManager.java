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

package net.matmas.extensionmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.matmas.extensionmanager.annotations.Disabled;
import net.matmas.extensionmanager.annotations.After;
import net.matmas.extensionmanager.annotations.Before;

/**
 *
 * @author matmas
 */
public class ExtensionManager {

	private ExtensionManager() {
		try {
			loadPlugins();
			inspectDirectory(getPluginsDir());
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// -------------------------------------------------------------------------

	private static File getPluginsDir() throws URISyntaxException {
		File thisJARFile = new File(ExtensionManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		File pluginsDir = new File(thisJARFile.getParentFile() + "/plugins");
		if (!pluginsDir.exists()) {
			pluginsDir.mkdirs();
		}
		return pluginsDir;
	}

	private static void loadPlugins() throws URISyntaxException, MalformedURLException {
		File pluginsDir = getPluginsDir();
		List<URL> urls = new ArrayList<URL>();
		for (File file : listFilesRecursively(pluginsDir)) {
			if (isJar(file)) {
				URL url = file.toURI().toURL();
				urls.add(url);
			}
		}
		URLClassLoader uRLClassLoader = new URLClassLoader(urls.toArray(new URL[]{}), Thread.currentThread().getContextClassLoader());
		Thread.currentThread().setContextClassLoader(uRLClassLoader);
	}

	// -------------------------------------------------------------------------

	private static void inspect(Class caller) {
		try {
			inspect(new File(caller.getProtectionDomain().getCodeSource().getLocation().toURI()));
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(ExtensionManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void inspect(File jarFileOrDirectory) throws ClassNotFoundException, FileNotFoundException, IOException {
		if (jarFileOrDirectory.isDirectory()) {
			inspectDirectory(jarFileOrDirectory);
		}
		else if (isJar(jarFileOrDirectory)) {
			inspectJAR(jarFileOrDirectory);
		}
		else {
			throw new RuntimeException("File to inspect is not a JAR file");
		}
	}

	private static void inspectDirectory(File directory) throws ClassNotFoundException, FileNotFoundException, IOException {
		for (File fileEntry : listFilesRecursively(directory)) {
			if (!fileEntry.isDirectory()) {
				if (isJar(fileEntry)) {
					inspectJAR(fileEntry);
				}

				if (isClass(fileEntry.toString())) {
					inspectClass(getRelativePath(fileEntry, directory));
				}
			}
		}
	}

	private static void inspectJAR(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		JarInputStream jar = new JarInputStream(new FileInputStream(file));
		JarEntry jarEntry;
		while ((jarEntry = jar.getNextJarEntry()) != null) {
			if (!jarEntry.isDirectory()) {
				if (isClass(jarEntry.getName())) {
					inspectClass(jarEntry.getName());
				}
			}
		}
	}

	// -------------------------------------------------------------------------

	private static List<File> listFilesRecursively(File directory) {
		List<File> files = new ArrayList<File>();
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(listFilesRecursively(file));
			}
			if (file.isFile()) {
				files.add(file);
			}
		}
		return files;
	}
	
	private static boolean isJar(File file) {
		return file.getName().endsWith(".jar");
	}

	private static boolean isClass(String entry) {
		return entry.endsWith(".class");
	}

	private static String trimFileNameExtension(String filename) {
		return filename.replaceFirst("\\.[^\\.]*$", "");
	}

	private static String getRelativePath(File file, File directory) {
		return file.toString().substring(directory.getPath().length() + 1);
	}

	// -------------------------------------------------------------------------

	private static List<Class> allClasses = new ArrayList<Class>();

	public static List<Class> getAllClasses() {
		return allClasses;
	}
	
	// -------------------------------------------------------------------------

	private static void inspectClass(String entry) throws ClassNotFoundException {
		String className = trimFileNameExtension(entry.replace('/', '.'));

		Class clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
		allClasses.add(clazz);

		if (clazz.getSuperclass() != null) {

			for (Class interfaze : clazz.getInterfaces()) {
				if (!extensions.containsKey(interfaze)) {
					extensions.put(interfaze, new ArrayList<Class>());
				}
				if (!extensions.get(interfaze).contains(clazz)) {
					extensions.get(interfaze).add(clazz);
				}
			}

			for (Class subclazz : getSuperclasses(clazz)) {
				if (!extensions.containsKey(subclazz)) {
					extensions.put(subclazz, new ArrayList<Class>());
				}
				if (!extensions.get(subclazz).contains(clazz)) {
					extensions.get(subclazz).add(clazz);
				}
			}
		}
	}

	private static Map<Class, List<Class>> extensions = new Hashtable<Class, List<Class>>();

	// -------------------------------------------------------------------------

	private static <T> List<Class<T>> getSortedExtensions(Class<T> interfaceOrClass) {
		List<Class<T>> sortedExtensions = new ArrayList<Class<T>>();

		List<Class<T>> unsortedExtensions = new ArrayList<Class<T>>();
		for (Class clazz : extensions.get(interfaceOrClass)) {
			if (!clazz.isAnnotationPresent(Disabled.class)) {
				unsortedExtensions.add(clazz);
			}
		}

		Map<Class, Set<Class>> initializeAfter = new Hashtable<Class, Set<Class>>();
		for (Class clazz : unsortedExtensions) {
			After classInitializeAfter = (After)clazz.getAnnotation(After.class);
			if (classInitializeAfter != null) {
				if (!initializeAfter.containsKey(clazz)) {
					initializeAfter.put(clazz, new HashSet<Class>());
				}
				initializeAfter.get(clazz).addAll(Arrays.asList(classInitializeAfter.value()));
			}

			Before classInitializeBefore = (Before)clazz.getAnnotation(Before.class);
			if (classInitializeBefore != null) {
				for (Class classBefore : classInitializeBefore.value()) {
					if (!initializeAfter.containsKey(classBefore)) {
						initializeAfter.put(classBefore, new HashSet<Class>());
					}
					if (initializeAfter.containsKey(clazz) && initializeAfter.get(clazz).contains(classBefore)) {
						throw new RuntimeException("@" + After.class.toString().replaceFirst("^.*\\.", "") +
												" and @" + Before.class.toString().replaceFirst("^.*\\.", "") +
												"must not conflict with each other.");
					}
					initializeAfter.get(classBefore).add(clazz);
				}
			}
		}

		for (Class keyClass : initializeAfter.keySet()) {
			for (Class valueClass : initializeAfter.get(keyClass)) {
				if (valueClass == keyClass) {
					throw new RuntimeException("@" + After.class.toString().replaceFirst("^.*\\.", "") +
											" and @" + Before.class.toString().replaceFirst("^.*\\.", "") +
											" must not refer to the same class where used.");
				}

				if (!isExtension(valueClass, interfaceOrClass)) {
					initializeAfter.get(keyClass).remove(valueClass);
//					throw new RuntimeException("@" + InitializeAfter.class.toString().replaceFirst("^.*\\.", "") +
//											" and @" + InitializeBefore.class.toString().replaceFirst("^.*\\.", "") +
//											" must refer to compatible classes.");
				}
			}
		}

		Class clazz = null;
		int counter = 0;
		List<Class> problematicClasses = new ArrayList<Class>();

		while (!unsortedExtensions.isEmpty()) {

			if (clazz == null || !unsortedExtensions.contains(clazz)) {
				clazz = unsortedExtensions.get(0);
			}

			boolean retry = false;
			if (initializeAfter.containsKey(clazz)) {
				for (Class classInitializeAfter : initializeAfter.get(clazz)) {
					if (unsortedExtensions.contains(classInitializeAfter)) {
						clazz = classInitializeAfter;
						retry = true;
						break;
					}
				}
			}

			if (retry) {
				counter++;
				if (counter > unsortedExtensions.size()) {
					if (problematicClasses.size() > 0 && clazz == problematicClasses.get(0)) {
						throw new RuntimeException("Conflict with " +
							"@" + After.class.toString().replaceFirst("^.*\\.", "") +
							" and @" + Before.class.toString().replaceFirst("^.*\\.", "") +
							" between classes: " + problematicClasses);
					}
					problematicClasses.add(clazz);
				}
			}
			else {
				counter = 0;
			}

			if (!retry) {
				unsortedExtensions.remove(clazz);
				sortedExtensions.add(clazz);
			}

		}
		

		return sortedExtensions;
	}

	public static <T> List<T> getAllExtensions(Class<T> interfaceOrClass) {
		List<T> resultingInstances = new ArrayList<T>();
		if (extensions.containsKey(interfaceOrClass)) {
			for (Class<T> clazz : getSortedExtensions(interfaceOrClass)) {
				try {
					if (!classInstances.containsKey(clazz)) {
						classInstances.put(clazz, clazz.newInstance());
					}
					resultingInstances.add((T)classInstances.get(clazz));
				} catch (InstantiationException ex) {
					throw new RuntimeException(ex.getMessage() + " has no no-arg constructor.");
				} catch (IllegalAccessException ex) {
					throw new RuntimeException(ex.getMessage() + ". This class must have public no-arg constructor.");
				}
			}
		}
		return resultingInstances;
	}

	private static Map<Class, Object> classInstances = new Hashtable<Class, Object>();

	public static <T> T getInstanceOf(Class<T> clazz) {
		try {
			if (!classInstances.containsKey(clazz)) {
				classInstances.put(clazz, clazz.newInstance());
			}
			return (T)classInstances.get(clazz);
		} catch (InstantiationException ex) {
			throw new RuntimeException(ex.getMessage() + " has no no-arg constructor.");
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex.getMessage() + ". This class must have public no-arg constructor.");
		}
	}

	// -------------------------------------------------------------------------

	private static List<Class> getSuperclasses(Class clazz) {
		List<Class> superclasses = new ArrayList<Class>();
		while (clazz != Object.class) {
			clazz = clazz.getSuperclass();
			superclasses.add(clazz);
		}
		return superclasses;
	}

	private static boolean isExtension(Class extension, Class extensionPoint) {
//		if (extensions.get(extensionPoint) == null) {
//			return false;
//		}
//		return extensions.get(extensionPoint).contains(extension);
		if (extensionPoint.isInterface()) {
			return Arrays.asList(extension.getInterfaces()).contains(extensionPoint);
		}
		else if (extensionPoint.getSuperclass() != null) {
			return getSuperclasses(extension).contains(extensionPoint);
		}
		else {
			throw new RuntimeException();
		}
	}

	// -------------------------------------------------------------------------

	private static String[] applicationArguments;

	public static String[] getApplicationArguments() {
		return applicationArguments;
	}

	// -------------------------------------------------------------------------

	public static void main(String[] args) {
		applicationArguments = args;

		int i = 0;
		for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
			if (i >= 1) {
				Class caller = null;
				try {
					caller = Class.forName(stackTraceElement.getClassName());
				} catch (ClassNotFoundException ex) {
					throw new RuntimeException(ex);
				}
				inspect(caller);
			}
			i++;
		}

		getAllExtensions(BasicExtensionPoint.class);
	}
}
