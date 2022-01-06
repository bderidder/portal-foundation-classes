package portal.demo.layout;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class SimpleLayoutDemo extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;
	private FreeMarkerDemoPanel _freeMarkerPanel;
	private LayoutDemoPanel _layoutPanel;

	public SimpleLayoutDemo()
	{
	}

	@Override
	public void init()
	{
		setLayout(new FreeMarkerLayout("portal/demo/layout/SimpleLayoutDemo.ftl"));

		_freeMarkerPanel = new FreeMarkerDemoPanel();
		_layoutPanel = new LayoutDemoPanel();

		add(_freeMarkerPanel, "FreeMarkerDemoPanel");
		add(_layoutPanel, "LayoutDemoPanel");
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public Component<?> getComponent()
	{
		return this;
	}
}
