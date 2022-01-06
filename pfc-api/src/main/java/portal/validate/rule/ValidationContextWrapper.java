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

public class ValidationContextWrapper implements IValidationContext
{
	public ValidationContextWrapper(IValidationContext parentContext)
	{
		_parentContext = parentContext;

		_valid = true;
	}

	public boolean isValid()
	{
		return _valid;
	}

	public void validationFailed(String description)
	{
		_valid = false;

		if (_parentContext != null)
		{
			_parentContext.validationFailed(description);
		}
	}

	public void validationPassed()
	{
		if (_parentContext != null)
		{
			_parentContext.validationPassed();
		}
	}

	private IValidationContext _parentContext;
	private boolean _valid;
}