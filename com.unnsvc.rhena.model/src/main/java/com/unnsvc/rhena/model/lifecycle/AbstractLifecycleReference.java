
package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.ILifecycleReference;

public abstract class AbstractLifecycleReference implements ILifecycleReference {

	private String schema;
	private String clazz;
	private Document config;
	
	private ModuleIdentifier source;
	private ESelectionType selectionType;
	private IEntryPoint entryPoint;

	public AbstractLifecycleReference(String schema, String clazz, Document config, ModuleIdentifier source, ESelectionType selectionType, IEntryPoint entryPoint) {

		this.schema = schema;
		this.clazz = clazz;
		this.config = config;
		this.source = source;
		this.selectionType = selectionType;
		this.entryPoint = entryPoint;
	}

	public String getSchema() {

		return schema;
	}

	public void setSchema(String schema) {

		this.schema = schema;
	}

	public String getClazz() {

		return clazz;
	}

	public void setClazz(String clazz) {

		this.clazz = clazz;
	}

	public Document getConfig() {

		return config;
	}

	public void setConfig(Document config) {

		this.config = config;
	}

	@Override
	public ModuleIdentifier getSource() {

		
		return source;
	}

	@Override
	public void setSource(ModuleIdentifier source) {

		this.source = source;
	}

	@Override
	public ESelectionType getTraverseType() {

		
		return selectionType;
	}

	@Override
	public void setTraverseType(ESelectionType selectionType) {

		this.selectionType = selectionType;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		
		return entryPoint;
	}

	@Override
	public void setEntryPoint(IEntryPoint entryPoint) {

		this.entryPoint = entryPoint;
	}

}
