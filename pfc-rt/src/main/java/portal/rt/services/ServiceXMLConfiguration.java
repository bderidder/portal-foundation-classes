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
package portal.rt.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import portal.services.IServiceConfig;
import portal.services.ServiceException;

public class ServiceXMLConfiguration implements IServiceConfig
{
	private static final Log LOGGER = LogFactory.getLog(ServiceXMLConfiguration.class);

	public ServiceXMLConfiguration(ServletContext context, InputStream xmlStream)
			throws ServiceException
	{
		this._context = context;

		try
		{
			SAXReader reader = new SAXReader();

			reader.setEntityResolver(new DTDEntityResolver());
			reader.setValidation(true);

			Document document = reader.read(xmlStream);

			parseDocument(document);
		}
		catch (DocumentException e)
		{
			LOGGER.error("Unable to parse service xml stream", e);

			throw new ServiceException(
					"Unable to parse service configuration file");
		}
	}

	public String getServiceName()
	{
		return _serviceName;
	}

	public Class<?>[] getInterfaceClasses()
	{
		return _interfaceClasses;
	}

	public Class<?> getControllerClass()
	{
		return _controllerClass;
	}

	@Override
	public Set<String> getAttributeNames()
	{
		return _attributes.keySet();
	}

	@Override
	public String getAttributeValue(String name)
	{
		if (_attributes.containsKey(name))
		{
			return _attributes.get(name);
		}
		else
		{
			return null;
		}
	}

	public Class<?>[] getDependencies()
	{
		return _dependencies;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getResourcePaths(String basePath) throws ServiceException
	{
		return _context.getResourcePaths(basePath);
	}

	@Override
	public InputStream getResourceAsStream(String resourcePath)
			throws ServiceException
	{
		return _context.getResourceAsStream(resourcePath);
	}

	private Class<?> resolveClass(String className) throws ServiceException
	{
		try
		{
			return Class.forName(className);
		}
		catch (Exception e)
		{
			throw new ServiceException("Could not resolve class for "
					+ className, e);
		}
	}

	private void parseDocument(Document document) throws ServiceException
	{
		Element root = document.getRootElement();

		_serviceName = root.element("name").getStringValue();

		_controllerClass = resolveClass(root.element("controller")
				.getStringValue());
		_interfaceClasses = readInterfaces(root.element("interfaces"));

		_attributes = new HashMap<>();

		for (Iterator<?> i = root.elementIterator("attribute"); i.hasNext();)
		{
			Element attributeElement = (Element) i.next();

			String name = attributeElement.element("name").getStringValue();
			String value = attributeElement.element("value").getStringValue();

			_attributes.put(name, value);
		}

		Element dependenciesElem = root.element("dependencies");

		_dependencies = readDependencies(dependenciesElem);
	}

	private Class<?>[] readInterfaces(Element interfacesElem)
			throws ServiceException
	{
		List<?> intList = interfacesElem.elements("interface");

		if (intList.isEmpty())
		{
			throw new ServiceException(
					"at least one interface should be declared");
		}

		Class<?>[] interfaces = new Class<?>[intList.size()];

		for (int i = 0; i != intList.size(); i++)
		{
			interfaces[i] = resolveClass(((Element) intList.get(i)).getStringValue());
		}

		return interfaces;
	}

	private Class<?>[] readDependencies(Element dependenciesElem)
			throws ServiceException
	{
		List<?> depList = dependenciesElem.elements("dependency");

		if (depList.isEmpty())
		{
			return null;
		}

		Class<?>[] deps = new Class<?>[depList.size()];

		for (int i = 0; i != depList.size(); i++)
		{
			deps[i] = resolveClass(((Element) depList.get(i)).getStringValue());
		}

		return deps;
	}
	private ServletContext _context;
	private String _serviceName;
	private Class<?> _controllerClass;
	private Class<?>[] _interfaceClasses;
	private Class<?>[] _dependencies;
	private HashMap<String, String> _attributes;
}
