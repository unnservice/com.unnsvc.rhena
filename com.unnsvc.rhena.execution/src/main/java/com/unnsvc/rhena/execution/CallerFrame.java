
package com.unnsvc.rhena.execution;

/**
 * Marker saying that it represents the caller so it has no incoming, nor any
 * module, just outgoing
 * 
 * @author noname
 *
 */
public class CallerFrame extends ExecutionFrame {

	public CallerFrame() {

		super(null);

	}

}
