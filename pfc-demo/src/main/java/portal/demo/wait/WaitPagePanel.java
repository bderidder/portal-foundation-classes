package portal.demo.wait;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class WaitPagePanel extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public WaitPagePanel()
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
		setLayout(new FreeMarkerLayout("portal/demo/wait/WaitPagePanel.ftl"));

		Button startButton = new Button();

		startButton.setCaption("Start Long Operation");
		startButton.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				try
				{
					Thread.sleep(5000);
				}
				catch (InterruptedException e)
				{
					// do nothing
				}
			}
		});

		add(startButton, "StartButton");
	}
}
