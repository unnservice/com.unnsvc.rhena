
package com.unnsvc.rhena.core.execution;

import java.util.Stack;

import com.unnsvc.rhena.common.exceptions.NotUniqueException;

public class UniqueStack<T> extends Stack<T> {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	public synchronized void pushUnique(T e) throws NotUniqueException {

		if (e == null) {
			throw new NotUniqueException("Attempted to push null value into UniqueStack.");
		}

		if (contains(e)) {

			super.push(e);
			throw new NotUniqueException("Pushing an non-unique object");
		}
		super.push(e);
	}
}
