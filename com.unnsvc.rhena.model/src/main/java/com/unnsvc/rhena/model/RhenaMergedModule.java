
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

/**
 * Merged module represents a module that has had all of its parents merged,
 * this module isn't cached but produced on-the-fly
 * 
 * @author noname
 *
 */
public class RhenaMergedModule extends RhenaModule {

	public RhenaMergedModule(ModuleIdentifier identifier, RepositoryIdentifier repositoryIdentifier) {

		super(identifier, repositoryIdentifier);
	}

	public RhenaMergedModule(IRhenaModule clone) {

		super(clone.getIdentifier(), clone.getRepositoryIdentifier());
		setModuleType(clone.getModuleType());
		setParent(clone.getParent());
		setLifecycleConfiguration(clone.getLifecycleConfiguration());
		setDependencies(clone.getDependencies());
		setProperties(clone.getProperties());
	}

	public void mergeOverwrite(IRhenaModule from) {

		setIdentifier(from.getIdentifier());
		setRepositoryIdentifier(from.getRepositoryIdentifier());
		setModuleType(from.getModuleType());
		setParent(from.getParent());
		setLifecycleConfiguration(from.getLifecycleConfiguration());
		getDependencies().addAll(from.getDependencies());
		getProperties().putAll(from.getProperties());
	}

}
