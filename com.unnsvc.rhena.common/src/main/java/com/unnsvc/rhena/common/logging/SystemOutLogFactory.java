
package com.unnsvc.rhena.common.logging;

public class SystemOutLogFactory implements ILogFactory {

	/**
	 * @TODO cache loggers?
	 */
	@Override
	public ILogAdapter createLog(Class<?> clazz) {

		return new SystemOutLogAdapter(clazz);
	}

}
