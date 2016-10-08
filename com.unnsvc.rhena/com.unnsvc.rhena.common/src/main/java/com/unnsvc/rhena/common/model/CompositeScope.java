
package com.unnsvc.rhena.common.model;

public class CompositeScope {

	public static final CompositeScope MODEL = new CompositeScope("model", null, Subscope.MODEL);
	public static final CompositeScope NORMAL = new CompositeScope("normal", CompositeScope.MODEL, Subscope.RESOURCES, Subscope.COMPILE, Subscope.PACKAGE);
	public static final CompositeScope LIFECYCLE = new CompositeScope("lifecycle", CompositeScope.MODEL, Subscope.RESOURCES, Subscope.COMPILE, Subscope.PACKAGE);
	public static final CompositeScope TEST = new CompositeScope("test", CompositeScope.NORMAL, Subscope.RESOURCES, Subscope.COMPILE, Subscope.PACKAGE, Subscope.TEST);
	public static final CompositeScope ITEST = new CompositeScope("itest", CompositeScope.TEST, Subscope.RESOURCES, Subscope.COMPILE, Subscope.PACKAGE, Subscope.TEST, Subscope.ITEST);

	private String scopeName;
	private CompositeScope scopeDependency;
	private Subscope[] subscopes;

	CompositeScope(String scopeName, CompositeScope scopeDependency, Subscope... subscopes) {

		this.scopeName = scopeName;
		this.scopeDependency = scopeDependency;
		this.subscopes = subscopes;
	}

	public Subscope[] getSubscopes() {

		return subscopes;
	}

	@Override
	public String toString() {

		return scopeName;
	}

	public static enum Subscope {

		MODEL, RESOURCES, COMPILE, PACKAGE, TEST, ITEST;
	}

	public CompositeScope getDependency() {

		return scopeDependency;
	}

	public static CompositeScope valueOf(String scopeString) {

		switch (scopeString) {
			case "model":
				return CompositeScope.MODEL;
			case "lifecycle":
				return CompositeScope.LIFECYCLE;
			case "normal":
				return CompositeScope.NORMAL;
			case "test":
				return CompositeScope.TEST;
			case "itest":
				return CompositeScope.ITEST;
		}
		throw new IllegalArgumentException("Not a valid scope: " + scopeString);
	}
}
