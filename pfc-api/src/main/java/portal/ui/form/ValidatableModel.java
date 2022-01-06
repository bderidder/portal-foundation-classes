/*
 * Copyright 2000, 2013,  Bavo De Ridder
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
package portal.ui.form;

import portal.validate.IValidatable;
import portal.validate.IValidationContext;

public class ValidatableModel implements IValidatableUserInput
{
	private static final long serialVersionUID = 1L;
	private ValidationContext _validationContext;
	private IValidatable _validatable;

	public ValidatableModel()
	{
		_validationContext = new ValidationContext();
		_validatable = null;
	}

	public final void setValidationRule(IValidatable validationRule)
	{
		_validatable = validationRule;
	}

	@Override
	public final boolean isValid()
	{
		return _validationContext.isValid();
	}

	@Override
	public final void validate(IValidationContext validationContext)
	{
		IValidatable myValidatable = getValidatable();

		_validationContext = new ValidationContext(validationContext);

		if (myValidatable != null)
		{
			myValidatable.validate(_validationContext);
		}
	}

	private IValidatable getValidatable()
	{
		return _validatable;
	}
}
