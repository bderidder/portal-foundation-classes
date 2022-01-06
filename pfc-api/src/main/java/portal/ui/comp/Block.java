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

package portal.ui.comp;

import java.io.IOException;

import portal.model.IModel;
import portal.ui.Component;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class Block extends Component<IModel>
{
	private static final long serialVersionUID = 1L;
	
	public static final String P = "p";
	public static final String DIV = "div";

	public Block(String blockType, String style, Component<?> child)
	{
		this._blockType = blockType;
		this._style = style;
		this._child = child;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		try
		{
			pRenderContext.getWriter().write(
					"<" + _blockType + " class='" + _style + "'>");

			if (_child != null)
			{
				pRenderContext.includeComponent(_child);
			}

			pRenderContext.getWriter().write("</" + _blockType + ">");
		}
		catch (IOException e)
		{
			throw new RenderException("Got IO exception", e);
		}
	}

	private String _blockType;
	private String _style;
	private Component<?> _child;
}