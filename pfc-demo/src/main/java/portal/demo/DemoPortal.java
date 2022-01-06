package portal.demo;

import java.util.ArrayList;
import java.util.List;

import portal.demo.binding.BindingLayoutInfo;
import portal.demo.controls.ControlsDemoInfo;
import portal.demo.crash.CrashDemoInfo;
import portal.demo.desktop.DesktopDemoInfo;
import portal.demo.listbox.ListBoxDemoInfo;
import portal.demo.validation.ValidationDemoInfo;
import portal.demo.layout.SimpleLayoutDemoInfo;
import portal.demo.wait.WaitPageInfo;
import portal.eventbus.EventBus;
import portal.ui.Component;
import portal.ui.IApplication;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class DemoPortal implements IApplication
{
	private static final long serialVersionUID = 1L;

	public DemoPortal()
	{
	}

	public void init()
	{
		_eventBus = new EventBus();

		_demoManager = new DemoManager(_eventBus);

		_rootPanel = new Panel();
		_rootPanel.setLayout(new FreeMarkerLayout("portal/demo/RootPanel.ftl"));

		_rootPanel.add(_demoManager, "DemoEntries");

		createDemoEntries();
	}

	public String getName()
	{
		return "Portal Demo";
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return _rootPanel;
	}

	private void createDemoEntries()
	{
		List<DemoEntryInfo> demoInfoEntries = new ArrayList<>();

		demoInfoEntries.add(new ControlsDemoInfo());
		demoInfoEntries.add(new SimpleLayoutDemoInfo());
		demoInfoEntries.add(new ListBoxDemoInfo());
		demoInfoEntries.add(new ValidationDemoInfo());
		demoInfoEntries.add(new BindingLayoutInfo());
		demoInfoEntries.add(new DesktopDemoInfo());
		demoInfoEntries.add(new WaitPageInfo());
		demoInfoEntries.add(new CrashDemoInfo());

		_demoChooser = new DemoChooser(_eventBus, demoInfoEntries);

		_rootPanel.add(_demoChooser, "DemoChooser");
	}
	private Panel _rootPanel;
	private EventBus _eventBus;
	private DemoManager _demoManager;
	private DemoChooser _demoChooser;
}
