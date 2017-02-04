
package com.unnsvc.rhena.common.search;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.logging.ILogger;

public class ModuleCollectingFlatTreeVisitor extends AFlatTreeVisitor {

	public ModuleCollectingFlatTreeVisitor(ILogger logger, IRhenaCache cache) {

		super(logger, cache);

	}
}
