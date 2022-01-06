package portal.demo.desktop;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Panel;
import portal.ui.desktop.Desktop;
import portal.ui.desktop.Frame;
import portal.ui.render.FreeMarkerLayout;

public class DesktopDemoPanel extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public DesktopDemoPanel()
	{
	}

	public void init()
	{
		createUI();
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return this;
	}

	private void createUI()
	{
		setLayout(new FreeMarkerLayout("portal/demo/desktop/DesktopDemoPanel.ftl"));

		createDesktop();
		createWindowList();
		createAddButton();
	}

	private void createDesktop()
	{
		_rootFrame = new Frame();

		_rootFrame.getContentPanel().setLayout(
				new FreeMarkerLayout("portal/demo/desktop/RootFrame.ftl"));

		_desktop = new Desktop();
		_desktop.setFrameRenderer(new DemoFrameRenderer());
		_desktop.setRootFrame(_rootFrame);

		add(_desktop, "Desktop");
	}

	private void createWindowList()
	{
		_windowList = new WindowList(_desktop);
		add(_windowList, "WindowList");
	}

	private void createAddButton()
	{
		Button closeButton = new Button();

		closeButton.setCaption("New Frame");
		closeButton.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				_desktop.addFrame(createNewFrame());
			}
		});

		add(closeButton, "AddButton");
	}

	private Frame createNewFrame()
	{
		Frame frame = new DemoFrame();

		_frameCount++;

		frame.setTitle("New Frame " + _frameCount);

		return frame;
	}
	private Desktop _desktop;
	private Frame _rootFrame;
	private WindowList _windowList;
	private int _frameCount = 0;
}
