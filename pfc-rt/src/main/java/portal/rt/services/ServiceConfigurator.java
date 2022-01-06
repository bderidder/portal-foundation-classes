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
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.ServiceException;

public class ServiceConfigurator extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final Log LOGGER = LogFactory.getLog(ServiceConfigurator.class);
    private static final String SERVICES_RESOURCE_PATH = "/WEB-INF/services/";

    @Override
    public void init() throws ServletException
    {
        try
        {
            LOGGER.debug("Configuring services from " + SERVICES_RESOURCE_PATH);

            ServiceManager serviceManager = new ServiceManager();

            ServletContext context = getServletConfig().getServletContext();

            Set<?> services = context.getResourcePaths(SERVICES_RESOURCE_PATH);

            configureServices(services, serviceManager);

            //ServiceLocator.setServiceLocator(serviceManager);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            throw new ServletException(
                    "could not load and initialize all services");
        }
    }

    private void configureServices(Set<?> services, ServiceManager serviceManager)
            throws Exception
    {
        ServletContext context = getServletConfig().getServletContext();

        Iterator<?> it = services.iterator();

        while (it.hasNext())
        {
            String resourcePath = (String) it.next();

            if (resourcePath.endsWith("-service.xml"))
            {
                LOGGER.debug("Configuring service from " + resourcePath);

                InputStream xmlStream = context.getResourceAsStream(resourcePath);

                configureService(resourcePath, xmlStream, serviceManager);
            }
        }
    }

    private void configureService(String resourcePath, InputStream xmlStream,
            ServiceManager serviceManager)
    {
        try
        {
            serviceManager.configureFromStream(
                    getServletConfig().getServletContext(), xmlStream);
        }
        catch (ServiceException e)
        {
            LOGGER.error("Unable to parse service configuration from " + resourcePath, e);
        }
    }
}
