package portal.demo.binding;

import portal.demo.DemoEntry;
import portal.demo.binding.boundlistbox.FileBoundListPanel;
import portal.ui.Component;

public class BindingLayout implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public BindingLayout()
	{
	}

	public void init()
	{
		_velocityFileBoundList = new FileBoundListPanel();
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return _velocityFileBoundList;
	}

	private FileBoundListPanel _velocityFileBoundList;
}