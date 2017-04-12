package com.unnsvc.rhena.agent.classloaders;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class CustomURLClassLoader extends URLClassLoader {

	public CustomURLClassLoader(List<URL> classpath, ClassLoader parent) {

		super(classpath.toArray(new URL[classpath.size()]), parent);
	}
}
