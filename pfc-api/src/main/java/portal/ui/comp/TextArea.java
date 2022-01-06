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
import java.text.MessageFormat;

import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;
import portal.ui.AbstractFormFieldUpdater;
import portal.ui.form.FormElement;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class TextArea extends FormElement<IPropertyModel<String>>
{
    private static final long serialVersionUID = 1L;

    //private static final Log LOGGER = LogFactory.getLog(TextArea.class);
    private class TextAreaUpdater extends AbstractFormFieldUpdater
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void update(String[] values)
        {
            if (values != null)
            {
                getModel().setValue(values[0]);
            }
        }
    }

    public TextArea()
    {
    }

    public TextArea(IPropertyModel<String> model)
    {
        super(model);
    }

    public int getColumns()
    {
        return _columns;
    }

    public void setColumns(int columns)
    {
        this._columns = columns;
    }

    public int getRows()
    {
        return _rows;
    }

    public void setRows(int rows)
    {
        this._rows = rows;
    }

    @Override
    public IPropertyModel<String> createDefaultModel()
    {
        return new SimplePropertyModel<>(new String());
    }

    @Override
    public void draw(IRenderContext pRenderContext) throws RenderException
    {
        PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

        String updaterKey = pRenderContext.addUpdater(new TextAreaUpdater());

        StringBuffer buffer = new StringBuffer();

        buffer.append("<textarea");

        if (!isEnabled())
        {
            addAttribute("disabled", "true", buffer);
        }

        addAttribute("name", updaterKey, buffer);

        if (getColumns() > 0)
        {
            addAttribute("cols", Integer.toString(getColumns()), buffer);
        }

        if (getRows() > 0)
        {
            addAttribute("rows", Integer.toString(getRows()), buffer);
        }

        if (!isValid())
        {
            addAttribute("class", "TextAreaError", buffer);
            //			addAttribute(
            //				"title",
            //				getTextAreaModel().getErrorDescription(),
            //				buffer);
        }
        else
        {
            addAttribute("class", "TextAreaNormal", buffer);
        }

        buffer.append(">");

        if (getModel().getValue() != null)
        {
            buffer.append(getModel().getValue());
        }

        buffer.append("</textarea>");

        writer.println(buffer.toString());

        writer.flush();
    }

    private void addAttribute(String name, String value, StringBuffer buffer)
    {
        if (value != null)
        {
            buffer.append(MessageFormat.format(" {0}=\"{1}\"", name, value));
        }
    }
    private int _columns = 0;
    private int _rows = 0;
}
