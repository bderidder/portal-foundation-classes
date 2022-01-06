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
package portal.ui.desktop;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.model.IModel;
import portal.ui.Component;
import portal.ui.comp.Panel;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public abstract class Window extends Component<IModel>
{
    private static final long serialVersionUID = 1L;
    private static final Log LOGGER = LogFactory.getLog(Window.class);

    public Window()
    {
        this(null);
    }

    public Window(String name)
    {
        super(name);

        _contentPanel = new Panel();
        _contentPanel.setParent(this);
    }

    public abstract void show();

    public void close()
    {
        LOGGER.debug("close requested");

        notifyClosed();
    }

    public String getTitle()
    {
        if (hasDialog())
        {
            return getTopDialog().getTitle() + " - " + _title;
        }
        else
        {
            return _title;
        }
    }

    public void setTitle(String string)
    {
        _title = string;
    }

    public Panel getContentPanel()
    {
        return _contentPanel;
    }

    public void setContentPanel(Panel panel)
    {
        if (_contentPanel != null)
        {
            _contentPanel.setParent(null);
        }

        _contentPanel = panel;

        _contentPanel.setParent(this);
    }

    @Override
    public final void draw(IRenderContext pRenderContext) throws RenderException, IOException
    {
        if (hasDialog())
        {
            pRenderContext.includeComponent(getTopDialog());
        }
        else
        {
            pRenderContext.includeComponent(_contentPanel);
        }
    }

    final void showDialog(Dialog dialog)
    {
        pushDialog(dialog);

        dialog.addWindowListener(_myWindowListener);
    }

    public boolean hasDialog()
    {
        return !_dialogs.isEmpty();
    }

    private Dialog getTopDialog()
    {
        return _dialogs.getLast();
    }

    private void popDialog()
    {
        if (hasDialog())
        {
            Dialog dialog = _dialogs.removeLast();

            dialog.removeWindowListener(_myWindowListener);
        }
    }

    private void pushDialog(Dialog dialog)
    {
        _dialogs.addLast(dialog);
    }

    public void addWindowListener(WindowListener listener)
    {
        synchronized (_windowListeners)
        {
            _windowListeners.add(listener);
        }
    }

    public void removeWindowListener(WindowListener listener)
    {
        synchronized (_windowListeners)
        {
            _windowListeners.remove(listener);
        }
    }

    private void notifyClosed()
    {
        List<WindowListener> listeners;

        synchronized (_windowListeners)
        {
            listeners = new LinkedList<>(_windowListeners);
        }

        Iterator<WindowListener> it = listeners.iterator();

        WindowEvent event = new WindowEvent(this, this);

        while (it.hasNext())
        {
            WindowListener listener = it.next();

            listener.closed(event);
        }
    }
    private String _title = "Untitled";
    private MyWindowListener _myWindowListener = new MyWindowListener();
    private final List<WindowListener> _windowListeners = new LinkedList<>();
    private final LinkedList<Dialog> _dialogs = new LinkedList<>();
    private Panel _contentPanel;

    public class MyWindowListener implements WindowListener
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void closed(WindowEvent event)
        {
            popDialog();
        }
    }
}
