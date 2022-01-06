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
package portal.rt.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import portal.ui.IAction;
import portal.ui.RandomIDGenerator;

public final class ActionMap implements Serializable
{
    private static final long serialVersionUID = 1L;

    public ActionMap()
    {
        _actions = new HashMap<>();
    }

    public String addAction(IAction action)
    {
        String key = RandomIDGenerator.getRandomID();

        _actions.put(key, action);

        return key;
    }

    public Iterator<String> getKeyIterator()
    {
        return _actions.keySet().iterator();
    }

    public IAction getAction(String key)
    {
        return _actions.get(key);
    }

    public void reset()
    {
        _actions.clear();
    }
    private Map<String, IAction> _actions;
}
