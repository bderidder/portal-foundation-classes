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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class DefaultSelectionModel implements ISelectionModel
{
    private static final long serialVersionUID = 1L;
    private final SortedSet<Integer> _selectionSet = new TreeSet<>();
    private SelectionMode _selectionMode;

    public DefaultSelectionModel(SelectionMode selectionMode)
    {
        this._selectionMode = selectionMode;
    }

    @Override
    public int getFirstSelected()
    {
        int selectedIndex = -1;

        Iterator<Integer> it = _selectionSet.iterator();

        while (it.hasNext())
        {
            Integer iIndex = it.next();

            if (iIndex.intValue() > selectedIndex)
            {
                selectedIndex = iIndex.intValue();
            }
        }

        return selectedIndex;
    }

    @Override
    public void clearSelection()
    {
        _selectionSet.clear();
    }

    @Override
    public void setSelected(int index, boolean selected)
    {
        Integer iIndex = new Integer(index);

        if (!selected)
        {
            _selectionSet.remove(iIndex);
        }
        else if (!_selectionSet.contains(iIndex))
        {
            if (_selectionMode.equals(SelectionMode.SINGLE_SELECTION))
            {
                clearSelection();
            }

            _selectionSet.add(iIndex);
        }
    }

    @Override
    public boolean isSelected(int index)
    {
        Integer iIndex = new Integer(index);

        return _selectionSet.contains(iIndex);
    }

    @Override
    public Iterator<Integer> iterator()
    {
        TreeSet<Integer> clonedSet = null;

        synchronized (_selectionSet)
        {
            clonedSet = new TreeSet<>(_selectionSet);
        }

        return clonedSet.iterator();
    }

    @Override
    public SelectionMode getSelectionMode()
    {
        return _selectionMode;
    }
}
