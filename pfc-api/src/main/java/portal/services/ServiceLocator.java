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

package portal.services;


/**
 * <p>
 *  This class allows you to locate a service based on the interface it needs to implement.
 * </p>
 * 
 * <p>
 *  Example:
 * </p>
 * 
 * <code>
 *  Registry registry = (Registry) ServiceLocator.getServiceLocator().locateService(portal.registry.Registry.class);
 * </code>
 * 
 * @author Bavo De Ridder < bavo AT coderspotting DOT org >
 */
public abstract class ServiceLocator
{
	private static ServiceLocator serviceLocator = null;

	protected static void setServiceLocator(ServiceLocator serviceLocator)
			throws ServiceException
	{
		if (ServiceLocator.serviceLocator != null)
		{
			throw new ServiceException(
					"There is already a service locator registered");
		}

		ServiceLocator.serviceLocator = serviceLocator;
	}

	/**
	 * Returns an instance of the ServiceLocator that has been initialized
	 * by the framework.
	 * 
	 * @return the singleton instance of the ServiceLocator
	 */
	public static final ServiceLocator getServiceLocator()
	{
		return serviceLocator;
	}

	/**
	 * This method will search for a registered service that implements a certain interface. When no service
	 * is found, null is returned. When multiple services implement the requested interface, only one will
	 * be returned. Which one depends on the internal workings of the engine and might change.
	 * 
	 * @param interfaceRequired The Class instance of the interface the service should implement
	 * 
	 * @return an instance of the service implementing the requested interface or null when none found
	 * 
	 * @throws ServiceException thrown when the framework was unable to search for a service
	 */
	public abstract IService locateService(Class<?> interfaceRequired)
			throws ServiceException;
}