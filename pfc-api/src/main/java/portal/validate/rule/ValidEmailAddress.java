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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import portal.model.IPropertyModel;
import portal.validate.IValidationContext;

/*
 * This validator only checks the syntax of an email address
 *
 */
public class ValidEmailAddress extends ValidationRule
{
	private static final long serialVersionUID = 1L;
	
	public ValidEmailAddress(IPropertyModel<String> stringModel)
	{
		_model = stringModel;
	}

	protected boolean validateRule(IValidationContext ctx)
	{
		String str = _model.getValue();

		try
		{
			new InternetAddress(str);

			return true;
		}
		catch (AddressException e)
		{
			return false;
		}
	}

	private IPropertyModel<String> _model;
}