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

import java.security.Principal;

public class LDAPPrincipal implements Principal
{
	public LDAPPrincipal(String authName)
	{
		this._authName = authName;
	}

	public final String getName()
	{
		return _authName;
	}
	
	public boolean equals(Object pObject)
	{
		if (pObject instanceof LDAPPrincipal)
		{
			LDAPPrincipal otherPrincipal = (LDAPPrincipal) pObject;

			return otherPrincipal._authName.equals(_authName);
		}
		else
		{
			return false;
		}
	}
	
	public int hashCode()
	{
		return _authName.hashCode();
	}
	
	private String _authName;
}