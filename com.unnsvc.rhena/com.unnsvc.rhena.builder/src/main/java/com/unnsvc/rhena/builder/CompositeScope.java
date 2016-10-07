
package com.unnsvc.rhena.builder;

public enum CompositeScope {

	MODEL(null), 
	LIFECYCLE(CompositeScope.MODEL),
	NORMAL(CompositeScope.MODEL),
	TEST(CompositeScope.NORMAL),
	ITEST(CompositeScope.TEST);

	private CompositeScope dependency;

	CompositeScope(CompositeScope dependency) {

		this.dependency = dependency;
	}
	
	public static interface ICompositeScope {
		
	}
	
	public CompositeScope getDependency() {
		
		return dependency;
	}

	public static enum Model implements ICompositeScope {

		MODEL;
	}

	public static enum Normal implements ICompositeScope {

		MAIN_RESOURCES, MAIN_COMPILE, MAIN_PACKAGE;
	}

	public static enum Lifecycle implements ICompositeScope {

		One, TWO;
	}

	public static enum Test implements ICompositeScope {
		
		ONE;
	}

	public static enum ITest implements ICompositeScope {
		
		ONE;
	}

}
