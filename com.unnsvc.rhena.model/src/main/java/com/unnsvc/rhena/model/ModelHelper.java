
package com.unnsvc.rhena.model;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModelHelper {
	
	private static Logger log = LoggerFactory.getLogger(ModelHelper.class);

	/**
	 * We don't actually need this...
	 * 
	 * @param location
	 * @return
	 * @throws RhenaException
	 */
	public static ModuleIdentifier locationToModuleIdentifier(File location) throws RhenaException {

		try {
			location = location.getCanonicalFile();
			File module = new File(location, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();
			XMLReader reader = parser.getXMLReader();

			final ValueHolder componentValue = new ValueHolder();

			parser.parse(module, new DefaultHandler() {

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

					if (localName.equals("meta")) {

						String component = attributes.getValue("component");
						componentValue.setComponent(component);

						String version = attributes.getValue("version");
						componentValue.setVersion(version);

						// stop parsing
						reader.setContentHandler(null);
					}
				}
			});

			String projectName = location.getName();

			if (componentValue.isValid() && projectName.startsWith(componentValue.getComponent())
					&& projectName.length() > componentValue.getComponent().length()) {

				String moduleName = projectName.substring(componentValue.getComponent().length() + 1);
				return ModuleIdentifier.valueOf(componentValue.getComponent() + ":" + moduleName + ":" + componentValue.getVersion());

			} else {

				log.error("Extracted: " + componentValue.toString() + " from: " + location + " is valid? " + componentValue.isValid());
				return null;
			}
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	private static class ValueHolder {

		private String component;
		private String version;

		public ValueHolder() {

		}

		public String getVersion() {

			return version;
		}

		public void setVersion(String version) {

			this.version = version;
		}

		public void setComponent(String component) {

			this.component = component;
		}

		public String getComponent() {

			return component;
		}

		public boolean isValid() {

			return component != null && version != null;
		}

		@Override
		public String toString() {

			return "ValueHolder [component=" + component + ", version=" + version + "]";
		}
	}
}
