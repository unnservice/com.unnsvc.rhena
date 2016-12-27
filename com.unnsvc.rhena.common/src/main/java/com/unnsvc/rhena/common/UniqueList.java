
package com.unnsvc.rhena.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Method which overrides the add and addALl methods to check for uniqueness
 * 
 * @author noname
 *
 * @param <T>
 */
public class UniqueList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(T e) {

		if (contains(e)) {
			return false;
		} else {
			return super.add(e);
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {

		boolean added = false;
		for (T t : c) {
			added = add(t);
		}
		return added;
	}
}
