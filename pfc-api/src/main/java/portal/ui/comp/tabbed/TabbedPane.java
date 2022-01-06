/*
 * Copyright 2000 - 2004, Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Portal Foundation Classes; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */
package portal.ui.comp.tabbed;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import portal.model.IModel;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;
import portal.validate.IValidatable;
import portal.validate.IValidationContext;

public class TabbedPane extends Component<IModel> implements IValidatable
{
    private static final long serialVersionUID = 1L;

    public TabbedPane()
    {
        _tabs = new ArrayList<InternalTab>();

        _selectedTabIndex = -1;
    }

    public Tab add(String title, Component<?> component)
    {
        SimpleTab tab = new SimpleTab(title, component);

        add(tab);

        return tab;
    }

    public void add(Tab newTab)
    {
        newTab.getComponent().setParent(this);

        InternalTab internalTab = new InternalTab(newTab);

        _tabs.add(internalTab);

        if (_selectedTabIndex == -1)
        {
            _selectedTabIndex = 0;

            internalTab.beforeActive();
        }
    }

    public boolean removeTab(Tab removeTab)
    {
        int index = searchTab(removeTab);

        if (index != -1)
        {
            if (index == _selectedTabIndex)
            {
                removeTab.afterActive();
            }

            _tabs.remove(index);

            if (_selectedTabIndex >= _tabs.size())
            {
                _selectedTabIndex = _tabs.size() - 1;

                Tab newSelectedTab = (Tab) _tabs.get(_selectedTabIndex);

                newSelectedTab.beforeActive();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int getIndexOfTab(Tab tab)
    {
        return searchTab(tab);
    }

    public int size()
    {
        return _tabs.size();
    }

    public int getSelectedIndex()
    {
        return _selectedTabIndex;
    }

    public void setSelectedTab(Tab tab)
    {
        int indexOfTab = getIndexOfTab(tab);

        if (indexOfTab == -1)
        {
            throw new IllegalArgumentException(
                    "The tab is not a member of this tabbed pane.");
        }

        setSelectedTab(getIndexOfTab(tab));
    }

    public void setSelectedTab(int index)
    {
        if (_selectedTabIndex >= _tabs.size())
        {
            throw new IllegalArgumentException("There are only " + _tabs.size()
                    + " tabs. Could not select tab " + index);
        }

        if (index == _selectedTabIndex)
        {
            return;
        }

        Tab oldSelectedTab = (Tab) _tabs.get(_selectedTabIndex);
        Tab newSelectedTab = (Tab) _tabs.get(index);

        oldSelectedTab.afterActive();
        newSelectedTab.beforeActive();

        _selectedTabIndex = index;
    }

    public Iterator<InternalTab> iterator()
    {
        return _tabs.iterator();
    }

    public void validate(IValidationContext validationContext)
    {
        Iterator<InternalTab> tabIterator = iterator();

        while (tabIterator.hasNext())
        {
            InternalTab tab = tabIterator.next();

            tab.validate(validationContext);
        }
    }

    public void draw(IRenderContext pRenderContext) throws RenderException, IOException
    {
        Iterator<InternalTab> tabIterator = iterator();

        int selectedIndex = getSelectedIndex();
        int currentTab = 0;
        Tab selectedTab = null;

        PrintWriter out = new PrintWriter(pRenderContext.getWriter());

        out.println("<table class='TabbedPane' cellspacing='0' cellpadding='0'>");
        out.println("<tr>");
        out.println("<td>");

        out.println("<table style='border-collapse: collapse;' cellspacing='0' cellpadding='0'>");
        out.println("<tr>");

        while (tabIterator.hasNext())
        {
            InternalTab internalTab = tabIterator.next();

            if (currentTab == selectedIndex)
            {
                selectedTab = internalTab;

                out.println("<td class='SelectedTab' valign='middle' align='center'>");
                out.println("<nobr class='TabSelector'>");
                renderTabTitle(internalTab, out);
                out.println("</nobr>");
                out.println("</td>");
            }
            else
            {
                String actionUrl = pRenderContext.createActionUrl(new SelectedAction(currentTab));

                out.println("<td class='NormalTab' valign='middle' align='center'>");
                out.println("<nobr class='TabSelector'>");
                out.println("<a href=\"" + actionUrl
                        + "\" class='TabSelector'>");
                renderTabTitle(internalTab, out);
                out.println("</a>");
                out.println("</nobr>");
                out.println("</td>");
            }

            currentTab++;
        }

        out.println("</tr>");
        out.println("</table>");

        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td class='TabbedContent'>");

        if (selectedTab != null)
        {
            pRenderContext.includeComponent(selectedTab.getComponent());
        }

        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    }

    private void renderTabTitle(InternalTab tab, PrintWriter out)
    {
        if (tab.isValid())
        {
            out.println(tab.getTitle());
        }
        else
        {
            out.println("<span class='TabTitleError'>" + tab.getTitle()
                    + "</span>");
        }
    }

    private int searchTab(Tab tab)
    {
        int index = -1;

        for (int i = 0; i != _tabs.size(); i++)
        {
            InternalTab internalTab = _tabs.get(i);

            if (internalTab.getChildTab() == tab)
            {
                index = i;
                break;
            }
        }

        return index;
    }

    private class SelectedAction implements IAction
    {
        private static final long serialVersionUID = 1L;

        public SelectedAction(int index)
        {
            this._index = index;
        }

        public void doAction()
        {
            validate(null);
            setSelectedTab(_index);
        }
        private int _index;
    }
    private int _selectedTabIndex;
    private List<InternalTab> _tabs;
}
