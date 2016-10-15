
package com.unnsvc.rhena.core.model.processing;

import java.util.Stack;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class UniqueStack<T> extends Stack<T> {

	private static final long serialVersionUID = 1L;

	public synchronized void pushUnique(T e) throws RhenaException {

		if (contains(e)) {
			throw new RhenaException("Pushing an non-unique object");
		}
		super.push(e);
	}
}
