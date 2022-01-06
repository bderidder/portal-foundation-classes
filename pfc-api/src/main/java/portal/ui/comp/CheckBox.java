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
import portal.ui.Component;
import portal.ui.IUpdater;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class CheckBox extends Component<IPropertyModel<Boolean>>
{
    private static final long serialVersionUID = 1L;

    private class CheckBoxUpdater extends AbstractFormFieldUpdater
    {
        private static final long serialVersionUID = 1L;

        public void update(String[] values)
        {
            IPropertyModel<Boolean> model = getModel();

            if (values == null)
            {
                model.setValue(Boolean.FALSE);
            }
            else
            {
                model.setValue(Boolean.TRUE);
            }
        }
    }

    public CheckBox()
    {
    }

    public CheckBox(IPropertyModel<Boolean> model)
    {
        super(model);
    }

    @Override
    public IPropertyModel<Boolean> createDefaultModel()
    {
        return new SimplePropertyModel<>(Boolean.FALSE);
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
        PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

        StringBuffer buffer = new StringBuffer();

        if (getCaption() != null)
        {
            drawWithCaption(pRenderContext, buffer);
        }
        else
        {
            drawInputField(pRenderContext, buffer);
        }

        writer.println(buffer.toString());
    }

    private void drawWithCaption(IRenderContext pRenderContext, StringBuffer buffer)
            throws RenderException
    {
        buffer.append("<table cellspacing='0' cellpadding='0' border='0'>");
        buffer.append("<tr>");
        buffer.append("<td>");

        String idKey = drawInputField(pRenderContext, buffer);

        buffer.append("</td>");

        buffer.append("<td onclick=\"document.getElementById('").append(idKey).append("').checked=!document.getElementById('").append(idKey).append("').checked\">");

        buffer.append("<span class ='CheckboxLabel'>");
        buffer.append(getCaption());

        buffer.append("</span>");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("</table>");
    }

    private String drawInputField(IRenderContext pRenderContext, StringBuffer buffer)
            throws RenderException
    {
        IUpdater updater = new CheckBoxUpdater();

        String key = pRenderContext.addUpdater(updater);

        String idKey = "checkbox" + key;

        buffer.append("<input");

        if (!isEnabled())
        {
            addAttribute("disabled", "true", buffer);
        }

        addAttribute("type", "checkbox", buffer);
        addAttribute("name", key, buffer);
        addAttribute("value", "dummy", buffer);
        addAttribute("id", idKey, buffer);

        if (getModel().getValue())
        {
            addAttribute("checked", "checked", buffer);
        }

        buffer.append("/>");

        return idKey;
    }

    private void addAttribute(String name, String value, StringBuffer buffer)
    {
        if (value != null)
        {
            buffer.append(" " + name + "=\"" + value + "\"");
        }
    }
    private String _caption = null;
}
