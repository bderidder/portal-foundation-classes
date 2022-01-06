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

package portal.rt.services.monitor;

import portal.services.IPeerServiceController;
import portal.services.IService;
import portal.services.IServiceConfig;
import portal.services.ServiceException;
import portal.services.registry.RegistryException;

public class MonitorServiceController implements IPeerServiceController
{
	// private static final Log LOGGER =
	// LogFactory.getLog(MonitorServiceController.class);

	public void init(IServiceConfig serviceConfig) throws ServiceException
	{
		String monitorNamespace = serviceConfig
				.getAttributeValue("monitor.namespace");

		_monitorService = new MonitorServiceImpl(monitorNamespace);
	}

	public void reload() throws RegistryException
	{
	}

	public void start() throws RegistryException
	{
		_monitorService.start();
	}

	public void stop() throws RegistryException
	{
		_monitorService.stop();
	}

	public IService getService() throws ServiceException
	{
		return _monitorService;
	}

	private MonitorServiceImpl _monitorService;
}