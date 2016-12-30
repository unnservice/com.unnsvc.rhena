
package com.unnsvc.rhena.core;

import java.util.HashSet;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.core.model.EntryPoint;

/**
 * An edge set is similar to a hashSet but will ensure that edges are kept in
 * unique and in order
 * 
 * @author noname
 *
 */
public class ExecutionMergeEdgeSet extends HashSet<IEntryPoint> {

	private static final long serialVersionUID = 1L;

	public boolean addEntryPoint(IEntryPoint entryPoint) {

		if (entryPoint.getExecutionType().hasParent()) {
			addEntryPoint(new EntryPoint(entryPoint.getExecutionType().getParent(), entryPoint.getTarget()));
		}

		return add(entryPoint);
	}
}
