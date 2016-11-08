
package com.unnsvc.rhena.core;

import java.util.HashSet;
import java.util.Iterator;

import com.unnsvc.rhena.common.model.IRhenaEdge;

/**
 * An edge set is similar to a hashSet but will ensure that edges are kept
 * unique
 * 
 * @author noname
 *
 */
public class EdgeSet extends HashSet<IRhenaEdge> {

	private static final long serialVersionUID = 1L;

	public boolean addEdge(IRhenaEdge edge) {

		for (Iterator<IRhenaEdge> iter = this.iterator(); iter.hasNext();) {

			IRhenaEdge existing = iter.next();

			if (existing.getTarget().equals(edge.getTarget())) {
				if (existing.getTraverseType().equals(edge.getTraverseType())) {

					if (existing.getExecutionType().equals(edge.getExecutionType())) {
						
						return false;
					} else if (edge.getExecutionType().canTraverse(existing.getExecutionType())) {

						iter.remove();
						return add(edge);
					} else if (existing.getExecutionType().canTraverse(edge.getExecutionType())) {
						
						return false;
					}
				}
			}
		}

		return add(edge);
	}
}
