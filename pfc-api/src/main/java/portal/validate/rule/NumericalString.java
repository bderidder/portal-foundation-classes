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

import portal.model.IPropertyModel;
import portal.validate.IValidationContext;

public class NumericalString extends ValidationRule
{
	private static final long serialVersionUID = 1L;

	public NumericalString(IPropertyModel<String> stringModel)
	{
		_model = stringModel;
	}

	protected boolean validateRule(IValidationContext ctx)
	{
		String str = _model.getValue();

		boolean numerical = true;
		for (int i = 0; i != str.length(); i++)
		{
			numerical = numerical && Character.isDigit(str.charAt(i));
		}

		return numerical;
	}

	private IPropertyModel<String> _model;
}