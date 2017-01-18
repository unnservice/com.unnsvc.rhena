
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

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

	/**
	 * Throws checked exception if it can't convert to enum
	 * 
	 * @param executionType
	 * @return
	 * @throws RhenaException
	 */
	public static EExecutionType valueOf(String executionType) throws RhenaException {

		try {
			return EExecutionType.valueOf(executionType.toUpperCase());
		} catch (Throwable t) {
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
	 * To file name without extension
	 * 
	 * @param identifier
	 * @param type
	 * @return
	 */
	public static String toFileName(ModuleIdentifier identifier, EExecutionType type) {

		return identifier.getComponentName().toString() + "." + identifier.getModuleName().toString() + "-" + type.toString().toLowerCase() + "-"
				+ identifier.getVersion().toString();
	}

	public static List<IEntryPoint> getAllEntryPoints(IRhenaModule module, boolean lifecycles) {

		List<IEntryPoint> eps = new ArrayList<IEntryPoint>();
		if (module.getParent() != null) {
			eps.add(module.getParent().getEntryPoint());
		}
		if (lifecycles) {
			if (!module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
				ILifecycleReference lifecycle = module.getLifecycleDeclarations().get(module.getLifecycleName());
				eps.add(lifecycle.getContext().getModuleEdge().getEntryPoint());
				lifecycle.getProcessors().forEach(proc -> eps.add(proc.getModuleEdge().getEntryPoint()));
				eps.add(lifecycle.getGenerator().getModuleEdge().getEntryPoint());
				lifecycle.getCommands().forEach(comm -> eps.add(comm.getModuleEdge().getEntryPoint()));
			}
		}
		module.getDependencies().forEach(dep -> eps.add(dep.getEntryPoint()));

		return eps;
	}

	public static String generateSha1(File generated) throws RhenaException {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			try (InputStream input = new FileInputStream(generated)) {
				byte[] buffer = new byte[8192];
				int len = input.read(buffer);

				while (len != -1) {
					digest.update(buffer, 0, len);
					len = input.read(buffer);
				}
				return new HexBinaryAdapter().marshal(digest.digest());
			}
		} catch (NoSuchAlgorithmException | IOException nsae) {
			throw new RhenaException(nsae.getMessage(), nsae);
		} finally {
			if (digest != null) {
				try {
					digest.clone();
				} catch (CloneNotSupportedException e) {

					throw new RhenaException(e.getMessage(), e);
				}
			}
		}
	}

	public static Node getChildNode(Document document, String nodeName) {

		NodeList nl = document.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {

			Node child = nl.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getLocalName().equals(nodeName)) {
				return child;
			}
		}
		return null;
	}

	public static Node getChildNode(Node node, String nodeName) {

		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {

			Node child = nl.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getLocalName().equals(nodeName)) {
				return child;
			}
		}
		return null;
	}

	public static String format(String format, Object... args) {

		try (Formatter f = new Formatter()) {

			return f.format(format, args).toString();
		}
	}
}
