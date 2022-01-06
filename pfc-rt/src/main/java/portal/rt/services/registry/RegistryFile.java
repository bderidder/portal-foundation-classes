/*
 * Copyright 2000 - 2004,  Bavo De Ridder
 * 
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 * 
 * http://www.gnu.org/licenses/gpl.html
 */

package portal.rt.services.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import portal.services.registry.RegistryException;
import portal.services.registry.RegistryKey;

public class RegistryFile
{
	private static final Log LOGGER = LogFactory.getLog(RegistryFile.class);

	public RegistryFile(String fileName)
	{
		_dataFile = fileName;
	}

	public Map<String,RegistryKey> loadRegistry() throws RegistryException
	{
		try
		{
			Map<String,RegistryKey> keys = new HashMap<String,RegistryKey>();

			File file = new File(_dataFile);

			_lastModified = file.lastModified();

			InputStream stream = new FileInputStream(file);

			loadRegistry(keys, stream);

			stream.close();

			return keys;
		}
		catch (Exception e)
		{
			LOGGER.error("Could not load registry", e);

			throw new RegistryException("Could not load registry ["
					+ e.getMessage() + "]");
		}
	}

	private void loadRegistry(Map<String,RegistryKey> keys, InputStream stream)
			throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);

		DocumentBuilder docBuilder = dbf.newDocumentBuilder();

		docBuilder.setEntityResolver(new RegistryDTDEntityResolver());

		InputSource inputSource = new InputSource(stream);

		Document doc = docBuilder.parse(inputSource);

		parse(keys, doc.getDocumentElement());
	}

	private void parse(Map<String,RegistryKey> keys, Element elem) throws Exception
	{
		NodeList children = elem.getChildNodes();

		for (int i = 0; i != children.getLength(); i++)
		{
			Node currentNode = children.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element currentElem = (Element) currentNode;
				String tagName = currentElem.getTagName();

				if (tagName.equals(REGISTRY_KEY))
				{
					processRegistryKey(keys, currentElem);
				}
			}
		}
	}

	private void processRegistryKey(Map<String,RegistryKey> keys, Element elem)
			throws Exception
	{
		String name = null;
		String type = null;
		String value = null;

		NodeList children = elem.getChildNodes();

		for (int i = 0; i != children.getLength(); i++)
		{
			Node currentNode = children.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element currentElem = (Element) currentNode;
				String tagName = currentElem.getTagName();

				if (tagName.equals(REGISTRY_KEY_NAME) && name == null)
				{
					name = processNameElement(currentElem);
				}
				else if (tagName.equals(REGISTRY_KEY_TYPE) && type == null)
				{
					type = processTypeElement(currentElem);
				}
				else if (tagName.equals(REGISTRY_KEY_VALUE) && value == null)
				{
					value = processValueElement(currentElem);
				}
			}
		}

		if ((name == null) || (type == null) || (value == null))
		{
			LOGGER.error("Name, type or value were null");

			return;
		}

		RegistryKey key = null;

		key = RegistryKeyFactory.createRegistryKey(name, type, value);

		keys.put(name, key);
	}

	private String processNameElement(Element elem)
	{
		elem.normalize();

		return elem.getFirstChild().getNodeValue();
	}

	private String processTypeElement(Element elem)
	{
		elem.normalize();

		return elem.getFirstChild().getNodeValue();
	}

	private String processValueElement(Element elem)
	{
		elem.normalize();

		return elem.getFirstChild().getNodeValue();
	}

	public static Integer parseStringToInteger(String str)
	{
		try
		{
			return new Integer(str);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public boolean isModified() throws RegistryException
	{
		try
		{
			File file = new File(_dataFile);

			return file.lastModified() > _lastModified;
		}
		catch (Exception e)
		{
			throw new RegistryException(
					"Could not check last modified date on the registry file ["
							+ e.getMessage() + "]");
		}
	}

	private long _lastModified;

	private String _dataFile = null;

	private static final String REGISTRY_KEY = "registry-key";
	private static final String REGISTRY_KEY_NAME = "name";
	private static final String REGISTRY_KEY_TYPE = "type";
	private static final String REGISTRY_KEY_VALUE = "value";
}