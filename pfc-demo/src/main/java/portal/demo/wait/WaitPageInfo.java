package portal.demo.wait;
import portal.demo.DemoEntry;
import portal.demo.DemoEntryInfo;

public class WaitPageInfo implements DemoEntryInfo
{
	private static final long serialVersionUID = 1L;

	public String getShortName()
	{
		return "Wait Page";
	}

	public String getDescription()
	{
		return "Shows how a friendly wait page is shown when the request takes a longer time.";
	}

	public DemoEntry createDemoEntry()
	{
		return new WaitPagePanel();
	}

}