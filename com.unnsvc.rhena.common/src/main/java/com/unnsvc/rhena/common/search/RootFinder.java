
package com.unnsvc.rhena.common.search;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class RootFinder {

	private IRhenaCache cache;
	private ModuleIdentifier identifier;
	private EExecutionType type;
	private String componentNameOrigin;
	private String componentNameCurrent;

	public RootFinder(IRhenaCache cache, ModuleIdentifier identifier, EExecutionType type, String componentNameOrigin, String componentNameCurrent) {

		this.cache = cache;
		this.identifier = identifier;
		this.type = type;
		this.componentNameOrigin = componentNameOrigin;
		this.componentNameCurrent = componentNameCurrent;
	}

	public Set<ModuleIdentifier> findRoots(int level) {

		Set<ModuleIdentifier> roots = new HashSet<ModuleIdentifier>();

		for (IRhenaEdge edge : cache.getEdges()) {

			/**
			 * Test each edge
			 */
			if (edge.getEntryPoint().getTarget().equals(identifier)) {
				
				if (edge.getEntryPoint().getExecutionType().compareTo(type) >= 0) {
					System.err.println("Found edge targeting identifier: " + edge);
					
					if (edge.getTraverseType().equals(ESelectionType.DIRECT)) {
						if(level != 1) {
							continue;
						}
						// higher up the hierarchy and DIRECT won't reach what
						// we're trying to target
						// so we have found root on the previous node
						// add current as root
						roots.add(identifier);
						continue;
					}
					if (edge.getTraverseType().equals(ESelectionType.COMPONENT)) {
						if (!componentNameOrigin.equals(componentNameCurrent)) {
							// current node was a root so don't walk further up
							roots.add(identifier);
							continue;
						}
					}
					RootFinder upward = new RootFinder(cache, edge.getSource(), type, componentNameOrigin, edge.getSource().getComponentName().toString());
					roots.addAll(upward.findRoots(level++));
				}
			}
		}
		
		/**
		 * No edges were found to be targeting this, or this was the first parent
		 */
		if(roots.isEmpty()) {
			roots.add(identifier);
		}

		return roots;
	}

}
