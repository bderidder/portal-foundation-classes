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

import portal.model.IModel;
import portal.ui.AbstractFileFieldUpdater;
import portal.ui.Component;
import portal.ui.IFileItem;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class FileUpload extends Component<IModel>
{
	private static final long serialVersionUID = 1L;

	private class FileUploadUpdater extends AbstractFileFieldUpdater
	{
		private static final long serialVersionUID = 1L;

		public void update(IFileItem fileItem)
		{
			setFileItem(fileItem);
		}
	}

	public FileUpload()
	{
		super();
	}

	public final IFileItem getFileItem()
	{
		return _fileItem;
	}

	public int getSize()
	{
		return _size;
	}

	public void setSize(int size)
	{
		this._size = size;
	}

	public void reset()
	{
		if (_fileItem != null)
		{
			_fileItem.delete();

			_fileItem = null;
		}
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		String updaterKey = pRenderContext.addUpdater(new FileUploadUpdater());

		StringBuffer buffer = new StringBuffer();

		buffer.append("<input");

		addAttribute("type", "file", buffer);
		addAttribute("name", updaterKey, buffer);

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

	protected final void setFileItem(IFileItem item)
	{
		_fileItem = item;
	}

	private int _size = 0;

	private transient IFileItem _fileItem;
}