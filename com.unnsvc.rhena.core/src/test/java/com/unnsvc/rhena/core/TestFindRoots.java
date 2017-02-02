
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

	@Test
	public void testFindRoot() throws RhenaException {

		InMemoryRepository repo = new InMemoryRepository(getContext());

		InMemoryModule one = new InMemoryModule("test:one:0.0.1", repo);
		InMemoryModule two = new InMemoryModule("test:two:0.0.1", repo);
		InMemoryModule three = new InMemoryModule("test:three:0.0.1", repo);
		InMemoryModule four = new InMemoryModule("test:four:0.0.1", repo);

		one.getDeclaredDependencies().add(new InMemoryEdge(one, two, EExecutionType.TEST, ESelectionType.SCOPE));
		two.getDeclaredDependencies().add(new InMemoryEdge(two, three, EExecutionType.TEST, ESelectionType.DIRECT));
		three.getDeclaredDependencies().add(new InMemoryEdge(three, four, EExecutionType.TEST, ESelectionType.SCOPE));

		getContext().addAdditionalRepository(repo);
		repo.addModule(one);
		repo.addModule(two);
		repo.addModule(three);
		repo.addModule(four);

		IRhenaEngine engine = new RhenaEngine(getContext());
		IRhenaModule module = engine.materialiseModel(ModuleIdentifier.valueOf("test:four:0.0.1"));
		Assert.assertNotNull(module);
		Set<ModuleIdentifier> roots = engine.findRoots(module.getIdentifier(), EExecutionType.TEST);
		roots.forEach(root -> System.err.println("Root contained: " + root));
		Assert.assertNotNull(roots);
		Assert.assertTrue(!roots.isEmpty());
		Assert.assertEquals(roots.size(), 1);
		Assert.assertTrue(roots.contains(ModuleIdentifier.valueOf("test:three:0.0.1")));
	}
	
	@Test
	public void testFindMultipleRoot() throws RhenaException {

		InMemoryRepository repo = new InMemoryRepository(getContext());

		InMemoryModule one = new InMemoryModule("test:one:0.0.1", repo);
		InMemoryModule two = new InMemoryModule("test:two:0.0.1", repo);
		InMemoryModule three = new InMemoryModule("test:three:0.0.1", repo);
		InMemoryModule four = new InMemoryModule("test:four:0.0.1", repo);
		InMemoryModule five = new InMemoryModule("test:five:0.0.1", repo);

		one.getDeclaredDependencies().add(new InMemoryEdge(one, two, EExecutionType.TEST, ESelectionType.DIRECT));
		one.getDeclaredDependencies().add(new InMemoryEdge(one, three, EExecutionType.TEST, ESelectionType.DIRECT));

		two.getDeclaredDependencies().add(new InMemoryEdge(two, four, EExecutionType.TEST, ESelectionType.SCOPE));
		three.getDeclaredDependencies().add(new InMemoryEdge(three, four, EExecutionType.TEST, ESelectionType.SCOPE));
		
		four.getDeclaredDependencies().add(new InMemoryEdge(four, five, EExecutionType.TEST, ESelectionType.SCOPE));

		getContext().addAdditionalRepository(repo);
		repo.addModule(one);
		repo.addModule(two);
		repo.addModule(three);
		repo.addModule(four);
		repo.addModule(five);

		IRhenaEngine engine = new RhenaEngine(getContext());
		IRhenaModule module = engine.materialiseModel(ModuleIdentifier.valueOf("test:five:0.0.1"));
		Assert.assertNotNull(module);
		Set<ModuleIdentifier> roots = engine.findRoots(module.getIdentifier(), EExecutionType.TEST);
		roots.forEach(root -> System.err.println("Root contained: " + root));
		Assert.assertNotNull(roots);
		Assert.assertTrue(!roots.isEmpty());
		Assert.assertEquals(roots.size(), 2);
		Assert.assertTrue(roots.contains(ModuleIdentifier.valueOf("test:two:0.0.1")));
		Assert.assertTrue(roots.contains(ModuleIdentifier.valueOf("test:three:0.0.1")));
	}
}
