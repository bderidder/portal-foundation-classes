package portal.demo;

import java.util.EventObject;

public class StartDemoEvent extends EventObject
{
	private static final long serialVersionUID = 1L;

	public StartDemoEvent(Object source, DemoEntryInfo entryInfo)
	{
		super(source);

		this._entryInfo = entryInfo;
	}

	public DemoEntryInfo getDemoEntryInfo()
	{
		return _entryInfo;
	}

	private DemoEntryInfo _entryInfo;
}