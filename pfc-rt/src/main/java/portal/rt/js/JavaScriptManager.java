/*
 * Copyright 2000, 2013,  Bavo De Ridder
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
package portal.rt.js;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import portal.ui.js.IJavaScriptManager;
import portal.ui.js.IScriptBody;
import portal.ui.render.RenderException;

public class JavaScriptManager implements IJavaScriptManager
{
	private List<IScriptBody> _scriptBodies;

	public JavaScriptManager()
	{
		_scriptBodies = new LinkedList<>();
	}

	public void reset()
	{
		_scriptBodies.clear();
	}

	@Override
	public void addScriptBody(IScriptBody pScriptBody)
	{
		_scriptBodies.add(pScriptBody);
	}

	public void include(HttpServletResponse pServletResponse)
			throws IOException
	{
		CharArrayWriter charWriter = new CharArrayWriter();
		PrintWriter writer = new PrintWriter(charWriter);

		Iterator<IScriptBody> it = _scriptBodies.iterator();
		while (it.hasNext())
		{
			IScriptBody scriptBody = it.next();

			writer.println("<script type=\"text/javascript\">");

			try
			{
				scriptBody.streamBody(writer);
			}
			catch (RenderException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			writer.println("</script>");
		}

		char[] buffer = charWriter.toCharArray();

		pServletResponse.getWriter().write(buffer);
	}
}
