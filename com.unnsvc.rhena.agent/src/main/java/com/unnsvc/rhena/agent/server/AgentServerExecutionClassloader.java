
package com.unnsvc.rhena.agent.server;

/**
 * This classloader will first attempt to load a class from the parent
 * classloader, if not found then attempt to load classes from the calling
 * client over tcp/ip
 * 
 * @author noname
 *
 */
public class AgentServerExecutionClassloader extends ClassLoader {

	private ClassLoader parent;

	public AgentServerExecutionClassloader(ClassLoader parent) {

		this.parent = parent;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		System.err.println("Load class " + name);
		return super.findClass(name);
	}

}
