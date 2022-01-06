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

package portal.rt.debug;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.ui.Component;

public final class DebugUtils
{
	private static final Log LOGGER = LogFactory.getLog(DebugUtils.class);

	public static void dumpComponentStack(Component<?> comp)
	{
		Component<?> parent = comp;

		while (parent != null)
		{
			LOGGER.debug("Component: " + parent);

			parent = parent.getParent();
		}

		LOGGER.debug("Component stack finished, encountered null");
	}

	private DebugUtils()
	{
	}
}