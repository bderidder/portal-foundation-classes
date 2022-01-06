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

public class FlowLayout extends TableLayoutManager
{
	private static final long serialVersionUID = 1L;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public FlowLayout(int orientation)
	{
		this._orientation = orientation;
	}

	protected void doTableContent(Container target, IRenderContext pRenderContext)
			throws RenderException
	{
		try
		{
			writeBeginFlow(pRenderContext);

			for (int i = 0; i != target.size(); i++)
			{
				Component<?> child = target.getComponent(i);

				GridConstraint constraint = getConstraint(target, i);

				writeBeginComponent(pRenderContext, constraint);
				pRenderContext.includeComponent(child);
				writeAfterComponent(pRenderContext, constraint);
			}

			writeAfterFlow(pRenderContext);
		}
		catch (IOException e)
		{
			throw new RenderException("Got IO exception", e);
		}
	}

	private void writeBeginFlow(IRenderContext pRenderContext) throws RenderException,
			IOException
	{
		if (_orientation == FlowLayout.HORIZONTAL)
		{
			pRenderContext.getWriter().write("<tr>");
		}
	}

	private void writeAfterFlow(IRenderContext pRenderContext) throws RenderException,
			IOException
	{
		if (_orientation == FlowLayout.HORIZONTAL)
		{
			pRenderContext.getWriter().write("</tr>");
		}
	}

	private void writeBeginComponent(IRenderContext pRenderContext,
			GridConstraint constraint) throws RenderException, IOException
	{
		String styleValue = constraint.toStyleValue();

		if (_orientation == FlowLayout.HORIZONTAL)
		{
			pRenderContext.getWriter().write("<td style=\"" + styleValue + "\">");
		}
		else
		{
			pRenderContext.getWriter().write("<tr><td style=\"" + styleValue + "\">");
		}
	}

	private void writeAfterComponent(IRenderContext pRenderContext,
			GridConstraint constraint) throws RenderException, IOException
	{
		if (_orientation == FlowLayout.HORIZONTAL)
		{
			pRenderContext.getWriter().write("</td>");
		}
		else
		{
			pRenderContext.getWriter().write("</td></tr>");
		}
	}

	private GridConstraint getConstraint(Container target, int index)
	{
		Object oConstraint = target.getConstraint(index);

		if (oConstraint == null)
		{
			return defaultConstraint;
		}
		else if (oConstraint instanceof GridConstraint)
		{
			return (GridConstraint) oConstraint;
		}
		else
		{
			return defaultConstraint;
		}
	}

	private int _orientation;

	private static GridConstraint defaultConstraint = new GridConstraint(
			new Insets(1, 1, 1, 1), new Insets(0, 0, 0, 0));
}