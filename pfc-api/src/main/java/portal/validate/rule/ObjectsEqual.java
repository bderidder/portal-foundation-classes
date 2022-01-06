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

package portal.validate.rule;

import portal.validate.IValidationContext;

public class ObjectsEqual extends ValidationRule
{
	private static final long serialVersionUID = 1L;

	public ObjectsEqual(Object o1, Object o2)
	{
		this._o1 = o1;
		this._o2 = o2;
	}

	protected boolean validateRule(IValidationContext ctx)
	{
		if (_o1 == null && _o2 == null)
		{
			return false;
		}
		else if (_o1.equals(_o2))
		{
			return true;
		}

		return false;
	}

	private Object _o1, _o2;
}