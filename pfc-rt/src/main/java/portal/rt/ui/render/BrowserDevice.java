package portal.rt.ui.render;

import portal.rt.ui.SystemDesktop;
import portal.ui.IClientBrowser;

public class BrowserDevice implements IClientBrowser
{
	public BrowserDevice()
	{
	}

	public void init()
	{
		_graphicsDevice = new GraphicsDevice(this);
		_systemDesktop = new SystemDesktop(this);
	}

	public final GraphicsDevice getGraphicsDevice()
	{
		return _graphicsDevice;
	}

	public final SystemDesktop getSystemDesktop()
	{
		return _systemDesktop;
	}

	private GraphicsDevice _graphicsDevice;
	private SystemDesktop _systemDesktop;
}
