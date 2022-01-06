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
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class Desktop extends Component<IModel>
{
    private static final long serialVersionUID = 1L;
    private static final Log LOGGER = LogFactory.getLog(Desktop.class);
    private Frame _rootFrame;
    private LinkedList<Frame> _frames = new LinkedList<>();
    private final List<DesktopListener> _desktopListeners = new LinkedList<>();
    private FrameRenderer _frameRenderer;
    private MyWindowListener _desktopListener = new MyWindowListener();

    public Desktop()
    {
        this(null, new SimpleFrameRenderer());
    }

    public Desktop(String name)
    {
        this(name, new SimpleFrameRenderer());
    }

    public Desktop(FrameRenderer frameRenderer)
    {
        this(null, frameRenderer);
    }

    public Desktop(String name, FrameRenderer frameRenderer)
    {
        super(name);

        this._frameRenderer = frameRenderer;
    }

    public void setFrameRenderer(FrameRenderer frameRenderer)
    {
        this._frameRenderer = frameRenderer;
    }

    public void setRootFrame(Frame frame)
    {
        if (_rootFrame != null)
        {
            _rootFrame.setParent(null);
        }

        _rootFrame = frame;
        frame.setParent(this);
    }

    public void addFrame(Frame frame)
    {
        frame.setParent(this);
        frame.addWindowListener(_desktopListener);

        pushFrame(frame);

        notifyAdded(frame);
    }

    public void setActiveFrame(Frame frame)
    {
        int index = _frames.indexOf(frame);

        if (index >= 0)
        {
            _frames.remove(index);
            _frames.addLast(frame);
        }
    }

    public boolean hasFrames()
    {
        return !_frames.isEmpty();
    }

    @Override
    public final void draw(IRenderContext pRenderContext) throws RenderException, IOException
    {

        if (hasFrames())
        {
            if (_frameRenderer == null)
            {
                LOGGER.error("No FrameRenderer instance set, cannot render frame");
            }
            else
            {
                _frameRenderer.renderFrame(pRenderContext, getTopFrame());
            }
        }
        else if (_rootFrame != null)
        {
            pRenderContext.includeComponent(_rootFrame);
        }
    }

    public void addDesktopListener(DesktopListener listener)
    {
        synchronized (_desktopListeners)
        {
            _desktopListeners.add(listener);
        }
    }

    public void removeDesktopListener(DesktopListener listener)
    {
        synchronized (_desktopListeners)
        {
            _desktopListeners.remove(listener);
        }
    }

    private void notifyAdded(Frame frame)
    {
        List<DesktopListener> listeners;

        synchronized (_desktopListeners)
        {
            listeners = new LinkedList<>(_desktopListeners);
        }

        Iterator<DesktopListener> it = listeners.iterator();

        DesktopEvent event = new DesktopEvent(this, this, frame);

        while (it.hasNext())
        {
            DesktopListener listener = it.next();

            listener.frameAdded(event);
        }
    }

    private void notifyRemoved(Frame frame)
    {
        List<DesktopListener> listeners;

        synchronized (_desktopListeners)
        {
            listeners = new LinkedList<>(_desktopListeners);
        }

        Iterator<DesktopListener> it = listeners.iterator();

        DesktopEvent event = new DesktopEvent(this, this, frame);

        while (it.hasNext())
        {
            DesktopListener listener = it.next();

            listener.frameRemoved(event);
        }
    }

    private Frame getTopFrame()
    {
        return _frames.getLast();
    }

    private void pushFrame(Frame frame)
    {
        _frames.addLast(frame);
    }

    private void removeFrame(Frame frame)
    {
        LOGGER.debug("Frame removed: " + frame);

        if (_frames.remove(frame))
        {
            frame.setParent(null);

            notifyRemoved(frame);
        }
    }

    public class MyWindowListener implements WindowListener
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void closed(WindowEvent event)
        {
            Frame frame = (Frame) event.getWindow();

            frame.removeWindowListener(_desktopListener);

            removeFrame(frame);
        }
    }
}
