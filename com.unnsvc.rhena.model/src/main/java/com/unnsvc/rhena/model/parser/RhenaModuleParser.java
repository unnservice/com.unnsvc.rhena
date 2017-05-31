
package com.unnsvc.rhena.model.parser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.ILifecycleSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.common.utils.MiscUtils;
import com.unnsvc.rhena.model.EntryPoint;
import com.unnsvc.rhena.model.RhenaEdge;
import com.unnsvc.rhena.model.RhenaModule;
import com.unnsvc.rhena.model.lifecycle.CommandSpec;
import com.unnsvc.rhena.model.lifecycle.ContextSpec;
import com.unnsvc.rhena.model.lifecycle.GeneratorSpec;
import com.unnsvc.rhena.model.lifecycle.LifecycleSpec;
import com.unnsvc.rhena.model.lifecycle.ProcessorSpec;

/**
 * @TODO XSD validation once the settings.xml format is established
 * @author noname
 *
 */
public class RhenaModuleParser {

	private RhenaModule module;

	public RhenaModuleParser(RepositoryIdentifier repositoryIdentifier, ModuleIdentifier identifier, URI moduleDescriptorLocation) throws RhenaException {

		try {
			this.module = new RhenaModule(identifier, repositoryIdentifier);

			parse(moduleDescriptorLocation);
		} catch (Exception re) {
			throw new RhenaException(re);
		}
	}

	public void parse(URI uri) throws Exception {

		Document document = parseAndValidateDocument(uri);

		NodeList children = document.getChildNodes();
		Node moduleNode = children.item(0);

		if (moduleNode.getLocalName().equals("module")) {
			module.setFramework(false);
		} else if (moduleNode.getLocalName().equals("framework")) {
			module.setFramework(true);
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
					IRhenaEdge edge = processDepenencyNode(moduleChild);
					module.addDependency(edge);
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
		Schema schema = schemaFactory.newSchema(RhenaConstants.class.getClassLoader().getResource("META-INF/schema/module.xsd"));

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

			ILifecycleSpec defaultConfig = new LifecycleSpec();
			module.setLifecycleConfiguration(defaultConfig);
		} else {
			ILifecycleSpec lifecycleConfiguration = new LifecycleSpec(lifecycleAttrNode.getNodeValue());
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
						ILifecycleSpec declaredConfiguration = processLifecycleNode(metaChild);
						module.addDeclaredConfiguration(declaredConfiguration);
					}
				}
			}
		}
	}

	private ILifecycleSpec processLifecycleNode(Node lifecycleNode) throws RhenaException {

		String lifecycleName = lifecycleNode.getAttributes().getNamedItem("name").getNodeValue();
		LifecycleSpec lifecycleConfiguration = new LifecycleSpec(lifecycleName);

		NodeList children = lifecycleNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				/**
				 * This is for lifecycle dependencies
				 */
				if (child.getNamespaceURI().equals(RhenaConstants.NS_RHENA_DEPENDENCY)) {

					IRhenaEdge edge = processDepenencyNode(child);
					lifecycleConfiguration.addLifecycleDependency(edge);

				} else {

					/**
					 * This is for the list of processors
					 */

					String clazzAttrStr = child.getAttributes().getNamedItem("class").getNodeValue();
					String schemaAttrStr = null;

					// if (child.getAttributes().getNamedItem("schema") != null)
					// {
					// schemaAttrStr =
					// child.getAttributes().getNamedItem("schema").getNodeValue();
					// }

					EExecutionType et = EExecutionType.MAIN;
					
					// these are not added anymore to processor constructor
					ESelectionType tt = ESelectionType.SCOPE;
					ModuleIdentifier source = module.getIdentifier();

					// processors have no module= attributes anymore as they are
					// now declared in dependencies to the processor
					// String moduleAttrStr =
					// child.getAttributes().getNamedItem("module").getNodeValue();
					// ModuleIdentifier target =
					// ModuleIdentifier.valueOf(moduleAttrStr);
					// IEntryPoint entryPoint = new EntryPoint(et, target);

					Document processorConfig = null;
					List<IRhenaEdge> processorDeps = new ArrayList<IRhenaEdge>();

					/**
					 * Processor children
					 */
					NodeList processorChildren = child.getChildNodes();
					for (int pci = 0; pci < processorChildren.getLength(); pci++) {

						Node processorChild = processorChildren.item(pci);

						if (processorChild.getNodeType() == Node.ELEMENT_NODE) {

							if (processorChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_DEPENDENCY)) {
								IRhenaEdge edge = processDepenencyNode(processorChild);
								processorDeps.add(edge);
							} else if (processorChild.getNodeName().equals("configuration")) {
								// is configuration node
								// Make a document out of the entire
								// configuration node content
								processorConfig = nodeToDocument(processorChild);
							}
						}
					}

					/**
					 * @TODO perform caching on edge
					 */

					if (child.getLocalName().equals("context")) {

						ContextSpec configurator = new ContextSpec(schemaAttrStr, clazzAttrStr, processorConfig, processorDeps);
						lifecycleConfiguration.setContext(configurator);
					} else if (child.getLocalName().equals("processor")) {

						ProcessorSpec processor = new ProcessorSpec(schemaAttrStr, clazzAttrStr, processorConfig, processorDeps);
						lifecycleConfiguration.addProcessor(processor);
					} else if (child.getLocalName().equals("generator")) {

						GeneratorSpec generator = new GeneratorSpec(schemaAttrStr, clazzAttrStr, processorConfig, processorDeps);
						lifecycleConfiguration.setGenerator(generator);
					} else if (child.getLocalName().equals("command")) {

						String commandName = child.getAttributes().getNamedItem("name").getNodeValue();
						CommandSpec command = new CommandSpec(schemaAttrStr, clazzAttrStr, processorConfig, commandName, processorDeps);
						lifecycleConfiguration.addCommand(command);
					}
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

		Document document = MiscUtils.newEmptyDocument();
		Node importedNode = document.importNode(child, true);
		document.appendChild(importedNode);
		return document;
	}

	private IRhenaEdge processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		EExecutionType dependencyType = MiscUtils.valueOf(moduleChild.getLocalName());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();

		ModuleIdentifier moduleIdentifier = ModuleIdentifier.valueOf(dependencyTargetModuleIdentifier);
		ESelectionType traverseType = ESelectionType.SCOPE;

		if (moduleChild.getAttributes().getNamedItem("traverse") != null) {

			traverseType = ESelectionType.valueOf(moduleChild.getAttributes().getNamedItem("traverse").getNodeValue().toUpperCase());
		}

		IRhenaEdge edge = newEdge(module.getIdentifier(), dependencyType, moduleIdentifier, traverseType);
		return edge;
	}

	public RhenaModule getModule() {

		return module;
	}
}
