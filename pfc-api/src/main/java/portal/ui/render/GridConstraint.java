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

public class GridConstraint implements Serializable
{
	private static final long serialVersionUID = 1L;

	public GridConstraint(Insets padding, Insets spacing)
	{
		this._padding = padding;
		this._margin = spacing;

		this._styleRule = new StyleRule();
	}

	public StyleRule getStyleRule()
	{
		return _styleRule;
	}

	public Insets getPadding()
	{
		return _padding;
	}

	public Insets getMargin()
	{
		return _margin;
	}

	public void setPadding(Insets insets)
	{
		_padding = insets;
	}

	public void setSpacing(Insets insets)
	{
		_margin = insets;
	}

	public String toStyleValue()
	{
		String paddingValue = getPadding().getTop() + " "
				+ getPadding().getRight() + " " + getPadding().getBottom()
				+ " " + getPadding().getLeft();

		String marginValue = getMargin().getTop() + " "
				+ getMargin().getRight() + " " + getMargin().getBottom() + " "
				+ getMargin().getLeft();

		String styleValue = "padding: " + paddingValue + "; margin: "
				+ marginValue + ";" + _styleRule.toStyleValue();

		return styleValue;
	}

	private Insets _padding;
	private Insets _margin;
	private StyleRule _styleRule;
}