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

package portal.ui.comp.tabbed;

import portal.ui.Component;
import portal.validate.IValidatable;
import portal.validate.IValidationContext;

public class SimpleTab implements Tab, IValidatable
{
	private static final long serialVersionUID = 1L;
	
	public SimpleTab(String title, Component<?> component)
	{
		this._title = title;
		this._component = component;
		this._enabled = true;
	}

	public String getTitle()
	{
		return _title;
	}

	public Component<?> getComponent()
	{
		return _component;
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this._enabled = enabled;
	}
	
	public void beforeActive()
	{
	}
	
	public void afterActive()
	{
	}

	public void validate(IValidationContext validationContext)
	{
		if (_component instanceof IValidatable)
		{
			((IValidatable) _component).validate(validationContext);
		}
	}

	private boolean _enabled;
	private String _title;
	private Component<?> _component;
}