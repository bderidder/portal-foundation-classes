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

import portal.model.IModel;
import portal.model.ISelectableModel;
import portal.ui.Component;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public final class RadioButton extends Component<IModel> implements ISelectableModel
{
	private static final long serialVersionUID = 1L;

	public RadioButton()
	{
		super();
	}

	public boolean isSelected()
	{
		return _myRadioButtonGroup.isSelected(this);
	}

	public void setSelected(boolean selected)
	{
		_myRadioButtonGroup.setSelected(this, selected);
	}

	public void setRadioButtonGroup(RadioButtonGroup buttonGroup)
	{
		if (_myRadioButtonGroup != null)
		{
			_myRadioButtonGroup.removeRadioButton(this);
		}

		_myRadioButtonGroup = buttonGroup;

		if (_myRadioButtonGroup != null)
		{
			_myRadioButtonGroup.addRadioButton(this);
		}
	}

	public RadioButtonGroup getRadioButtonGroup()
	{
		return _myRadioButtonGroup;
	}

	public void setCaption(String caption)
	{
		this._caption = caption;
	}

	public String getCaption()
	{
		return _caption;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException
	{
		getRadioButtonGroup().renderRadioButton(this, pRenderContext);
	}

	private String _caption = null;
	private RadioButtonGroup _myRadioButtonGroup;
}