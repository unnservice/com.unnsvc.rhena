package com.unnsvc.rhena.common.config;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IUserConfigParser {

	public IRepositoryConfiguration parse(Document document) throws RhenaException;

	public byte[] serialise(IRepositoryConfiguration repoConfig) throws RhenaException;

}
