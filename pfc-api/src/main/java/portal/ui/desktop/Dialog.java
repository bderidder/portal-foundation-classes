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

package portal.ui.desktop;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import portal.ui.Component;

public class Dialog extends Window
{
	private static final long serialVersionUID = 1L;

	public Dialog()
	{
		this(null);
	}

	public Dialog(Component<?> parent)
	{
		setParent(parent);

		_dialogListeners = new LinkedList<DialogListener>();
	}

	public void show()
	{
		if (getParent() == null)
		{
			throw new IllegalStateException(
					"Parent was null, dialogs need a parent when shown");
		}

		Component<?> parent = getParent();

		while (parent != null)
		{
			if (Window.class.isAssignableFrom(parent.getClass()))
			{
				break;
			}
			else
			{
				parent = parent.getParent();
			}
		}

		if (parent != null)
		{
			Window window = (Window) parent;
			window.showDialog(this);
		}
		else
		{
			throw new IllegalStateException("No InternalFrame found, "
					+ "dialogs need a parent when shown");
		}
	}

	public void addDialogListener(DialogListener listener)
	{
		synchronized (_dialogListeners)
		{
			_dialogListeners.add(listener);
		}
	}

	public void removeDialogListener(DialogListener listener)
	{
		synchronized (_dialogListeners)
		{
			_dialogListeners.remove(listener);
		}
	}

	protected void notifyDialogListeners(int resultCode)
	{
		List<DialogListener> list = null;

		synchronized (_dialogListeners)
		{
			list = new LinkedList<DialogListener>(_dialogListeners);
		}

		Iterator<DialogListener> it = list.iterator();
		while (it.hasNext())
		{
			DialogListener listener = it.next();

			listener.result(resultCode);
		}
	}

	private List<DialogListener> _dialogListeners;
}