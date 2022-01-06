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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WARStylesheetProvider implements IStylesheetProvider
{
	private static final int STREAM_BUFFER_SIZE = 1024;
	private static final int EOF = -1;

	private static final String SHEET_RESOURCE_PATH = "/WEB-INF/CSS/";

	public WARStylesheetProvider(ServletConfig servletConfig)
			throws ServletException
	{
		_servletConfig = servletConfig;

		init();
	}

	public Reader getStyleSheetReader()
	{
		return new StringReader(_sheetsContents);
	}

	public boolean isCacheable()
	{
		return true;
	}

	public void init() throws ServletException
	{
		try
		{
			ServletContext context = getServletConfig().getServletContext();

			Set<?> sheets = context.getResourcePaths(SHEET_RESOURCE_PATH);

			_sheetsContents = fetchSheets(sheets).toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new ServletException("could not process stylesheets");
		}
	}

	private ServletConfig getServletConfig()
	{
		return _servletConfig;
	}

	private StringBuffer fetchSheets(Set<?> resources) throws ServletException,
			IOException
	{
		ServletContext context = getServletConfig().getServletContext();

		StringWriter stringWriter = new StringWriter();

		Iterator<?> it = resources.iterator();

		while (it.hasNext())
		{
			String resourcePath = (String) it.next();

			Reader reader = new InputStreamReader(
					context.getResourceAsStream(resourcePath));

			stringWriter.write("\n\n/* BEGIN " + resourcePath + " */\n\n");

			streamResource(reader, stringWriter);

			stringWriter.write("\n\n/* END " + resourcePath + " */\n\n");
		}

		return stringWriter.getBuffer();
	}

	private void streamResource(Reader reader, Writer writer)
			throws ServletException, IOException
	{
		char[] buffer = new char[STREAM_BUFFER_SIZE];
		int read = 0;

		while ((read = reader.read(buffer)) != EOF)
		{
			writer.write(buffer, 0, read);
		}
	}

	private ServletConfig _servletConfig;
	private String _sheetsContents;
}