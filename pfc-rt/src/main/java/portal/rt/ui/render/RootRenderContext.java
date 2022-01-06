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
package portal.rt.ui.render;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.interfaces.IOptionalInterface;
import portal.interfaces.InterfaceRuntime;
import portal.interfaces.InterfaceRuntimeException;
import portal.interfaces.NotAnOptionalInterfaceException;
import portal.interfaces.StaticFactory;
import portal.model.IModel;
import portal.rt.servlet.IHttpProcessContext;
import portal.rt.ui.ActionMap;
import portal.rt.ui.UpdaterMap;
import portal.rt.ui.resource.ResourceMap;
import portal.rt.ui.resource.ResourceUtils;
import portal.services.resource.IResource;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.IUpdater;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class RootRenderContext extends InterfaceRuntime implements IRenderContext
{
    private static final Log LOGGER = LogFactory.getLog(RootRenderContext.class);
    private IHttpProcessContext _processContext;
    private Writer _writer;

    public RootRenderContext(Component<?> component,
            IHttpProcessContext processContext) throws RenderException
    {
        _processContext = processContext;

        try
        {
            _writer = processContext.getResponse().getWriter();
        }
        catch (IOException e)
        {
            throw new RenderException(
                    "Failed to construct a ServletRenderContext", e);
        }
    }

    public void addExtention(Class<? extends IOptionalInterface> pInterface, IOptionalInterface pImplementor)
            throws NotAnOptionalInterfaceException, InterfaceRuntimeException
    {
        registerInterface(pInterface, new StaticFactory(pImplementor));
    }

    @Override
    public Writer getWriter()
    {
        return _writer;
    }

    @Override
    public String addUpdater(IUpdater updater)
    {
        return getUpdaterMap().addUpdater(updater);
    }

    @Override
    public void addUpdater(String key, IUpdater updater)
    {
        getUpdaterMap().addUpdater(key, updater);
    }

    @Override
    public boolean containsUpdaterKey(String key)
    {
        return getUpdaterMap().containsKey(key);
    }

    @Override
    public String createActionUrl(IAction action) throws RenderException
    {
        ActionMap actionMap = getActionMap();

        String key = actionMap.addAction(action);

        return "javascript:pfc.submitFormWithAction('" + key + "');";
    }

    @Override
    public String createResourceUrl(IResource resource) throws RenderException
    {
        ResourceMap resourceMap = getResourceMap();

        String key = resourceMap.addResource(resource);

        return ResourceUtils.createResourceURL(false, key);
    }

    @Override
    public String createDownloadUrl(IResource resource, String filename)
            throws RenderException
    {
        ResourceMap resourceMap = getResourceMap();

        String key = resourceMap.addResource(resource);

        return ResourceUtils.createDownloadURL(false, key, filename);
    }

    @Override
    public void includeComponent(Component<?> childComponent)
            throws RenderException, IOException
    {
        if (childComponent == null)
        {
            throw new RenderException("Child was null, cannot include");
        }

        if (childComponent.isVisible())
        {
            IModel oldModel = (IModel) _processContext.getRequest()
                    .getAttribute("Model");

            IModel model = childComponent.getModel();

            _processContext.getRequest().setAttribute("Model", model);

            childComponent.render(this);

            if (oldModel != null)
            {
                _processContext.getRequest().setAttribute("Model", model);
            }
            else
            {
                _processContext.getRequest().removeAttribute("Model");
            }
        }
    }

    private UpdaterMap getUpdaterMap()
    {
        return _processContext.getBrowserDevice().getGraphicsDevice()
                .getUpdaterMap();
    }

    private ActionMap getActionMap()
    {
        return _processContext.getBrowserDevice().getGraphicsDevice()
                .getActionMap();
    }

    private ResourceMap getResourceMap()
    {
        return _processContext.getBrowserDevice().getGraphicsDevice()
                .getResourceMap();
    }
}
