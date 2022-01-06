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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.rt.ui.render.BrowserDevice;
import portal.services.registry.BooleanKey;

public class HttpProcessContext implements IHttpProcessContext
{
	public HttpProcessContext(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response)
	{
		this._servletContext = servletContext;
		this._contextRequest = request;
		this._contextResponse = response;
	}

	public BrowserDevice getBrowserDevice()
	{
		return ProcessUtils.getBrowserDevice(_contextRequest.getSession());
	}

	public void redirectToRenderer() throws ServletException
	{
		if (isRedirectAfterPost())
		{
			redirectToUrl(_contextRequest, getResponse(),
					_contextRequest.getContextPath()
							+ ProcessConstants.RENDER_URL);
		}
		else
		{

			dispatchToUrl(_contextRequest, getResponse(),
					ProcessConstants.RENDER_URL);
		}
	}

	private void redirectToUrl(HttpServletRequest request,
			HttpServletResponse response,
			String url) throws ServletException
	{
		try
		{
			response.sendRedirect(url);
		}
		catch (IOException e)
		{
			throw new ServletException("redirect exception: " + e.toString());
		}
	}

	private void dispatchToUrl(HttpServletRequest request,
			HttpServletResponse response,
			String url) throws ServletException
	{
		try
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(
					ProcessConstants.RENDER_URL);

			dispatcher.forward(request, response);
		}
		catch (IOException e)
		{
			throw new ServletException("dispatch exception: " + e.toString());
		}
	}

	private boolean isRedirectAfterPost()
	{
		return BooleanKey.getValue(
				"portal.core.engine.renderer.redirectAfterPost", Boolean.FALSE)
				.booleanValue();
	}

	public HttpServletRequest getRequest()
	{
		return _contextRequest;
	}

	public HttpServletResponse getResponse()
	{
		return _contextResponse;
	}

	public ServletContext getServletContext()
	{
		return _servletContext;
	}

	private ServletContext _servletContext;
	private HttpServletRequest _contextRequest;
	private HttpServletResponse _contextResponse;
}