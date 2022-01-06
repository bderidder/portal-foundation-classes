package portal.demo.crash;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class CrashDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Crash Handler";
	}

	public String getDescription()
	{
		return "Show you what can happen when the application throws an exception that is not caught.";
	}

	public DemoEntry createDemoEntry()
	{
		return new CrashDemoPanel();
	}

}