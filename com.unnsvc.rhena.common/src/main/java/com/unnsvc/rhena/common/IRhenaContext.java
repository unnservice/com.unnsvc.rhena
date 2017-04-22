
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.IRhenaResolver;

public interface IRhenaContext {

	public IRhenaConfiguration getConfig();

	public IRhenaCache getCache();

	public IRhenaResolver getResolver();

	public IRhenaFactories getFactories();

}
