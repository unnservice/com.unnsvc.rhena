
package com.unnsvc.rhena.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentHelper {

	public static List<Node> childNodes(Document document) {

		NodeList children = document.getChildNodes();
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public static List<Node> childNodes(Node document) {

		NodeList children = document.getChildNodes();
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				nodes.add(node);
			}
		}
		return nodes;
	}

}
