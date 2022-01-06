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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultListDataModel<DataType> implements IListDataModel<DataType>
{
	private static final long serialVersionUID = 1L;

	public DefaultListDataModel()
	{
		_values = new ArrayList<DataType>();
	}

	public Iterator<DataType> getDataIterator()
	{
		return _values.iterator();
	}

	public void addElement(DataType value)
	{
		_values.add(value);
	}

	public void addElementAt(int index, DataType value)
	{
		_values.add(index, value);
	}

	public void removeElement(int index)
	{
		_values.remove(index);
	}

	public void clear()
	{
		_values.clear();
	}

	public DataType getElementAt(int index)
	{
		return _values.get(index);
	}

	public int getSize()
	{
		return _values.size();
	}

	private List<DataType> _values;
}