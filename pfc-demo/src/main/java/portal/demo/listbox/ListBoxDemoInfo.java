package portal.demo.listbox;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class ListBoxDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "List Boxes";
	}

	public String getDescription()
	{
		return "Shows how you can work with list boxes.";
	}

	public DemoEntry createDemoEntry()
	{
		return new ListBoxDemoPanel();
	}

}