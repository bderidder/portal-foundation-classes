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
import java.io.Reader;
import java.io.Writer;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.registry.StringKey;

public class StylesheetServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final Log LOGGER = LogFactory.getLog(StylesheetServlet.class);

	private static final int STREAM_BUFFER_SIZE = 1024;
	private static final int EOF = -1;

	private static final String RESPONSE_CONTENT_TYPE = "text/css";

	private static final int CACHE_MINUTES = 10;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException
	{
		initProvider();

		try
		{
			prepareResponse(request, response);

			if (_provider.isCacheable())
			{
				setCachingHeaders(request, response);
			}

			Writer writer = response.getWriter();

			streamResource(_provider.getStyleSheetReader(), writer);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new ServletException("could not process stylesheets");
		}
	}

	private void prepareResponse(HttpServletRequest request,
			HttpServletResponse response) throws ServletException
	{
		response.setContentType(RESPONSE_CONTENT_TYPE);
	}

	private void setCachingHeaders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		Calendar lastModifiedTime = Calendar.getInstance();
		Calendar expiresTime = Calendar.getInstance();
		expiresTime.add(Calendar.MINUTE, CACHE_MINUTES);

		int maxAge = CACHE_MINUTES * 60;

		String eTagID = request.getSession().getId();

		response.setHeader("ETag", "\"" + eTagID + "\"");
		response.setHeader("Cache-Control", "s-maxage=" + maxAge + "\", public");

		response.setDateHeader("Last-Modified", lastModifiedTime.getTime()
				.getTime());

		response.setDateHeader("Expires", expiresTime.getTime().getTime());
	}

	private void streamResource(Reader reader, Writer writer)
			throws IOException
	{
		char[] buffer = new char[STREAM_BUFFER_SIZE];
		int read = 0;

		while ((read = reader.read(buffer)) != EOF)
		{
			writer.write(buffer, 0, read);
		}
	}

	private void initProvider() throws ServletException
	{
		if (_provider == null)
		{
			String stylesheetPath = StringKey.getValue(
					"portal.core.engine.renderer.stylesheetPath", "");

			if (stylesheetPath == null || stylesheetPath.equals(""))
			{
				LOGGER.debug("Using WARStylesheetProvider");

				_provider = new WARStylesheetProvider(getServletConfig());
			}
			else
			{
				LOGGER.debug("Using FileStylesheetProvider[" + stylesheetPath
						+ "]");

				_provider = new FileStylesheetProvider(stylesheetPath);
			}
		}
	}

	private IStylesheetProvider _provider;
}