/*
 * Copyright 2000,  Bavo De Ridder
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
import portal.ui.Component;
import portal.ui.IAction;

public class AbstractButton extends Component<IModel>
{
    private static final long serialVersionUID = 1L;

    public final void setAction(IAction buttonAction)
    {
        this._buttonAction = buttonAction;
    }

    public final IAction getAction()
    {
        return _buttonAction;
    }

    public final void setCaption(String caption)
    {
        this._caption = caption;
    }

    public final String getCaption()
    {
        return _caption;
    }
    private IAction _buttonAction;
    private String _caption;
}
