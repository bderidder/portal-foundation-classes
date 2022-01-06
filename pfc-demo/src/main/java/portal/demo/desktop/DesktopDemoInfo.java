package portal.demo.desktop;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class DesktopDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Desktop";
	}

	public String getDescription()
	{
		return "A sample usage of a desktop with frames and dialogs.";
	}

	public DemoEntry createDemoEntry()
	{
		return new DesktopDemoPanel();
	}

}