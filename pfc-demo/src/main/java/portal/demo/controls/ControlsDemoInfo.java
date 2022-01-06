package portal.demo.controls;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class ControlsDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Basic Controls";
	}

	public String getDescription()
	{
		return "A list of basic controls like text input, check boxes ...";
	}

	public DemoEntry createDemoEntry()
	{
		return new ControlsDemoPanel();
	}

}