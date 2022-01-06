package portal.demo.desktop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import portal.model.IModel;
import portal.ui.IAction;
import portal.ui.comp.ActionLink;
import portal.ui.comp.PanelDelegator;
import portal.ui.desktop.Desktop;
import portal.ui.desktop.DesktopEvent;
import portal.ui.desktop.DesktopListener;
import portal.ui.desktop.Frame;
import portal.ui.render.FlowLayout;
import portal.ui.render.RenderException;

public class WindowList extends PanelDelegator<IModel>
{
	private static final long serialVersionUID = 1L;

	public WindowList(Desktop desktop)
	{
		this._desktop = desktop;

		_windows = new ArrayList<Frame>();

		desktop.addDesktopListener(new MyDesktopListener());

		getContentPanel().setLayout(new FlowLayout(FlowLayout.VERTICAL));
	}

	public void beforeRender() throws RenderException
	{
		getContentPanel().removeAll();

		Iterator<Frame> it = _windows.iterator();
		while (it.hasNext())
		{
			Frame frame = it.next();

			ActionLink link = new ActionLink();
			link.setCaption(frame.getTitle());
			link.setAction(new FrameSelectAction(frame));

			getContentPanel().add(link);
		}
	}

	private class MyDesktopListener implements DesktopListener
	{
		private static final long serialVersionUID = 1L;

		public void frameAdded(DesktopEvent desktopEvent)
		{
			_windows.add(desktopEvent.getFrame());
		}

		public void frameRemoved(DesktopEvent desktopEvent)
		{
			_windows.remove(desktopEvent.getFrame());
		}
	}

	private class FrameSelectAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		public FrameSelectAction(Frame frame)
		{
			this._frame = frame;
		}

		public void doAction()
		{
			_desktop.setActiveFrame(_frame);
		}

		private Frame _frame;
	}

	private Desktop _desktop;

	private List<Frame> _windows;
}