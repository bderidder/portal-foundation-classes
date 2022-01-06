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

import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;
import portal.ui.render.StyleRule;

public class ActionLink extends AbstractButton
{
	private static final long serialVersionUID = 1L;

	public ActionLink()
	{
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		String actionUrl = pRenderContext.createActionUrl(getAction());

		StringBuffer buffer = new StringBuffer();

		buffer.append("<a");

		addAttribute("href", actionUrl, buffer);

		if (_styleRule != null)
		{
			if (_styleRule.getStyleClass() != null)
			{
				addAttribute("class", _styleRule.getStyleClass(), buffer);
			}

			addAttribute("style", _styleRule.toStyleValue(), buffer);
		}
		else
		{
			addAttribute("class", "ActionLink", buffer);
		}

		buffer.append(">");

		buffer.append(getCaption());

		buffer.append("</a>");

		try
		{
			pRenderContext.getWriter().write(buffer.toString());
		}
		catch (IOException e)
		{
			throw new RenderException("Could not write buffer", e);
		}
	}

	public void setStyleRule(StyleRule styleRule)
	{
		this._styleRule = styleRule;
	}

	private void addAttribute(String name, String value, StringBuffer buffer)
	{
		if (value != null)
		{
			buffer.append(" " + name + "=\"" + value + "\"");
		}
	}

	private StyleRule _styleRule;
}