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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.eventbus.EventBus;
import portal.services.IService;
import portal.services.ServiceException;
import portal.services.ServiceLocator;

public class ServiceManager extends ServiceLocator
{
	private static final Log LOGGER = LogFactory.getLog(ServiceManager.class);

	public ServiceManager() throws ServiceException
	{
		_eventBus = new EventBus();
		_services = new ArrayList<ServiceController>();

		ServiceLocator.setServiceLocator(this);
	}

	public EventBus getServicesEventBus()
	{
		return _eventBus;
	}

	public ServiceController getInstanceManagerForService(Class<?> interfaceRequired)
			throws ServiceException
	{
		LOGGER.debug("Locate request for service: "
				+ interfaceRequired.getName());

		Iterator<ServiceController> it = _services.iterator();

		while (it.hasNext())
		{
			ServiceController serviceInstance = it.next();

			if (serviceInstance.implementsInterface(interfaceRequired))
			{
				LOGGER.debug("Service implementation found: " + serviceInstance);

				return serviceInstance;
			}
		}

		return null;
	}

	public IService locateService(Class<?> interfaceRequired)
			throws ServiceException
	{
		ServiceController instanceManager = getInstanceManagerForService(interfaceRequired);

		if (instanceManager != null)
		{
			return instanceManager.getService();
		}
		else
		{
			return null;
		}
	}

	public void configureFromStream(ServletContext context,
			InputStream xmlStream) throws ServiceException
	{
		ServiceXMLConfiguration configuration = new ServiceXMLConfiguration(
				context, xmlStream);

		ServiceController instanceManager = new ServiceController(this,
				configuration);

		_services.add(instanceManager);

		_eventBus.postEvent(new NewServiceEvent(this,
				configuration.getInterfaceClasses()));

		instanceManager.init();
	}

	private EventBus _eventBus;

	private List<ServiceController> _services;
}