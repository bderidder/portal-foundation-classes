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

package portal.eventbus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class EventBus implements Serializable
{
	private static final long serialVersionUID = 1L;

	public EventBus()
	{
		_listeners = new ArrayList<IEventBusListener>();
	}

	public void postEvent(EventObject event)
	{
		ArrayList<IEventBusListener> clonedList = null;

		synchronized (_listeners)
		{
			clonedList = new ArrayList<IEventBusListener> (_listeners);
		}

		Iterator<IEventBusListener> it = clonedList.iterator();

		while (it.hasNext())
		{
			IEventBusListener listener = it.next();

			listener.processEvent(event);
		}
	}

	public void addEventBusListener(IEventBusListener listener)
	{
		synchronized (_listeners)
		{
			_listeners.add(listener);
		}
	}

	public void removeEventBusListener(IEventBusListener listener)
	{
		synchronized (_listeners)
		{
			_listeners.remove(listener);
		}
	}

	private List<IEventBusListener> _listeners;
}