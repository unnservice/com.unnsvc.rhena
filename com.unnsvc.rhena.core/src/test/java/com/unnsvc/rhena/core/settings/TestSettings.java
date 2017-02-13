
package com.unnsvc.rhena.core.settings;

import org.junit.Test;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.core.config.RhenaConfiguration;
import com.unnsvc.rhena.core.config.RhenaSettingsParser;

public class TestSettings {

	@Test
	public void testSettings() throws RhenaException {

		new RhenaSettingsParser().loadUserSettings(new RhenaConfiguration());
		
		
	}
}
