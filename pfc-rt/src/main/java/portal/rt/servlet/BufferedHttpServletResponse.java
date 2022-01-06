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

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferedHttpServletResponse extends HttpServletResponseWrapper
{
	public BufferedHttpServletResponse(HttpServletResponse response)
	{
		super(response);

		_charWriter = new CharArrayWriter();
		_printWriter = new PrintWriter(_charWriter);
	}

	public PrintWriter getWriter()
	{
		return _printWriter;
	}

	public CharArrayWriter getCharWriter()
	{
		return _charWriter;
	}

	private CharArrayWriter _charWriter;
	private PrintWriter _printWriter;
}