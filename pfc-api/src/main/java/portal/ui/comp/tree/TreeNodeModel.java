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
package portal.ui.comp.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import portal.model.IModel;

public class TreeNodeModel implements IModel
{
    private static final long serialVersionUID = 1L;
    private String _name;
    private List<TreeNodeModel> _children;
    private final List<TreeNodeEventListener> _listeners = new LinkedList<>();
    private boolean _opened;

    public TreeNodeModel()
    {
        _children = new LinkedList<>();

        _opened = false;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        this._name = name;
    }

    public boolean isOpened()
    {
        return _opened;
    }

    public void setOpened(boolean opened)
    {
        this._opened = opened;

        if (isOpened())
        {
            notifyOpened();
        }
        else
        {
            notifyClosed();
        }
    }

    public Collection<TreeNodeModel> getChildren()
    {
        return _children;
    }

    public boolean hasChildNodes()
    {
        return !_children.isEmpty();
    }

    public void addChildNode(TreeNodeModel childNode)
    {
        _children.add(childNode);
    }

    public void removeAllChildNodes()
    {
        _children.clear();
    }

    public void addTreeNodeEventListener(TreeNodeEventListener listener)
    {
        _listeners.add(listener);
    }

    // package visibility
    void notifySelected()
    {
        List<TreeNodeEventListener> tempListeners = null;

        synchronized (_listeners)
        {
            tempListeners = new LinkedList<>(_listeners);
        }

        Iterator<TreeNodeEventListener> it = tempListeners.iterator();

        while (it.hasNext())
        {
            TreeNodeEventListener listener = it.next();

            listener.selected();
        }
    }

    private void notifyOpened()
    {
        List<TreeNodeEventListener> tempListeners = null;

        synchronized (_listeners)
        {
            tempListeners = new LinkedList<>(_listeners);
        }

        Iterator<TreeNodeEventListener> it = tempListeners.iterator();

        while (it.hasNext())
        {
            TreeNodeEventListener listener = it.next();

            listener.opened();
        }

        _opened = true;
    }

    private void notifyClosed()
    {
        LinkedList<TreeNodeEventListener> tempListeners = null;

        synchronized (_listeners)
        {
            tempListeners = new LinkedList<>(_listeners);
        }

        Iterator<TreeNodeEventListener> it = tempListeners.iterator();

        while (it.hasNext())
        {
            TreeNodeEventListener listener = it.next();

            listener.closed();
        }

        _opened = false;
    }
}
