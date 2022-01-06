package portal.demo.binding;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class BindingLayoutInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Binding";
	}

	public String getDescription()
	{
		return "Shows you how binding works.";
	}

	public DemoEntry createDemoEntry()
	{
		return new BindingLayout();
	}

}