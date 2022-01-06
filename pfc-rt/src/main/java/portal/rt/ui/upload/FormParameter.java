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
package portal.rt.ui.upload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import portal.rt.servlet.PortalProcessException;

public class FormParameter implements RequestParameter
{
    public FormParameter(String name) throws PortalProcessException
    {
        this._name = name;

        _values = new ArrayList<>();
    }

    @Override
    public String getName()
    {
        return _name;
    }

    public String getValue() throws PortalProcessException
    {
        if (_values.size() < 1)
        {
            return null;
        }
        else
        {
            return _values.get(0);
        }
    }

    public Collection<String> getValues() throws PortalProcessException
    {
        if (_values.size() < 1)
        {
            return null;
        }
        else
        {
            return _values;
        }
    }

    public void addValue(String value)
    {
        _values.add(value);
    }
    private String _name;
    private List<String> _values;
}
