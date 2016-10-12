
package com.unnsvc.rhena.lifecycle;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.IProjectConfiguration;

public class DefaultGenerator implements IGenerator {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Document configuration) {

	}

	@Override
	public File generate(IRhenaModule model, IProjectConfiguration configurator) throws RhenaException {

		File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
		File dummyFile = new File(sysTempDir, "rhena-default-processor-dummy-" + model.getModuleIdentifier().getModuleName());
		try {
			dummyFile.createNewFile();
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}

		return dummyFile;
	}

}
