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

package portal.ui.comp;

import java.io.PrintWriter;

import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;
import portal.ui.AbstractFormFieldUpdater;
import portal.ui.form.FormElement;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class TextInput extends FormElement<IPropertyModel<String>>
{
	private static final long serialVersionUID = 1L;

	private class TextInputUpdater extends AbstractFormFieldUpdater
	{
		private static final long serialVersionUID = 1L;

		public void update(String[] values)
		{
			if (values != null)
			{
				getModel().setValue(values[0]);
			}
		}
	}

	public TextInput()
	{
	}

	public TextInput(IPropertyModel<String> model)
	{
		super(model);
	}

	public int getMaximumLength()
	{
		return _maximumLength;
	}

	public void setMaximumLength(int maximumLength)
	{
		this._maximumLength = maximumLength;
	}

	public int getSize()
	{
		return _size;
	}

	public void setSize(int size)
	{
		this._size = size;
	}

	public IPropertyModel<String> createDefaultModel()
	{
		return new SimplePropertyModel<String>(new String());
	}

	public void setId(String id)
	{
		this._id = id;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		String updaterKey = null;

		if (_id == null)
		{
			updaterKey = pRenderContext.addUpdater(new TextInputUpdater());
		}
		else
		{
			pRenderContext.addUpdater(_id, new TextInputUpdater());
			updaterKey = _id;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append("<input");

		addAttribute("type", "text", buffer);

		if (!isEnabled())
		{
			addAttribute("disabled", "true", buffer);
		}

		addAttribute("name", updaterKey, buffer);
		addAttribute("value", sanitizeHTML(getModel().getValue()), buffer);

		if (!isValid())
		{
			addAttribute("class", "TextInputError", buffer);
			//			addAttribute(
			//				"title",
			//				getTextInputModel().getErrorDescription(),
			//				buffer);
		}
		else
		{
			addAttribute("class", "TextInputNormal", buffer);
		}

		if (getMaximumLength() > 0)
		{
			addAttribute("maxLength", Integer.toString(getMaximumLength()),
					buffer);
		}

		if (getSize() > 0)
		{
			addAttribute("size", Integer.toString(getSize()), buffer);
		}

		buffer.append("/>");

		writer.println(buffer.toString());
	}

	private void addAttribute(String name, String value, StringBuffer buffer)
	{
		if (value != null)
		{
			buffer.append(" " + name + "=\"" + value + "\"");
		}
	}
	
	private String sanitizeHTML(String text)
	{
		return text.replaceAll("\"", "&quot;");
	}

	private int _maximumLength = 0;
	private int _size = 0;
	private String _id = null;
}