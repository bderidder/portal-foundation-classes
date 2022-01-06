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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JavaScriptServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int STREAM_BUFFER_SIZE = 1024;
	private static final int EOF = -1;
	private static final String RESPONSE_CONTENT_TYPE = "application/x-javascript";
	private static final String SCRIPT_RESOURCE_PATH = "/WEB-INF/JS/";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			ServletContext context = getServletConfig().getServletContext();

			Set<?> scripts = context.getResourcePaths(SCRIPT_RESOURCE_PATH);

			prepareResponse(request, response);

			sendResources(scripts, request, response);
		} catch (Exception e) {
			e.printStackTrace();

			throw new ServletException("could not process java scripts");
		}
	}

	private void prepareResponse(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		response.setContentType(RESPONSE_CONTENT_TYPE);
	}

	private void sendResources(Set<?> resources, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletConfig().getServletContext();

		Iterator<?> it = resources.iterator();

		while (it.hasNext()) {
			String resourcePath = (String) it.next();

			if (resourcePath.endsWith(".js")) {
				InputStream iStream = context.getResourceAsStream(resourcePath);

				response.getOutputStream().print(
						"\n\n/* BEGIN " + resourcePath + " */\n\n");

				streamResource(iStream, request, response);

				response.getOutputStream().print(
						"\n\n/* END " + resourcePath + " */\n\n");
			}
		}
	}

	private void streamResource(InputStream iStream,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream oStream = response.getOutputStream();

		byte[] buffer = new byte[STREAM_BUFFER_SIZE];
		int read = 0;

		while ((read = iStream.read(buffer)) != EOF) {
			oStream.write(buffer, 0, read);
		}
	}
}