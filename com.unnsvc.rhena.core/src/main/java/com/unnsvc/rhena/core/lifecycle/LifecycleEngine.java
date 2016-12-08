
package com.unnsvc.rhena.core.lifecycle;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;

/**
 * @TODO Eventually we want to cache the individual processors instead of the
 *       lifecycle as a whole and build a lifecycle on the fly? Cache lifecycle
 *       by name?
 * 
 * @author noname
 *
 */
public class LifecycleEngine {

	public Map<ILifecycleReference, ILifecycle> lifecycles;

	public LifecycleEngine() {

		this.lifecycles = new HashMap<ILifecycleReference, ILifecycle>();
	}

	public ILifecycle getLifecycle(ILifecycleReference reference) {

		ILifecycle lifecycle = lifecycles.get(reference);
		if (lifecycle == null) {
			lifecycle = createLifecycle(reference);
		}
		return lifecycle;
	}

	/**
	 * 
	 * @param reference
	 * @return
	 */
	private ILifecycle createLifecycle(ILifecycleReference reference) {

		Lifecycle lifecycle = new Lifecycle();		
		return lifecycle;
	}
}
