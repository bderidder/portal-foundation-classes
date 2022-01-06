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

package portal.rt.ui.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.rt.servlet.BasePortalServlet;
import portal.rt.servlet.IHttpProcessContext;
import portal.rt.servlet.ProcessConstants;
import portal.services.resource.IResource;

public class ResourceServlet extends BasePortalServlet
{
	private static final long serialVersionUID = 1L;

	private static final int CACHE_HOURS = 4;
	private static final int STREAM_BUFFER_SIZE = 4096;
	private static final int EOF = -1;

	private static final Log LOGGER = LogFactory.getLog(ResourceServlet.class);

	public void process(IHttpProcessContext processContext)
			throws ServletException
	{
		long beginTime = System.currentTimeMillis();

		try
		{
			String extraPathInfo = processContext.getRequest().getPathInfo();

			if (extraPathInfo != null)
			{
				processStatic(processContext);
			}
			else
			{
				processVolatile(processContext);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Could not process post request", e);
		}
		finally
		{
			long endTime = System.currentTimeMillis();

			LOGGER.debug("render time " + (endTime - beginTime));
		}
	}

	public void processStatic(IHttpProcessContext processContext)
			throws Exception
	{
		String extraPathInfo = processContext.getRequest().getPathInfo();
		extraPathInfo = extraPathInfo.substring(1);

		String resourceKey = null;
		String filename = null;

		if (extraPathInfo.indexOf('/') != -1)
		{
			resourceKey = extraPathInfo.substring(0, extraPathInfo.indexOf('/'));
			filename = extraPathInfo.substring(extraPathInfo.indexOf('/') + 1);
		}
		else
		{
			resourceKey = extraPathInfo;
		}

		IResource resource = StaticResourceMap.getStaticResourceMap()
				.getStaticResource(resourceKey);

		LOGGER.debug("Request for resource: " + resource);

		Calendar today = Calendar.getInstance();

		processContext.getResponse().setHeader("ETag",
				"\"" + resourceKey + "\"");
		processContext.getResponse().setHeader("Cache-Control",
				"max-age=3600, public");

		today.add(Calendar.HOUR, -1);
		processContext.getResponse().setDateHeader("Last-Modified",
				today.getTime().getTime());

		today.add(Calendar.HOUR, CACHE_HOURS);
		processContext.getResponse().setDateHeader("Expires",
				today.getTime().getTime());

		processContext.getResponse().setContentType(resource.getContentType());

		if (processContext.getRequest().getHeader("If-Modified-Since") != null)
		{
			processContext.getResponse().sendError(
					HttpServletResponse.SC_NOT_MODIFIED);

			return;
		}

		LOGGER.debug("Streaming resource: " + resource);

		if (filename != null)
		{
			addContentDispositionHeader(processContext.getResponse(), filename);
		}

		streamResource(resource.getResourceAsStream(),
				processContext.getResponse());
	}

	public void processVolatile(IHttpProcessContext processContext)
			throws Exception
	{
		ResourceMap resourceMap = processContext.getBrowserDevice()
				.getGraphicsDevice()
				.getResourceMap();
		String resourceKey = processContext.getRequest().getParameter(
				ProcessConstants.RESOURCE_REQUEST_KEY);
		String filename = processContext.getRequest().getParameter("name");

		IResource resource = resourceMap.getResource(resourceKey);

		LOGGER.debug("Streaming resource: " + resource);

		processContext.getResponse().setContentType(resource.getContentType());

		if (filename != null)
		{
			addContentDispositionHeader(processContext.getResponse(), filename);
		}

		streamResource(resource.getResourceAsStream(),
				processContext.getResponse());
	}

	private void addContentDispositionHeader(HttpServletResponse response,
			String filename)
	{
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);
	}

	private void streamResource(InputStream iStream,
			HttpServletResponse response) throws ServletException, IOException
	{
		OutputStream oStream = response.getOutputStream();

		byte[] buffer = new byte[STREAM_BUFFER_SIZE];
		int read = 0;

		while ((read = iStream.read(buffer)) != EOF)
		{
			oStream.write(buffer, 0, read);
		}
	}
}