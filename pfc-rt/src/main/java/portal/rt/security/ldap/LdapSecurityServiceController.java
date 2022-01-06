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

package portal.rt.security.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.IPeerServiceController;
import portal.services.IService;
import portal.services.IServiceConfig;
import portal.services.ServiceException;
import portal.services.registry.RegistryException;

public class LdapSecurityServiceController implements IPeerServiceController
{
	private static final Log LOGGER = LogFactory.getLog(LdapSecurityServiceController.class);

	public void init(IServiceConfig serviceConfig) throws ServiceException
	{
		LOGGER.debug("Configuring ldap security service");

		_ldapSecurityServiceImpl = new LdapSecurityService();
	}

	public void reload() throws RegistryException
	{
	}

	public void start() throws RegistryException
	{
	}

	public void stop() throws RegistryException
	{
	}

	public IService getService() throws ServiceException
	{
		return _ldapSecurityServiceImpl;
	}

	private LdapSecurityService _ldapSecurityServiceImpl;
}