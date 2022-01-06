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

package portal.rt.security.simple;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.security.ISecurityService;
import portal.services.registry.RegistryException;
import portal.services.registry.StringKey;

/**
 * <p>This class uses the following registry keys and default values:</p>
 * <table border="1" cellspacing="3" cellpadding="2">
 * <tr>
 *   <td><b>Name</b></td>
 *   <td><b>Type</b></td>
 *   <td><b>Default</b></td>
 *   <td><b>Description</b></td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.simple.Username</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>The username of the simple user</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.simple.Password</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>The password of the simple user</td>
 * </tr>
 * </table>
 * 
 * @author Bavo De Ridder <http://bavo.coderspotting.org/)
 */
public class SimpleSecurityService implements ISecurityService
{
	private static final Log LOGGER = LogFactory.getLog(SimpleSecurityService.class);

	private static final String REG_KEY_PASSWORD = "portal.security.sp.simple.Password";
	private static final String REG_KEY_USERNAME = "portal.security.sp.simple.Username";

	public SimpleSecurityService()
	{
	}

	public boolean verify(String username, String password)
			throws SecurityException
	{
		try
		{
			if ((username != null) && username.equals(getUsername()))
			{
				return ((password != null) && password.equals(getPassword()));
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("Could not verify credentials", e);

			throw new SecurityException("Could not verify credentials: "
					+ e.getMessage());
		}
	}

	public Principal login(String username, String password)
			throws SecurityException
	{
		if (!verify(username, password))
		{
			throw new SecurityException("User not known or wrong password.");
		}

		return new SimplePrincipal(username);
	}

	private String getUsername() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_USERNAME);
	}

	private String getPassword() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_PASSWORD);
	}

}