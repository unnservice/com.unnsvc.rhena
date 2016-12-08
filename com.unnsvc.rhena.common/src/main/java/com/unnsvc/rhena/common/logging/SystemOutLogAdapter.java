
package com.unnsvc.rhena.common.logging;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class SystemOutLogAdapter implements ILogAdapter {

	private Class<?> clazz;
	private int width;

	public SystemOutLogAdapter(Class<?> clazz, int width) {

		this.clazz = clazz;
		this.width = width;
	}

	public SystemOutLogAdapter(Class<?> clazz) {

		this(clazz, 30);
	}

	@Override
	public void info(ModuleIdentifier identifier, String message) {

		System.out.println(" INFO [" + shorten(clazz.getName()) + "] [" + identifier.toString() + "] " + message);
	}

	@Override
	public void warn(ModuleIdentifier identifier, String message) {

		System.out.println(" WARN [" + shorten(clazz.getName()) + "] [" + identifier.toString() + "] " + message);
	}

	@Override
	public void debug(ModuleIdentifier identifier, String message) {

		System.out.println("DEBUG [" + shorten(clazz.getName()) + "] [" + identifier.toString() + "] " + message);
	}

	@Override
	public void error(ModuleIdentifier identifier, String message) {

		System.out.println("ERROR [" + shorten(clazz.getName()) + "] [" + identifier.toString() + "] " + message);
	}

	@Override
	public void debug(String message) {

		System.out.println("DEBUG [" + shorten(clazz.getName()) + "] " + message);
	}

	private String shorten(String name) {

		if (name.length() > width) {

			return new String(name.getBytes(), name.length() - width, width);
		}

		return name;
	}
}
