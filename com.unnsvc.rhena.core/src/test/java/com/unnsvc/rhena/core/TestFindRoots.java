
package com.unnsvc.rhena.core;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.InMemoryEdge;
import com.unnsvc.rhena.core.model.InMemoryModule;
import com.unnsvc.rhena.core.resolution.InMemoryRepository;

public class TestFindRoots extends AbstractRhenaTest {

//	@Test
//	public void testFindRoot() throws RhenaException {
//
//		InMemoryRepository repo = new InMemoryRepository(getContext());
//
//		InMemoryModule one = new InMemoryModule("test:one:0.0.1", repo);
//		InMemoryModule two = new InMemoryModule("test:two:0.0.1", repo);
//		InMemoryModule three = new InMemoryModule("test:three:0.0.1", repo);
//		InMemoryModule four = new InMemoryModule("test:four:0.0.1", repo);
//
//		one.getDependencies().add(new InMemoryEdge(one, two, EExecutionType.TEST, ESelectionType.SCOPE));
//		two.getDependencies().add(new InMemoryEdge(two, three, EExecutionType.TEST, ESelectionType.SCOPE));
//		three.getDependencies().add(new InMemoryEdge(three, four, EExecutionType.TEST, ESelectionType.SCOPE));
//
//		
//		getContext().addAdditionalRepository(repo);
//		repo.addModule(one);
//		repo.addModule(two);
//		repo.addModule(three);
//		repo.addModule(four);
//
//		IRhenaEngine engine = new RhenaEngine(getContext());
//		IRhenaModule module = engine.materialiseModel(ModuleIdentifier.valueOf("test:four:0.0.1"));
//		Assert.assertNotNull(module);
//		Set<ModuleIdentifier> roots = engine.findRoots(module.getIdentifier(), EExecutionType.TEST);
//		Assert.assertNotNull(roots);
//		Assert.assertTrue(!roots.isEmpty());
//	}
}
