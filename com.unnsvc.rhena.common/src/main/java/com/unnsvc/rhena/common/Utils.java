
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;

public class Utils {

	public static int stackTraceCount() {

		RhenaException re = new RhenaException(new Exception("no-op"));
		return re.getStackTrace().length;
	}

	public static boolean exists(URI uri) {

		String scheme = uri.getScheme();
		if (scheme.equals("http") || scheme.equals("https")) {
			return existsHttp(uri);
		} else {
			return existsOther(uri);
		}
	}

	private static boolean existsHttp(URI uri) {

		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean existsOther(URI uri) {

		try (InputStream is = uri.toURL().openStream()) {

			return true;

		} catch (Exception ex) {

		}

		return false;
	}

	public static URL toUrl(File file) throws RhenaException {

		try {
			return file.toURI().toURL();
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}

	public static URL toUrl(String url) throws RhenaException {

		try {
			return new URL(url);
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}

	public static Document newEmptyDocument() throws RhenaException {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			return document;
		} catch (ParserConfigurationException pce) {

			throw new RhenaException(pce.getMessage(), pce);
		}
	}

	public static EExecutionType valueOf(String executionType) throws RhenaException {

		switch (executionType) {
			case "model":
				return EExecutionType.MODEL;
			case "framework":
				return EExecutionType.FRAMEWORK;
			case "deliverable":
				return EExecutionType.DELIVERABLE;
			case "test":
				return EExecutionType.TEST;
			case "integration":
				return EExecutionType.INTEGRATION;
			case "prototype":
				return EExecutionType.PROTOTYPE;
			default:
				throw new RhenaException("Unknown execution type: " + executionType);
		}
	}

	public static URI toUri(String uriString) throws RhenaException {

		try {
			return new URI(uriString).normalize();
		} catch (URISyntaxException use) {
			throw new RhenaException(use.getMessage(), use);
		}
	}

	public static ModuleIdentifier readModuleIdentifier(File workspaceProject) throws RhenaException {

		File moduleDescriptor = new File(workspaceProject, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		if (!moduleDescriptor.isFile()) {
			throw new NotExistsException("No module descriptor for: " + workspaceProject);
		}
		try {
			// @TODO obs, we skip module validation to make it faster, so we
			// will need
			// to check for nulls on undeclared values
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();

			ModuleHandler handler = new ModuleHandler();
			parser.parse(moduleDescriptor, handler);

			String componentName = handler.componentStr;
			String version = handler.versionStr;

			String projectName = workspaceProject.getName();

			if (!projectName.startsWith(componentName + ".") || !(projectName.length() > componentName.length() + 2)) {
				throw new RhenaException("Invalid module declaration, module identifier not well formed");
			}

			projectName = projectName.substring(componentName.length() + 1);

			return new ModuleIdentifier(componentName, projectName, version);

		} catch (IOException | SAXException | ParserConfigurationException iae) {

			throw new RhenaException(iae);
		}
	}

	private static class ModuleHandler extends DefaultHandler {

		private String componentStr;
		private String versionStr;

		public ModuleHandler() {

		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if (localName.equals("meta")) {
				componentStr = attributes.getValue("component");
				versionStr = attributes.getValue("version");
			}
		}
	}

	/**
	 * @TODO ONLY SELECT THE LIFECYCEL RELATIONSHIP WHICH IS RELEVANT
	 * @param module
	 * @return
	 */
	public static List<IRhenaEdge> getAllRelationships(IRhenaModule module) {

		List<IRhenaEdge> relationships = new ArrayList<IRhenaEdge>();
		if (module.getParent() != null) {
			relationships.add(module.getParent());
		}
		if (module.getLifecycleName() != null) {
			ILifecycleDeclaration lifecycle = module.getLifecycleDeclarations().get(module.getLifecycleName());
			relationships.add(lifecycle.getContext().getModuleEdge());
			lifecycle.getProcessors().forEach(proc -> relationships.add(proc.getModuleEdge()));
			relationships.add(lifecycle.getGenerator().getModuleEdge());
		}
		relationships.addAll(module.getDependencies());
		return relationships;
	}
}
