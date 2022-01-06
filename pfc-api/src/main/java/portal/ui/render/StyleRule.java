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
package portal.ui.render;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class StyleRule implements Serializable
{
    private static final long serialVersionUID = 1L;
    private HashMap<String, String> _styles;
    private String _styleClass;

    public StyleRule(String styleClass)
    {
        this._styles = new HashMap<>();

        _styleClass = styleClass;
    }

    public StyleRule()
    {
        this(null);
    }

    public String getStyleClass()
    {
        return _styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public StyleRule addStyle(String styleName, String styleValue)
    {
        if (_styles.containsKey(styleName))
        {
            _styles.remove(styleName);
        }

        _styles.put(styleName, styleValue);

        return this;
    }

    public void removeStyle(String styleName)
    {
        if (_styles.containsKey(styleName))
        {
            _styles.remove(styleName);
        }
    }

    public String toStyleValue()
    {
        String styleValue = new String();

        Iterator<String> styleNameIt = _styles.keySet().iterator();
        while (styleNameIt.hasNext())
        {
            String name = styleNameIt.next();
            String value = _styles.get(name);

            styleValue = styleValue + name + ": " + value + ";";
        }

        return styleValue;
    }
}
