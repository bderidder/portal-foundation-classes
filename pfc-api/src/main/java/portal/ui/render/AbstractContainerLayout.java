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

import java.io.IOException;

import portal.ui.Component;
import portal.ui.Container;

public abstract class AbstractContainerLayout implements LayoutManager
{
	private static final long serialVersionUID = 1L;

	public AbstractContainerLayout()
	{
	}

	public final void doLayout(Component<?> target, IRenderContext renderContext)
			throws RenderException, IOException
	{
		if (!(target instanceof Container))
		{
			throw new RenderException(
					"Can not layout a container component with a non container layout.");
		}
		else
		{
			doContainerLayout((Container) target, renderContext);
		}
	}

	public abstract void doContainerLayout(Container target,
			IRenderContext renderContext) throws RenderException, IOException;
}