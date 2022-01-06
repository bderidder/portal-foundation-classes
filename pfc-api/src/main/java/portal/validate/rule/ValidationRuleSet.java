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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import portal.validate.IValidatable;
import portal.validate.IValidationContext;

public class ValidationRuleSet implements IValidatable
{
	private static final long serialVersionUID = 1L;

	public ValidationRuleSet()
	{
		_childRules = new LinkedList<IValidatable>();
	}

	public void addValidationRule(IValidatable validatable)
	{
		_childRules.add(validatable);
	}

	public void validate(IValidationContext ctx)
	{
		Iterator<IValidatable> it = _childRules.iterator();

		while (it.hasNext())
		{
			IValidatable rule = it.next();

			rule.validate(ctx);
		}
	}

	private List<IValidatable> _childRules;
}