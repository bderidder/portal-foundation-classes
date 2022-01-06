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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class StartPortalInstance extends BasePortalServlet
{
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(StartPortalInstance.class);

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	@Override
	public void process(IHttpProcessContext processContext)
			throws ServletException
	{
		try
		{
			processContext.getRequest().getSession().invalidate();

			processContext.redirectToRenderer();
		}
		catch (Exception e)
		{
			LOGGER.error("Could not instantiate portal", e);

			throw new ServletException(e.getMessage());
		}
	}
}
