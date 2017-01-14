
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.InMemoryModule;
import com.unnsvc.rhena.core.resolution.InMemoryRepository;

public class TestInMemoryRepos extends AbstractRhenaTest {

	@Test
	public void testInMemory() throws RhenaException {

		InMemoryRepository repo = new InMemoryRepository(getContext());

		InMemoryModule one = new InMemoryModule("test:one:0.0.1", repo);
		InMemoryModule two = new InMemoryModule("test:two:0.0.1", repo);
		InMemoryModule three = new InMemoryModule("test:three:0.0.1", repo);
		InMemoryModule four = new InMemoryModule("test:four:0.0.1", repo);

		getContext().addAdditionalRepository(repo);
		repo.addModule(one);
		repo.addModule(two);
		repo.addModule(three);
		repo.addModule(four);

		IRhenaEngine engine = new RhenaEngine(getContext());
		IRhenaModule module = engine.materialiseModel(ModuleIdentifier.valueOf("test:one:0.0.1"));
		Assert.assertTrue(module instanceof InMemoryModule);
		Assert.assertEquals(module.getIdentifier().toString(), "test:one:0.0.1");
	}
}
