/*
 * Copyright 2000 - 2006, Bavo De Ridder
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
package portal.rt.ui.render;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.model.IModel;
import portal.rt.js.JavaScriptManager;
import portal.rt.servlet.BufferedHttpProcessContext;
import portal.rt.servlet.BufferedHttpServletResponse;
import portal.rt.servlet.IHttpProcessContext;
import portal.rt.servlet.ProcessConstants;
import portal.rt.servlet.RequestParameters;
import portal.rt.ui.ActionMap;
import portal.rt.ui.UpdaterMap;
import portal.rt.ui.resource.ResourceMap;
import portal.services.CachedServiceRef;
import portal.services.ServiceException;
import portal.services.freemarker.IFreeMarkerService;
import portal.services.registry.IntegerKey;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.IUpdater;
import portal.ui.js.IJavaScriptManager;
import portal.ui.render.RenderException;

public class GraphicsDevice implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(GraphicsDevice.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private BrowserDevice _browserDevice;
	private UpdaterMap _updaterMap;
	private ActionMap _actionMap;
	private ResourceMap _resourceMap;
	private JavaScriptManager _javaScriptManager;
	private RootRenderContext _rootRenderContext;
	private static CachedServiceRef<IFreeMarkerService> freeMarkerServiceRef = null;

	public GraphicsDevice(BrowserDevice pBrowserDevice)
	{
		_updaterMap = new UpdaterMap();
		_actionMap = new ActionMap();
		_resourceMap = new ResourceMap();

		_javaScriptManager = new JavaScriptManager();

		_browserDevice = pBrowserDevice;
	}

	public void resetMaps()
	{
		_updaterMap.reset();
		_actionMap.reset();
		_resourceMap.reset();
	}

	public UpdaterMap getUpdaterMap()
	{
		return _updaterMap;
	}

	public ActionMap getActionMap()
	{
		return _actionMap;
	}

	public ResourceMap getResourceMap()
	{
		return _resourceMap;
	}

	public void process(IHttpProcessContext context) throws ServletException,
			IOException
	{
		LOGGER.debug("processing");

		RequestParameters requestParameters = new RequestParameters(context
				.getRequest());

		doUpdate(requestParameters);

		IAction action = getRequestAction(requestParameters);

		if (action != null)
		{
			doAction(action);
		}
		else
		{
			LOGGER.debug("no action found on request");
		}
	}

	public void render(IHttpProcessContext context) throws RenderException
	{
		LOGGER.debug("rendering");

		try
		{
			BufferedHttpProcessContext bufferedContext = new BufferedHttpProcessContext(
					context);

			internalRender(bufferedContext, _browserDevice.getSystemDesktop().getRootComponent());

			streamRenderResponse(context, bufferedContext);
		}
		catch (Throwable t)
		{
			LOGGER.error("Could not render graphics device", t);
		}
	}

	private void internalRender(IHttpProcessContext context,
			Component<?> rootComponent) throws Throwable
	{
		_updaterMap.reset();
		_actionMap.reset();
		_resourceMap.reset();

		IModel model = _browserDevice.getSystemDesktop().getRootComponent()
				.getModel();

		if (model != null)
		{
			context.getRequest().setAttribute("Model", model);
		}

		_rootRenderContext = new RootRenderContext(_browserDevice
				.getSystemDesktop().getRootComponent(), context);
		_rootRenderContext.addExtention(IJavaScriptManager.class,
				_javaScriptManager);

		_javaScriptManager.reset();

		context.getRequest().setAttribute("_RENDERCONTEXT_",
				_rootRenderContext);

		rootComponent.render(_rootRenderContext);
	}

	private void streamRenderResponse(IHttpProcessContext parentContext,
			BufferedHttpProcessContext bufferedContext) throws IOException
	{
		setContentType(bufferedContext, "text/html");
		setCacheHeaders(bufferedContext);

		try
		{
			Configuration fmConfig = getConfiguration();

			Template deviceTemplate = fmConfig.getTemplate("portal/core/render/GraphicsDevice.ftl");

			HashMap<String, Object> dataModel = new HashMap<>();

			dataModel.put("device", new GraphicsDeviceContext(parentContext,
					bufferedContext));

			deviceTemplate.process(dataModel, parentContext.getResponse()
					.getWriter());
		}
		catch (ServiceException | IOException | TemplateException e)
		{
			LOGGER.error("Could not render response", e);
		}
	}

	private void doUpdate(RequestParameters requestParameters)
			throws ServletException
	{
		try
		{
			Iterator<String> it = getUpdaterMap().getKeyIterator();

			while (it.hasNext())
			{
				String key = it.next();

				IUpdater updater = getUpdaterMap().getUpdater(key);

				Object value = requestParameters.getParameterValuesAsObject(key);

				doUpdate(updater, value);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Could not perform update", e);
		}
	}

	private void doUpdate(IUpdater updater, Object value)
	{
		try
		{
			updater.update(value);
		}
		catch (Throwable t)
		{
			LOGGER.error("Could not update updater " + updater, t);
		}
	}

	private IAction getRequestAction(RequestParameters requestParameters)
	{
		IAction action = null;

		Iterator<String> it = getActionMap().getKeyIterator();

		while (it.hasNext())
		{
			String key = it.next();

			// .x and .y are required for image maps and alike
			if ((requestParameters.getParameter(key) != null)
					|| (requestParameters.getParameter(key + ".x") != null)
					|| (requestParameters.getParameter(key + ".y") != null))
			{
				LOGGER.debug("found action key " + key);

				action = getActionMap().getAction(key);

				break;
			}
		}

		return action;
	}

	private void doAction(IAction action)
	{
		LOGGER.debug("found action: " + action);

		long beginTime = System.currentTimeMillis();

		try
		{
			action.doAction();
		}
		catch (Throwable t)
		{
			LOGGER.error("Could not execute action " + action.getClass(), t);
		}
		finally
		{
			long endTime = System.currentTimeMillis();

			LOGGER.debug("process time " + (endTime - beginTime));
		}
	}

	private void setContentType(IHttpProcessContext processContext,
			String contentType)
	{
		processContext.getResponse().setContentType(contentType);
	}

	private void setCacheHeaders(IHttpProcessContext processContext)
	{
		processContext.getResponse().setHeader("Pragma", "No-cache");
		processContext.getResponse().setHeader("Cache-Control", "no-cache");
		processContext.getResponse().setDateHeader("Expires", 1);
	}

	private void pipeResponse(BufferedHttpServletResponse bufferedResponse,
			HttpServletResponse response) throws IOException
	{
		char[] buffer = bufferedResponse.getCharWriter().toCharArray();

		response.getWriter().write(buffer);
	}

	private Configuration getConfiguration() throws ServiceException
	{
		if (freeMarkerServiceRef == null)
		{
			freeMarkerServiceRef = new CachedServiceRef<>(IFreeMarkerService.class);
		}

		return freeMarkerServiceRef.getService()
				.getConfiguration();
	}

	private class GraphicsDeviceContext implements IGraphicsDeviceContext
	{
		private IHttpProcessContext _parentContext;
		private BufferedHttpProcessContext _bufferedContext;

		public GraphicsDeviceContext(IHttpProcessContext parentContext,
				BufferedHttpProcessContext bufferedContext)
		{
			_parentContext = parentContext;
			_bufferedContext = bufferedContext;
		}

		@Override
		public String getTitle()
		{
			return _browserDevice.getSystemDesktop().getTitle();
		}

		@Override
		public int getHeartbeatRefresh()
		{
			return IntegerKey.getValue("portal.core.engine.heartbeatRefresh",
					ProcessConstants.HEARTBEAT_REFRESH);
		}

		@Override
		public void includeHeader() throws IOException
		{
			_javaScriptManager.include(_parentContext.getResponse());
		}

		@Override
		public void includeBody() throws IOException
		{
			pipeResponse(_bufferedContext.getBufferedResponse(), _parentContext
					.getResponse());
		}
	}
}
