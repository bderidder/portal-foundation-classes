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

package portal.ui.render;

import java.io.Serializable;

public class Insets implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final Insets ZERO = new Insets(0, 0, 0, 0);

	public Insets(int bottom, int top, int left, int right)
	{
		this._bottom = bottom;
		this._top = top;
		this._left = left;
		this._right = right;
	}

	public int getBottom()
	{
		return _bottom;
	}

	public int getLeft()
	{
		return _left;
	}

	public int getRight()
	{
		return _right;
	}

	public int getTop()
	{
		return _top;
	}

	public void setBottom(int i)
	{
		_bottom = i;
	}

	public void setLeft(int i)
	{
		_left = i;
	}

	public void setRight(int i)
	{
		_right = i;
	}

	public void setTop(int i)
	{
		_top = i;
	}

	private int _bottom;
	private int _top;
	private int _left;
	private int _right;
}