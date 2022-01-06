package portal.ui.js;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.CachedServiceRef;
import portal.services.freemarker.IFreeMarkerService;
import portal.ui.render.RenderException;

public class TemplateScriptBody implements IScriptBody
{
	private static final Log LOGGER = LogFactory.getLog(TemplateScriptBody.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static CachedServiceRef<IFreeMarkerService> freeMarkerServiceRef = null;
	private String _freeMarkerTemplate;
	private Properties _properties;

	public TemplateScriptBody(String pVelocityTemplate, Properties pProperties)
	{
		setFreeMarkerTemplate(pVelocityTemplate);
		_properties = pProperties;
	}

	@Override
	public void streamBody(PrintWriter pWriter) throws RenderException
	{
		mergeTemplate(pWriter, getFreeMarkerTemplate());
	}

	public void mergeTemplate(PrintWriter pWriter,
			String templatePath) throws RenderException
	{
		try
		{
			LOGGER.debug("merging template from resource name " + templatePath);

			Configuration fmConfig = getConfiguration();

			Template template = fmConfig.getTemplate(
					templatePath, DEFAULT_ENCODING);

			HashMap<String, Object> dataModel = new HashMap<>();
			dataModel.put("properties", _properties);

			template.process(dataModel, pWriter);
		}
		catch (RenderException | IOException | TemplateException e)
		{
			throw new RenderException("Could not merge velocity template: "
					+ templatePath, e);
		}
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

	private String getFreeMarkerTemplate()
	{
		return _freeMarkerTemplate;
	}
}
