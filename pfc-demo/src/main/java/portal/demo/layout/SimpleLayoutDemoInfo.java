package portal.demo.layout;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class SimpleLayoutDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getShortName()
	{
		return "Simple Layout Sample";
	}

	@Override
	public String getDescription()
	{
		return "Shows how you can work with different layout managers.";
	}

	@Override
	public DemoEntry createDemoEntry()
	{
		return new SimpleLayoutDemo();
	}
}
