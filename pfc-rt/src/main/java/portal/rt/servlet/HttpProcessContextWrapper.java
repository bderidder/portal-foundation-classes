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

package portal.rt.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.rt.ui.render.BrowserDevice;

public class HttpProcessContextWrapper implements IHttpProcessContext
{

	public HttpProcessContextWrapper(IHttpProcessContext wrappedContext)
	{
		if (wrappedContext == null)
		{
			throw new IllegalArgumentException("wrapped context cannot be null");
		}

		this._wrappedContext = wrappedContext;
	}

	public BrowserDevice getBrowserDevice()
	{
		return getWrappedContext().getBrowserDevice();
	}

	public HttpServletRequest getRequest()
	{
		return getWrappedContext().getRequest();
	}

	public HttpServletResponse getResponse()
	{
		return getWrappedContext().getResponse();
	}

	public ServletContext getServletContext()
	{
		return getWrappedContext().getServletContext();
	}

	public void redirectToRenderer() throws ServletException
	{
		getWrappedContext().redirectToRenderer();
	}

	private IHttpProcessContext getWrappedContext()
	{
		return _wrappedContext;
	}

	private IHttpProcessContext _wrappedContext;
}