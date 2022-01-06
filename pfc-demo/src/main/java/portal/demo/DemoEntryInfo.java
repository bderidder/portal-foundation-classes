package portal.demo;

import java.io.Serializable;

public interface DemoEntryInfo extends Serializable
{
	String getShortName();

	String getDescription();

	DemoEntry createDemoEntry();
}