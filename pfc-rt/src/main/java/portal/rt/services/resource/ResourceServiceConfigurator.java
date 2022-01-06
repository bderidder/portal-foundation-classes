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
package portal.rt.services.resource;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import portal.services.resource.ResourceServiceException;

public class ResourceServiceConfigurator
{
    private static final Log LOGGER = LogFactory.getLog(ResourceServiceConfigurator.class);

    public ResourceServiceConfigurator()
    {
    }

    public void configure(ResourceServiceImpl resourceService,
            InputStream stream) throws ResourceServiceException
    {
        Document document = null;

        try
        {
            SAXReader reader = new SAXReader();

            LOGGER.info("Stream for config file is " + stream);

            document = reader.read(stream);
        }
        catch (DocumentException e)
        {
            throw new ResourceServiceException(
                    "could not parse configuration document", e);
        }

        configure(resourceService, document);
    }

    private void configure(ResourceServiceImpl resourceService,
            Document document) throws ResourceServiceException
    {
        Element root = document.getRootElement();

        for (Iterator<?> i = root.elementIterator("resource"); i.hasNext();)
        {
            Element resourceElement = (Element) i.next();

            configureResourceElement(resourceService, resourceElement);
        }
    }

    private void configureResourceElement(ResourceServiceImpl resourceService,
            Element resourceElement) throws ResourceServiceException
    {
        String classname = resourceElement.elementText("class");

        Properties properties = new Properties();

        for (Iterator<?> i = resourceElement.elementIterator("property"); i.hasNext();)
        {
            Element propertyElement = (Element) i.next();

            String name = propertyElement.attributeValue("name");
            String value = propertyElement.getText();

            properties.put(name, value);
        }

        configureResourceElement(resourceService, classname, properties);
    }

    private void configureResourceElement(ResourceServiceImpl resourceService,
            String classname,
            Properties properties) throws ResourceServiceException
    {
        LOGGER.debug("Configuring resource loader " + classname);

        IResourceLoader resourceLoader;

        try
        {
            Class<?> resourceLoaderClass = resolveClass(classname);

            resourceLoader = (IResourceLoader) resourceLoaderClass.newInstance();
        }
        catch (ResourceServiceException | InstantiationException | IllegalAccessException e)
        {
            throw new ResourceServiceException(
                    "Could not load resource loader class " + classname, e);
        }

        try
        {
            resourceLoader.init(properties);

            resourceService.addResourceLoader(resourceLoader);
        }
        catch (ResourceServiceException e)
        {
            LOGGER.error("Could not initialize resource loader '" + classname
                    + "' " + debugProperties(properties), e);
        }
    }

    private String debugProperties(Properties properties)
    {
        StringBuilder buffer = new StringBuilder();

        buffer.append("{Properties ");

        Iterator<Object> keyIt = properties.keySet().iterator();
        while (keyIt.hasNext())
        {
            String key = (String) keyIt.next();
            String value = (String) properties.get(key);

            buffer.append(MessageFormat.format("['{0}':'{1}']", key, value));
        }

        buffer.append("}");

        return buffer.toString();
    }

    private Class<?> resolveClass(String className)
            throws ResourceServiceException
    {
        try
        {
            return Class.forName(className);
        }
        catch (Exception e)
        {
            throw new ResourceServiceException("Could not resolve class for "
                    + className, e);
        }
    }
}
