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
import java.util.ArrayList;
import java.util.List;

import portal.ui.AbstractFormFieldUpdater;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public final class RadioButtonGroup implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private static final int EMPTY_SELECTION = -1;
    private int _selectedButtonIndex;
    private List<RadioButton> _buttons;

    public RadioButtonGroup()
    {
        _buttons = new ArrayList<>();
    }

    public boolean isSelected(RadioButton radioButton)
    {
        int buttonIndex = _buttons.indexOf(radioButton);

        if (buttonIndex == -1)
        {
            throw new IllegalArgumentException(
                    "the radio button is not a member of this group.");
        }
        else
        {
            return buttonIndex == _selectedButtonIndex;
        }
    }

    public void setSelected(RadioButton radioButton, boolean selected)
    {
        int buttonIndex = _buttons.indexOf(radioButton);

        if (buttonIndex != -1)
        {
            if (!selected && (_selectedButtonIndex == buttonIndex))
            {
                _selectedButtonIndex = EMPTY_SELECTION;
            }
            else if (!selected)
            {
                return;
            }
            else
            {
                _selectedButtonIndex = buttonIndex;
            }
        }
        else
        {
            throw new IllegalArgumentException(
                    "the radio button is not a member of this group.");
        }
    }

    public void addRadioButton(RadioButton radioButton)
    {
        if (_buttons.contains(radioButton))
        {
            // prevent indefinite recursion
            return;
        }
        else
        {
            _buttons.add(radioButton);

            //model.setRadioButtonGroup(this);
        }
    }

    public void removeRadioButton(RadioButton radioButton)
    {
        if (!_buttons.contains(radioButton))
        {
            throw new IllegalArgumentException(
                    "the radio button is not a member of this group.");
        }
        else
        {
            int buttonIndex = _buttons.indexOf(radioButton);

            if (_selectedButtonIndex == buttonIndex)
            {
                _selectedButtonIndex = EMPTY_SELECTION;
            }
            else if (_selectedButtonIndex > buttonIndex)
            {
                _selectedButtonIndex--;
            }

            _buttons.remove(_buttons.indexOf(radioButton));
        }
    }

    public void clearSelection()
    {
        _selectedButtonIndex = EMPTY_SELECTION;
    }

    public void renderRadioButton(RadioButton radioButton, IRenderContext pRenderContext)
            throws RenderException
    {
        if (_buttons.indexOf(radioButton) < 0)
        {
            throw new RenderException("This radio button is not in my group");
        }

        PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

        StringBuffer buffer = new StringBuffer();

        if (radioButton.getCaption() != null)
        {
            drawWithCaption(pRenderContext, radioButton, buffer);
        }
        else
        {
            drawInputField(pRenderContext, radioButton, buffer);
        }

        writer.println(buffer.toString());
    }

    private String getName()
    {
        return Integer.toString(hashCode());
    }

    private void drawWithCaption(IRenderContext pRenderContext,
            RadioButton radioButton,
            StringBuffer buffer) throws RenderException
    {
        buffer.append("<table cellspacing='0' cellpadding='0' border='0'>");
        buffer.append("<tr>");
        buffer.append("<td>");

        String idKey = drawInputField(pRenderContext, radioButton, buffer);

        buffer.append("</td>");

        buffer.append("<td onclick=\"document.getElementById('").append(idKey).append("').checked=!document.getElementById('").append(idKey).append("').checked\">");

        buffer.append("<span class ='RadioButtonLabel'>");
        buffer.append(radioButton.getCaption());

        buffer.append("</span>");
        buffer.append("</td>");
        buffer.append("</tr>");
        buffer.append("</table>");
    }

    private String drawInputField(IRenderContext pRenderContext,
            RadioButton radioButton,
            StringBuffer buffer) throws RenderException
    {
        if (!pRenderContext.containsUpdaterKey(getName()))
        {
            pRenderContext.addUpdater(getName(), new RadioButtonUpdater());
        }

        String idKey = "radiobutton" + getName() + radioButton.hashCode();

        int buttonIndex = _buttons.indexOf(radioButton);

        buffer.append("<input");

        addAttribute("type", "radio", buffer);
        addAttribute("name", getName(), buffer);
        addAttribute("id", idKey, buffer);
        addAttribute("value", Integer.toString(buttonIndex), buffer);

        if (buttonIndex == _selectedButtonIndex)
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

    private class RadioButtonUpdater extends AbstractFormFieldUpdater
    {
        private static final long serialVersionUID = 1L;

        public void update(String[] values)
        {
            if (values == null)
            {
                clearSelection();
            }
            else
            {
                String strValue = values[0];

                int iValue = Integer.parseInt(strValue);

                RadioButton radioButton = _buttons.get(iValue);

                setSelected(radioButton, true);
            }
        }
    }
}
