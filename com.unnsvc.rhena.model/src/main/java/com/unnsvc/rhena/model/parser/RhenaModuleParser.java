
package com.unnsvc.rhena.model.parser;

import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ERhenaModuleType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.lifecycle.DefaultLifecycleConfiguration;
import com.unnsvc.rhena.model.EntryPoint;
import com.unnsvc.rhena.model.LifecycleConfiguration;
import com.unnsvc.rhena.model.RhenaEdge;
import com.unnsvc.rhena.model.RhenaModule;
import com.unnsvc.rhena.model.UnresolvedLifecycleConfiguration;
import com.unnsvc.rhena.model.lifecycle.CommandReference;
import com.unnsvc.rhena.model.lifecycle.ContextReference;
import com.unnsvc.rhena.model.lifecycle.GeneratorReference;
import com.unnsvc.rhena.model.lifecycle.ProcessorReference;

/**
 * @TODO XSD validation once the settings.xml format is established
 * @author noname
 *
 */
public class RhenaModuleParser {

	private RhenaModule module;

	public RhenaModuleParser(RepositoryIdentifier repositoryIdentifier, ModuleIdentifier identifier, URI moduleDescriptorLocation)
			throws RhenaException {

		try {
			this.module = new RhenaModule(identifier, repositoryIdentifier);

			parse(moduleDescriptorLocation);
		} catch (Exception re) {
			throw new RhenaException(re.getMessage(), re);
		}
	}

	public void parse(URI uri) throws Exception {

		Document document = parseAndValidateDocument(uri);

		NodeList children = document.getChildNodes();
		Node moduleNode = children.item(0);

		if (moduleNode.getLocalName().equals("module")) {
			module.setModuleType(ERhenaModuleType.MODULE);
		} else if (moduleNode.getLocalName().equals("framework")) {
			module.setModuleType(ERhenaModuleType.FRAMEWORK);
		}

		if (moduleNode.getAttributes().getNamedItem("extends") != null) {
			Node extendsAttribute = moduleNode.getAttributes().getNamedItem("extends");
			if (extendsAttribute != null) {
				String extendsModuleIdentifierStr = extendsAttribute.getNodeValue();
				ModuleIdentifier extendsModuleIdentifier = ModuleIdentifier.valueOf(extendsModuleIdentifierStr);
				IRhenaEdge edge = newEdge(module.getIdentifier(), null, extendsModuleIdentifier, ESelectionType.HIERARCHY);
				module.setParent(edge);
			}
		}

		NodeList moduleChildren = moduleNode.getChildNodes();
		for (int i = 0; i < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if (moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if (moduleChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_MODULE)) {
					processMetaNode(moduleChild);
				} else if (moduleChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_DEPENDENCY)) {
					processDepenencyNode(moduleChild);
				}
			}
		}
	}

	private Document parseAndValidateDocument(URI uri) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(uri.toString());

		// validate
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/module.xsd"));

		try {
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (Exception ex) {
			throw new RhenaException("Schema validation error for: " + uri.toString(), ex);
		}

		return document;
	}

	private void processPropertyNode(Node propertyNode) {

		String nodeName = propertyNode.getLocalName();
		String nodeValue = propertyNode.getTextContent();
		module.setProperty(nodeName, nodeValue);
	}

	private void processMetaNode(Node moduleChild) throws DOMException, RhenaException {

		String componentNameStr = moduleChild.getAttributes().getNamedItem("component").getNodeValue();
		String versionStr = moduleChild.getAttributes().getNamedItem("version").getNodeValue();

		Node lifecycleAttrNode = moduleChild.getAttributes().getNamedItem("lifecycle");
		if (lifecycleAttrNode == null || lifecycleAttrNode.getNodeValue().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {

			ILifecycleConfiguration defaultConfig = new DefaultLifecycleConfiguration(RhenaConstants.DEFAULT_LIFECYCLE_NAME);
			module.setLifecycleConfiguration(defaultConfig);
		} else {
			UnresolvedLifecycleConfiguration lifecycleConfiguration = new UnresolvedLifecycleConfiguration(lifecycleAttrNode.getNodeValue());
			module.setLifecycleConfiguration(lifecycleConfiguration);
		}

		if (!module.getIdentifier().getComponentName().toString().equals(componentNameStr)) {
			throw new RhenaException("Component mismatch between: " + module.getIdentifier().getComponentName() + " and declared: " + componentNameStr);
		} else if (!module.getIdentifier().getVersion().toString().equals(versionStr)) {
			throw new RhenaException("Version mismatch on requested module: " + module.getIdentifier().toString() + " and declared version: " + versionStr);
		}

		NodeList metaNodeChildren = moduleChild.getChildNodes();
		for (int i = 0; i < metaNodeChildren.getLength(); i++) {
			Node metaChild = metaNodeChildren.item(i);
			if (metaChild.getNodeType() == Node.ELEMENT_NODE) {
				if (metaChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_PROPERTIES)) {
					processPropertyNode(metaChild);
				} else if (metaChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_MODULE)) {
					if (metaChild.getLocalName().equals("lifecycle")) {
						ILifecycleConfiguration declaredConfiguration = processLifecycleNode(metaChild);
						module.addDeclaredConfiguration(declaredConfiguration);
					}
				}
			}
		}
	}

	private ILifecycleConfiguration processLifecycleNode(Node lifecycleNode) throws RhenaException {

		String lifecycleName = lifecycleNode.getAttributes().getNamedItem("name").getNodeValue();
		LifecycleConfiguration lifecycleConfiguration = new LifecycleConfiguration(lifecycleName);

		NodeList children = lifecycleNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				String moduleAttrStr = child.getAttributes().getNamedItem("module").getNodeValue();
				String clazzAttrStr = child.getAttributes().getNamedItem("class").getNodeValue();
				String schemaAttrStr = null;
				
				if (child.getAttributes().getNamedItem("schema") != null) {
					schemaAttrStr = child.getAttributes().getNamedItem("schema").getNodeValue();
				}

				// Make a document out of the entire processor node
				Document config = nodeToDocument(child);

				EExecutionType et = EExecutionType.MAIN;
				ESelectionType tt = ESelectionType.SCOPE;

				ModuleIdentifier source = module.getIdentifier();
				ModuleIdentifier target = ModuleIdentifier.valueOf(moduleAttrStr);
				IEntryPoint entryPoint = new EntryPoint(et, target);

				/**
				 * @TODO perform caching on edge
				 */

				if (child.getLocalName().equals("context")) {

					ContextReference configurator = new ContextReference(schemaAttrStr, clazzAttrStr, config, source, tt, entryPoint);
					lifecycleConfiguration.setContext(configurator);
				} else if (child.getLocalName().equals("processor")) {

					ProcessorReference processor = new ProcessorReference(schemaAttrStr, clazzAttrStr, config, source, tt, entryPoint);
					lifecycleConfiguration.addProcessor(processor);
				} else if (child.getLocalName().equals("generator")) {

					GeneratorReference generator = new GeneratorReference(schemaAttrStr, clazzAttrStr, config, source, tt, entryPoint);
					lifecycleConfiguration.setGenerator(generator);
				} else if (child.getLocalName().equals("command")) {

					String commandName = child.getAttributes().getNamedItem("name").getNodeValue();
					CommandReference command = new CommandReference(schemaAttrStr, clazzAttrStr, config, commandName, source, tt, entryPoint);
					lifecycleConfiguration.addCommand(command);
				}
			}
		}

		return lifecycleConfiguration;
	}

	private IRhenaEdge newEdge(ModuleIdentifier source, EExecutionType type, ModuleIdentifier target, ESelectionType selection) {

		IRhenaEdge edge = new RhenaEdge(source, selection, new EntryPoint(type, target));
		return edge;
	}

	private Document nodeToDocument(Node child) throws RhenaException {

		Document document = Utils.newEmptyDocument();
		Node importedNode = document.importNode(child, true);
		document.appendChild(importedNode);
		return document;
	}

	private void processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		EExecutionType dependencyType = Utils.valueOf(moduleChild.getLocalName());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();

		ModuleIdentifier moduleIdentifier = ModuleIdentifier.valueOf(dependencyTargetModuleIdentifier);
		ESelectionType traverseType = ESelectionType.SCOPE;

		if (moduleChild.getAttributes().getNamedItem("traverse") != null) {

			traverseType = ESelectionType.valueOf(moduleChild.getAttributes().getNamedItem("traverse").getNodeValue().toUpperCase());
		}

		IRhenaEdge edge = newEdge(module.getIdentifier(), dependencyType, moduleIdentifier, traverseType);
		module.addDependency(edge);
	}

	public RhenaModule getModule() {

		return module;
	}
}
