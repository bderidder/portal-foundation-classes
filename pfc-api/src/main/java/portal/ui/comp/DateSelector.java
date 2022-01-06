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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;
import portal.ui.AbstractFormFieldUpdater;
import portal.ui.form.FormElement;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class DateSelector extends FormElement<IPropertyModel<Calendar>>
{
    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy");

    private class DateSelectorUpdater extends AbstractFormFieldUpdater
    {
        private static final long serialVersionUID = 1L;

        public void update(String[] values)
        {
            if (values != null)
            {
                getModel().setValue(stringToCalendar(values[0]));
            }
        }
    }

    public DateSelector()
    {
        super();
    }

    public DateSelector(IPropertyModel<Calendar> model)
    {
        super(model);
    }

    public IPropertyModel<Calendar> getCalendarModel()
    {
        return getModel();
    }

    @Override
    public IPropertyModel<Calendar> createDefaultModel()
    {
        return new SimplePropertyModel<>(Calendar.getInstance());
    }

    @Override
    public void draw(IRenderContext pRenderContext) throws RenderException
    {
        PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

        String updaterKey = pRenderContext.addUpdater(new DateSelectorUpdater());

        StringBuffer buffer = new StringBuffer();

        buffer.append("<table cellspacing='0' cellpadding='0' border='0'>");
        buffer.append("<tr>");
        buffer.append("<td>");

        buffer.append("<input");

        addAttribute("type", "text", buffer);

        addAttribute("name", updaterKey, buffer);
        addAttribute("id", updaterKey, buffer);
        addAttribute("value",
                calendarToString(getModel().getValue()), buffer);
        addAttribute("readonly", "1", buffer);

        if (!isValid())
        {
            addAttribute("class", "TextInputError", buffer);
        }
        else
        {
            addAttribute("class", "TextInputNormal", buffer);
        }

        buffer.append("/>");

        buffer.append("</td>");
        buffer.append("<td>");

        String calTriggerUrl = pRenderContext.createResourceUrl(ResourceSet.CALENDAR_TRIGGER);

        buffer.append(MessageFormat.format("<img src='{0}' id='trigger_{1}' style='cursor: pointer;' title='Date selector'/>", calTriggerUrl, updaterKey));

        if (isEnabled())
        {
            buffer.append("<script type='text/javascript'>");
            buffer.append("	Calendar.setup({");
            buffer.append("		inputField     :    '").append(updaterKey).append("',");
            buffer.append("		ifFormat       :    '%d/%m/%Y',");
            buffer.append("		button         :    'trigger_").append(updaterKey).append("',");
            buffer.append("		align          :    'Bl',");
            buffer.append("		singleClick    :    true,");
            buffer.append("		firstDay       :    1");
            buffer.append("	});");
            buffer.append("</script>");
        }

        buffer.append("</tr>");
        buffer.append("</table>");

        writer.println(buffer.toString());
    }

    private void addAttribute(String name, String value, StringBuffer buffer)
    {
        if (value != null)
        {
            buffer.append(MessageFormat.format(" {0}=\"{1}\"", name, value));
        }
    }

    private String calendarToString(Calendar pCalendar)
    {
        if (pCalendar == null)
        {
            return "";
        }

        return dateFormat.format(pCalendar.getTime());
    }

    private Calendar stringToCalendar(String pString)
    {
        try
        {
            Calendar cal = Calendar.getInstance();

            cal.setTime(dateFormat.parse(pString));

            return cal;
        }
        catch (ParseException e)
        {
            return null;
        }
    }
}
