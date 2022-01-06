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

package portal.ui.comp.list;

import java.io.PrintWriter;
import java.util.Iterator;

import portal.ui.AbstractFormFieldUpdater;
import portal.ui.IUpdater;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class ListBox<DataType> extends ListComponent<DataType>
{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_SIZE = 5;

	private class ListBoxUpdater extends AbstractFormFieldUpdater
	{
		private static final long serialVersionUID = 1L;
		
		public void update(String[] values)
		{
			ISelectionModel selectionModel = getSelectionModel();

			selectionModel.clearSelection();

			if (values != null)
			{
				for (int i = 0; i != values.length; i++)
				{
					int index = Integer.parseInt(values[i]);

					selectionModel.setSelected(index, true);
				}
			}
		}
	}

	public ListBox(IListDataModel<DataType> model, ISelectionModel selection)
	{
		super(model, selection);

		setSize(DEFAULT_SIZE);

		setSelectionModel(selection);
	}

	public int getSize()
	{
		return _size;
	}

	public void setSize(int size)
	{
		this._size = size;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		IUpdater updater = new ListBoxUpdater();

		String key = pRenderContext.addUpdater(updater);

		IListDataModel<DataType> listModel = getListDataModel();

		StringBuffer buffer = new StringBuffer();

		buffer.append("<select size='" + _size + "' name='" + key + "'");

		if (getStyleRule() != null)
		{
			if (getStyleRule().getStyleClass() != null)
			{
				buffer.append(" class=\"" + getStyleRule().getStyleClass()
						+ "\"");
			}

			buffer.append(" style=\"" + getStyleRule().toStyleValue() + "\"");
		}

		if (!isEnabled())
		{
			buffer.append(" disabled='true'");
		}

		if (SelectionMode.MULTIPLE_SELECTION.equals(getSelectionModel().getSelectionMode()))
		{
			buffer.append(" MULTIPLE");
		}

		buffer.append(">");

		int currentIndex = 0;
		Iterator<DataType> itData = listModel.getDataIterator();
		while (itData.hasNext())
		{
			DataType dataValue = itData.next();

			buffer.append("<option value='" + currentIndex + "'");

			if (getSelectionModel().isSelected(currentIndex))
			{
				buffer.append(" selected>");
			}
			else
			{
				buffer.append(">");
			}

			buffer.append(renderDataValue(dataValue));

			buffer.append("</option>");

			currentIndex++;
		}

		buffer.append("</select>");

		writer.println(buffer.toString());
	}

	private int _size;
}