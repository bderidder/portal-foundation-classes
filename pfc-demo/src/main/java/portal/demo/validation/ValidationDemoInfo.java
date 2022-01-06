package portal.demo.validation;

import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class ValidationDemoInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Form Validation";
	}

	public String getDescription()
	{
		return "An example on how validation can be performed on a form.";
	}

	public DemoEntry createDemoEntry()
	{
		return new ValidationDemoPanel();
	}

}