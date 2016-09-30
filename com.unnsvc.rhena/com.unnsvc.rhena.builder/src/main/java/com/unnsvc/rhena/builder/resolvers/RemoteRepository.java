
package com.unnsvc.rhena.builder.resolvers;

import java.net.URL;

import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class RemoteRepository implements IRepository {

	private URL baseUrl;

	public RemoteRepository(String urlString) throws RhenaException {

		try {
			this.baseUrl = new URL(urlString);
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	@Override
	public RhenaModule resolveModule(RhenaContext context, ModuleIdentifier moduleIdentifier) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");
	}
}
