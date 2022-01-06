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

import portal.ui.Component;
import portal.ui.render.StyleRule;

public abstract class ListComponent<DataType> extends Component<IListDataModel<DataType>>
{
	private static final long serialVersionUID = 1L;
	
	public ListComponent(IListDataModel<DataType> model, ISelectionModel selection)
	{
		super(model);

		setSelectionModel(selection);
	}

	public final StyleRule getStyleRule()
	{
		return _styleRule;
	}

	public final void setStyleRule(StyleRule styleRule)
	{
		_styleRule = styleRule;
	}

	public final void setListItemRenderer(IListItemRenderer listItemRenderer)
	{
		_listItemRenderer = listItemRenderer;
	}

	public final IListDataModel<DataType> getListDataModel()
	{
		return getModel();
	}

	public final ISelectionModel getSelectionModel()
	{
		return _selection;
	}

	public final void setSelectionModel(ISelectionModel selection)
	{
		this._selection = selection;
	}

	protected String renderDataValue(Object dataValue)
	{
		if (_listItemRenderer != null)
		{
			return _listItemRenderer.renderItem(dataValue);
		}
		else
		{
			return dataValue.toString();
		}
	}

	private StyleRule _styleRule;
	private ISelectionModel _selection;

	private IListItemRenderer _listItemRenderer;
}