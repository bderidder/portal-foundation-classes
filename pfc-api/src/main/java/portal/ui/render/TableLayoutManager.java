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

import portal.ui.Container;

public abstract class TableLayoutManager extends AbstractContainerLayout
{
	private static final long serialVersionUID = 1L;

	public static final String CENTERED = "center";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";

	public static final String TOP = "top";
	public static final String MIDDLE = "middle";
	public static final String BOTTOM = "bottom";

	public TableLayoutManager()
	{
		_horizontalPosition = LEFT;
		_verticalPosition = TOP;
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

	public String getHorizontalPosition()
	{
		return _horizontalPosition;
	}

	public String getVerticalPosition()
	{
		return _verticalPosition;
	}

	public void setHorizontalPosition(String string)
	{
		_horizontalPosition = string;
	}

	public void setVerticalPosition(String string)
	{
		_verticalPosition = string;
	}

	public final void doContainerLayout(Container target,
			IRenderContext renderContext) throws RenderException
	{
		try
		{
			renderContext.getWriter().write(
					"<table cellspacing=\"" + getSpacing()
							+ "\" cellpadding=\"" + getPadding()
							+ "\" align=\"" + getHorizontalPosition()
							+ "\" valign=\"" + getVerticalPosition() + "\">");

			doTableContent(target, renderContext);

			renderContext.getWriter().write("</table>");
		}
		catch (IOException e)
		{
			throw new RenderException("Got IO exception", e);
		}
	}

	protected abstract void doTableContent(Container target,
			IRenderContext pRenderContext) throws RenderException;

	private int _spacing = 0;
	private int _padding = 0;

	private String _horizontalPosition;
	private String _verticalPosition;

}