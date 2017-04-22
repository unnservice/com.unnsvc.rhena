
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.IRhenaResolver;

public class RhenaContext implements IRhenaContext {

	private IRhenaConfiguration config;
	private IRhenaCache cache;
	private IRhenaResolver resolver;
	private IRhenaFactories factories;

	public RhenaContext(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver, IRhenaFactories factories) {

		this.config = config;
		this.cache = cache;
		this.resolver = resolver;
		this.factories = factories;
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}

	@Override
	public IRhenaCache getCache() {

		return cache;
	}

	@Override
	public IRhenaResolver getResolver() {

		return resolver;
	}

	@Override
	public IRhenaFactories getFactories() {

		return factories;
	}

}
