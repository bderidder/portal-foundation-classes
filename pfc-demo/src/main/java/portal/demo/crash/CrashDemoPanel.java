package portal.demo.crash;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class CrashDemoPanel extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public CrashDemoPanel()
	{
	}

	public void init()
	{
		createPanel();
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return this;
	}

	private void createPanel()
	{
		setLayout(new FreeMarkerLayout("portal/demo/crash/CrashDemoPanel.ftl"));

		Button crashMeButton = new Button();

		crashMeButton.setCaption("Crash Me");
		crashMeButton.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				throw new NullPointerException("You wanted me to crash.");
			}
		});

		add(crashMeButton, "CrashMeButton");
	}
}
