
package com.unnsvc.rhena.common.logging;

public interface ILogFactory {

	public ILogAdapter createLog(Class<?> clazz);
}
