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

package portal.services.registry;

import portal.services.ServiceException;
import portal.services.ServiceLocator;

public class StringKey extends RegistryKey
{
	public static final String TYPE = "String";

	public StringKey(String pName, String value) throws RegistryException
	{
		super(pName);
		
		this._value = value;
	}

	public Object getValue() throws RegistryException
	{
		return _value;
	}

	public String getString() throws RegistryException
	{
		return _value;
	}

	public static String getValue(String name) throws RegistryException
	{
		IRegistry registry = null;

		try
		{
			registry = (IRegistry) ServiceLocator.getServiceLocator()
					.locateService(IRegistry.class);
		}
		catch (ServiceException e)
		{
			throw new RegistryException("Could not locate Registry service", e);
		}

		RegistryKey key = registry.getRegistryKey(name);

		if (key == null)
		{
			throw new RegistryException("Registry key with name " + name
					+ " does not exist");
		}

		if (key instanceof StringKey)
		{
			return ((StringKey) key).getString();
		}
		else
		{
			throw new TypeMismatchException(
					"Key existed but had a wrong type: " + key);
		}
	}

	public static String getValue(String name, String defaultValue)
	{
		try
		{
			return StringKey.getValue(name);
		}
		catch (RegistryException e)
		{
			return defaultValue;
		}
	}

	private String _value;
}