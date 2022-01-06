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
package portal.rt.ui.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import portal.services.resource.IResource;

public final class StaticResourceMap implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static StaticResourceMap staticResourceMap = null;

    public static StaticResourceMap getStaticResourceMap()
    {
        if (staticResourceMap == null)
        {
            staticResourceMap = new StaticResourceMap();
        }

        return staticResourceMap;
    }

    public String addStaticResource(IResource resource)
    {
        String key = Integer.toString(resource.hashCode());

        if (!_resources.containsKey(key))
        {
            _resources.put(key, resource);
        }

        return key;
    }

    public IResource getStaticResource(String key)
    {
        return _resources.get(key);
    }

    private StaticResourceMap()
    {
        _resources = new HashMap<>();
    }
    private Map<String, IResource> _resources;
}
