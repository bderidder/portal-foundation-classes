package portal.demo;

import java.util.EventObject;

import portal.eventbus.EventBus;
import portal.eventbus.EventBusFilter;
import portal.eventbus.IEventBusListener;
import portal.ui.comp.Panel;
import portal.ui.comp.ProxyComponent;

public class DemoManager extends Panel
{
	private static final long serialVersionUID = 1L;

	public DemoManager(EventBus eventBus)
	{
		this._eventBus = eventBus;

		_demoProxy = new ProxyComponent();

		add(_demoProxy);

		initEventBusListener();
	}

	private void initEventBusListener()
	{
		_eventBus.addEventBusListener(new EventBusFilter(StartDemoEvent.class,
				new StartDemoEventListener()));
	}

	private void startDemo(DemoEntryInfo entryInfo)
	{
		if (_currentDemo != null)
		{
			_currentDemo.destroy();
		}

		if (_currentInfo == entryInfo)
		{
			// we are already running this demo !
			return;
		}

		_currentInfo = entryInfo;
		_currentDemo = entryInfo.createDemoEntry();

		_currentDemo.init();

		_demoProxy.setComponent(_currentDemo.getComponent());
	}

	private EventBus _eventBus;

	private ProxyComponent _demoProxy;

	private DemoEntryInfo _currentInfo = null;
	private DemoEntry _currentDemo = null;

	private class StartDemoEventListener implements IEventBusListener
	{
		private static final long serialVersionUID = 1L;

		public void processEvent(EventObject event)
		{
			startDemo(((StartDemoEvent) event).getDemoEntryInfo());
		}
	}
}