/*
 * Copyright 2000 - 2004, Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Portal Foundation Classes; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */
package portal.rt.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.rt.ui.render.BrowserDevice;

public abstract class BasePortalServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(BasePortalServlet.class);

	@Override
	protected final void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException
	{
		doProcess(request, response);
	}

	@Override
	protected final void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		doProcess(request, response);
	}

	protected final void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = null;

		if (request.getSession(false) == null)
		{
			session = request.getSession(true);

			//session.setMaxInactiveInterval(0);
		}
		else
		{
			session = request.getSession();
		}

		try
		{
			/*
			 String myNDC = session.getId() + "-" + request.hashCode() + "-"
			 + System.currentTimeMillis();
			 NDC.push(myNDC);
			 */

			BrowserDevice browserDevice = ProcessUtils.getBrowserDevice(session);

			if (browserDevice == null)
			{
				browserDevice = new BrowserDevice();

				ProcessUtils.setBrowserDevice(session, browserDevice);

				ServletPortalContext portalContext = new ServletPortalContext(browserDevice);
				ProcessUtils.setPortalContext(session,
						new ServletPortalContext(browserDevice));

				ServletPortalContext.setPortalContext(portalContext);

				browserDevice.init();

				redirectToRenderer(request, response);
			}
			else
			{
				ServletPortalContext portalContext = ProcessUtils.getPortalContext(session);
				ServletPortalContext.setPortalContext(portalContext);

				process(new HttpProcessContext(getServletContext(), request,
						response));
			}
		}
		catch (ServletException | IOException e)
		{
			LOGGER.error("unknown error in core engine processing", e);
		}
		finally
		{
			//NDC.pop();
		}
	}

	protected abstract void process(IHttpProcessContext processContext)
			throws ServletException, IOException;

	protected void redirectToRenderer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect(request.getContextPath()
				+ ProcessConstants.RENDER_URL);
	}
}
