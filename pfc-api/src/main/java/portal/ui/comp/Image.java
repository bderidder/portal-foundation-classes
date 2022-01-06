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

import java.io.PrintWriter;

import portal.services.resource.IResource;
import portal.ui.IAction;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class Image extends AbstractButton
{
	private static final long serialVersionUID = 1L;

	// private static final Log LOGGER = LogFactory.getLog(Image.class);

	public Image(IResource resource)
	{
		this(resource, null);
	}

	public Image(IResource resource, IResource hoverResource)
	{
		super();

		_resource = resource;
		_hoverResource = hoverResource;
	}

	public IResource getResource()
	{
		return _resource;
	}

	public void setResource(IResource resource)
	{
		this._resource = resource;
	}

	public IResource getHoverResource()
	{
		return _hoverResource;
	}

	public void setHoverResource(IResource hoverResource)
	{
		this._hoverResource = hoverResource;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		StringBuffer buffer = new StringBuffer();

		String resourceUrl = pRenderContext.createResourceUrl(_resource);

		String hoverResourceUrl = resourceUrl;
		if (_hoverResource != null)
		{
			hoverResourceUrl = pRenderContext.createResourceUrl(_hoverResource);
		}

		String overJS = "pfc.switchImage('" + getId() + "', '" + hoverResourceUrl
				+ "')";
		String outJS = "pfc.switchImage('" + getId() + "', '" + resourceUrl + "')";

		IAction myAction = getAction();
		if (myAction != null)
		{
			String actionUrl = pRenderContext.createActionUrl(myAction);

			buffer.append("<a");
			addAttribute("href", actionUrl, buffer);
			addAttribute("onmouseover", overJS, buffer);
			addAttribute("onmouseout", outJS, buffer);
			buffer.append(">");

			drawImage(pRenderContext, resourceUrl, buffer);

			buffer.append("</a>");
		}
		else
		{
			buffer.append("<span");
			addAttribute("onmouseover", overJS, buffer);
			addAttribute("onmouseout", outJS, buffer);
			buffer.append(">");

			drawImage(pRenderContext, resourceUrl, buffer);

			buffer.append("</span>");
		}

		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		writer.print(buffer.toString());
		writer.flush();
	}

	private void drawImage(IRenderContext pRenderContext, String resourceUrl,
			StringBuffer buffer) throws RenderException
	{
		buffer.append("<img");

		addAttribute("id", getId(), buffer);
		addAttribute("src", resourceUrl, buffer);
		addAttribute("border", "no", buffer);

		String myCaption = getCaption();
		if (myCaption != null && myCaption.length() > 0)
		{
			addAttribute("title", myCaption, buffer);
		}

		buffer.append("/>");
	}

	private void addAttribute(String name, String value, StringBuffer buffer)
	{
		if (value != null)
		{
			buffer.append(" " + name + "=\"" + value + "\"");
		}
	}

	private IResource _resource;
	private IResource _hoverResource;
}