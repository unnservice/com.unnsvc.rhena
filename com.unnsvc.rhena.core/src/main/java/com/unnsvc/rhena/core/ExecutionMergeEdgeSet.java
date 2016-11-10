
package com.unnsvc.rhena.core;

import java.util.HashSet;
import java.util.Iterator;

import com.unnsvc.rhena.common.model.IEntryPoint;

/**
 * An edge set is similar to a hashSet but will ensure that edges are kept
 * unique
 * 
 * @author noname
 *
 */
public class ExecutionMergeEdgeSet extends HashSet<IEntryPoint> {

	private static final long serialVersionUID = 1L;

	public boolean addEdge(IEntryPoint edge) {

		for (Iterator<IEntryPoint> iter = this.iterator(); iter.hasNext();) {

			IEntryPoint existing = iter.next();

			if (existing.getTarget().equals(edge.getTarget())) {

				if (existing.getExecutionType().equals(edge.getExecutionType())) {

					return false;
				} else if (edge.getExecutionType().isParentOf(existing.getExecutionType())) {

					iter.remove();
					return add(edge);
				} else if (existing.getExecutionType().canTraverse(edge.getExecutionType())) {

					return false;
				}
			}
		}

		return add(edge);
	}
}
