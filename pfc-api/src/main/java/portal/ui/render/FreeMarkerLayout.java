/*
 * Copyright 2013,  Bavo De Ridder
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
package portal.ui.render;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.model.IModel;
import portal.services.CachedServiceRef;
import portal.services.freemarker.IFreeMarkerService;
import portal.services.resource.LazyResource;
import portal.ui.Component;
import portal.ui.Container;

public class FreeMarkerLayout implements LayoutManager
{
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(FreeMarkerLayout.class);
	private static final String DEFAULT_ENCODING = "ISO-8859-15";
	private static CachedServiceRef<IFreeMarkerService> freeMarkerServiceRef = null;
	private String _freeMarkerTemplate;

	public FreeMarkerLayout(String freeMarkerTemplate)
	{
		setFreeMarkerTemplate(freeMarkerTemplate);
	}

	private void setFreeMarkerTemplate(String freeMarkerTemplate)
	{
		if (freeMarkerTemplate == null || freeMarkerTemplate.length() == 0)
		{
			throw new IllegalArgumentException(
					"The FreeMarker template was either null or empty");
		}

		if (freeMarkerTemplate.startsWith("/"))
		{
			LOGGER.warn("template location starts with a '/' : "
					+ freeMarkerTemplate);
		}

		this._freeMarkerTemplate = freeMarkerTemplate;
	}

	public String getFreeMarkerTemplate()
	{
		return _freeMarkerTemplate;
	}

	@Override
	public void doLayout(Component<?> pTarget, IRenderContext pRenderContext)
			throws RenderException
	{
		mergeTemplate(pTarget, pRenderContext, getFreeMarkerTemplate());
	}

	public void mergeTemplate(Component<?> pTarget,
			IRenderContext pRenderContext,
			String templatePath) throws RenderException
	{
		try
		{
			LOGGER.debug("merging template from resource name " + templatePath);

			Configuration fmConfig = getConfiguration();

			Template fmTemplate = fmConfig.getTemplate(
					templatePath, DEFAULT_ENCODING);

			mergeTemplate(pTarget, pRenderContext, fmTemplate);
		}
		catch (Exception e)
		{
			throw new RenderException("Could not merge velocity template: "
					+ templatePath, e);
		}
	}

	private void mergeTemplate(Component<?> pTarget,
			IRenderContext pRenderContext,
			Template template) throws Exception
	{
		HashMap<String, Object> dataModel = new HashMap<>();

		IModel currentModel = pTarget.getModel();
		if (currentModel != null)
		{
			dataModel.put("model", currentModel);
		}

		PageContext pageContext = new PageContext(pTarget, pRenderContext);

		// this is the preferred name
		dataModel.put("context", pageContext);

		template.process(dataModel, pRenderContext.getWriter());
	}

	private Configuration getConfiguration() throws RenderException
	{
		if (freeMarkerServiceRef == null)
		{
			try
			{
				freeMarkerServiceRef = new CachedServiceRef<>(IFreeMarkerService.class);
			}
			catch (Exception e)
			{
				throw new RenderException("Could not get velocity engine");
			}
		}

		return freeMarkerServiceRef.getService().getConfiguration();
	}

	private class PageContext implements IVelocityPageContext
	{
		private Component<?> _target;
		private IRenderContext _renderContext;

		public PageContext(Component<?> pTarget, IRenderContext pRenderContext)
		{
			_target = pTarget;
			_renderContext = pRenderContext;
		}

		@Override
		public Component<?> getComponent()
		{
			return _target;
		}

		@Override
		public boolean hasChild(String componentName)
		{
			if (isContainer())
			{
				Container container = (Container) getComponent();

				return (container.getComponent(componentName) != null);
			}
			else
			{
				LOGGER.warn("Cannot check for the existance of a child named "
						+ componentName
						+ ", current component is not a container.");

				return false;
			}
		}

		@Override
		public void includeChild(String componentName) throws RenderException
		{
			if (!isContainer())
			{
				LOGGER.warn("Cannot include child named " + componentName
						+ ", current component is not a container.");
			}
			else
			{
				try
				{
					Container container = (Container) getComponent();

					Component<?> child = container.getComponent(componentName);

					_renderContext.includeComponent(child);
				}
				catch (RenderException | IOException e)
				{
					LOGGER.error("Could not include child " + componentName, e);
				}
			}
		}

		@Override
		public String getResourceURL(String resourceName)
				throws RenderException
		{
			LazyResource resource = new LazyResource(resourceName);

			return _renderContext.createResourceUrl(resource);
		}

		@Override
		public String getDownloadURL(String resourceName, String filename)
				throws RenderException
		{
			LazyResource resource = new LazyResource(resourceName);

			return _renderContext.createDownloadUrl(resource, filename);
		}

		private boolean isContainer()
		{
			return _target instanceof Container;
		}
	}
}
