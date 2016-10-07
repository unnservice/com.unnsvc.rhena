
package com.unnsvc.rhena.builder.resolvers;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;

public class RemoteRepository implements RhenaMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private URL baseUrl;

	public RemoteRepository(String urlString) throws RhenaException {

		try {
			this.baseUrl = new URL(urlString);
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	@Override
	public RhenaModuleDescriptor materialiseScope(RhenaContext context, CompositeScope scope, ModuleIdentifier moduleIdentifier) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");
	}
}
