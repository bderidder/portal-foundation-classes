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
import java.util.Iterator;

import portal.ui.Component;
import portal.ui.Container;

public class GridLayout extends AbstractContainerLayout
{
	private static final long serialVersionUID = 1L;

	public GridLayout(int cols)
	{
		this._cols = cols;
	}

	public int getPadding()
	{
		return _padding;
	}

	public int getSpacing()
	{
		return _spacing;
	}

	public void setPadding(int i)
	{
		_padding = i;
	}

	public void setSpacing(int i)
	{
		_spacing = i;
	}

	public void doContainerLayout(Container target, IRenderContext renderContext)
			throws RenderException
	{
		try
		{
			Iterator<Component<?>> it = target.getComponents().iterator();

			int currentCol = 0;

			renderContext.getWriter().write(
					"<table cellspacing='" + getSpacing() + "' cellpadding='"
							+ getPadding() + "'>");
			renderContext.getWriter().write("<tr>");

			while (it.hasNext())
			{
				Component<?> child = it.next();

				currentCol++;

				if (currentCol > _cols)
				{
					renderContext.getWriter().write("</tr>");
					renderContext.getWriter().write("<tr>");

					currentCol = 1;
				}
				else
				{
					// row doens't have to be closed yet.
				}

				renderContext.getWriter().write("<td>");
				renderContext.includeComponent(child);
				renderContext.getWriter().write("</td>");
			}

			renderContext.getWriter().write("</tr>");
			renderContext.getWriter().write("</table>");
		}
		catch (IOException e)
		{
			throw new RenderException("Got IO exception", e);
		}
	}

	private int _cols;
	private int _spacing = 0;
	private int _padding = 0;
}