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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.IPeerServiceController;
import portal.services.IService;
import portal.services.IServiceConfig;
import portal.services.ServiceException;
import portal.services.registry.RegistryException;

public class RegistryController implements IPeerServiceController
{
	private static final Log LOGGER = LogFactory.getLog(RegistryController.class);

	public void init(IServiceConfig serviceConfig) throws RegistryException
	{
		String registryFilename = serviceConfig.getAttributeValue("storage.filename");

		LOGGER.debug("Configuring registry using storage file: "
				+ registryFilename);

		RegistryFile registryFile = new RegistryFile(registryFilename);

		_registry = new FileRegistry(registryFile);
	}

	public void reload() throws RegistryException
	{
		_registry.reload();
	}

	public void start() throws RegistryException
	{
	}

	public void stop() throws RegistryException
	{
	}

	public IService getService() throws ServiceException
	{
		return _registry;
	}

	private FileRegistry _registry;
}