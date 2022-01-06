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

package portal.ui.comp.list;

import java.io.Serializable;

/**
 * For lack of the enum type, a class that represents selection modes.
 */
public final class SelectionMode implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final SelectionMode SINGLE_SELECTION = new SelectionMode(0);
	public static final SelectionMode MULTIPLE_SELECTION = new SelectionMode(1);

	public boolean equals(Object obj)
	{
		if (obj instanceof SelectionMode)
		{
			return ((SelectionMode) obj)._selectionMode.equals(this._selectionMode);
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return _selectionMode.hashCode();
	}

	private SelectionMode(int selectionMode)
	{
		this._selectionMode = new Integer(selectionMode);
	}

	private Integer _selectionMode;
}