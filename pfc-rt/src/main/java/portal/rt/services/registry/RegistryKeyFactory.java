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

import portal.services.registry.BooleanKey;
import portal.services.registry.IntegerKey;
import portal.services.registry.LongKey;
import portal.services.registry.RegistryException;
import portal.services.registry.RegistryKey;
import portal.services.registry.StringKey;

public final class RegistryKeyFactory
{
	public static RegistryKey createRegistryKey(String pName, String type, String value)
			throws RegistryException
	{
		if (type.equals(StringKey.TYPE))
		{
			return new StringKey(pName, value);
		}
		else if (type.equals(BooleanKey.TYPE))
		{
			return new BooleanKey(pName, value);
		}
		else if (type.equals(IntegerKey.TYPE))
		{
			return new IntegerKey(pName, value);
		}
		if (type.equals(LongKey.TYPE))
		{
			return new LongKey(pName, value);
		}
		else
		{
			throw new RegistryException("Unknown type: " + type);
		}
	}

	private RegistryKeyFactory()
	{
	}
}