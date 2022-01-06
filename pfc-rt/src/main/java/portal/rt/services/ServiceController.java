/*
 * Copyright 2000 - 2004,  Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */

package portal.rt.services;

import java.util.ArrayList;
import java.util.EventObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.eventbus.EventBusFilter;
import portal.eventbus.IEventBusListener;
import portal.services.IPeerServiceController;
import portal.services.IService;
import portal.services.ServiceException;
import portal.services.ServiceState;

public class ServiceController
{
	private static final Log LOGGER = LogFactory.getLog(ServiceController.class);

	public ServiceController(ServiceManager serviceManager,
			ServiceXMLConfiguration serviceConfig) throws ServiceException
	{
		this._serviceManager = serviceManager;
		this._serviceConfig = serviceConfig;

		initEventBusListener();
		initDependencies();
	}

	public boolean implementsInterface(Class<?> interfaceClass)
	{
		Class<?>[] interfaces = _serviceConfig.getInterfaceClasses();

		for (int i = 0; i != interfaces.length; i++)
		{
			if (interfaceClass.isAssignableFrom(interfaces[i]))
			{
				return true;
			}
		}

		return false;
	}

	public ServiceState getServiceStatus()
	{
		return _currentState;
	}

	public void reload() throws ServiceException
	{
		_controller.reload();
	}

	public void start() throws ServiceException
	{
		checkPendingState();
	}

	public void stop() throws ServiceException
	{
		if (getServiceStatus() == ServiceState.RUNNING)
		{
			_controller.stop();
		}

		setNewState(ServiceState.STOPPED);
	}

	public IService getService() throws ServiceException
	{
		return _controller.getService();
	}

	void init() throws ServiceException
	{
		try
		{
			_controller = (IPeerServiceController) _serviceConfig.getControllerClass()
					.newInstance();

			_controller.init(_serviceConfig);

			setNewState(ServiceState.PENDING);

			start();
		}
		catch (IllegalAccessException e)
		{
			throw new ServiceException(
					"Illegal access while instantiating controller class", e);
		}
		catch (InstantiationException e)
		{
			throw new ServiceException("Cannot instantiate controller class", e);
		}
	}

	private void setNewState(ServiceState newState)
	{
		ServiceState oldState = _currentState;

		_currentState = newState;

		LOGGER.debug("[" + _serviceConfig.getServiceName()
				+ "] changed state from " + oldState + " to " + newState);

		postStateChangedEvent(new StateChangedEvent(this,
				_serviceConfig.getInterfaceClasses(), oldState, newState));
	}

	private void initDependencies() throws ServiceException
	{
		Class<?>[] dependencies = _serviceConfig.getDependencies();

		if (dependencies == null)
		{
			LOGGER.debug("No dependencies");

			return;
		}

		for (int i = 0; i != dependencies.length; i++)
		{
			LOGGER.debug("Adding dependency " + dependencies[i].getName());

			ServiceController controller = _serviceManager.getInstanceManagerForService(dependencies[i]);

			if (controller == null)
			{
				_waitDeps.add(dependencies[i]);
			}
			else
			{
				if (controller.getServiceStatus() == ServiceState.RUNNING)
				{
					_goodDeps.add(dependencies[i]);
				}
				else
				{
					_waitDeps.add(dependencies[i]);
				}
			}
		}

		LOGGER.debug(_serviceConfig.getServiceName() + " waitDeps.size "
				+ _waitDeps.size());
		LOGGER.debug(_serviceConfig.getServiceName() + " goodDeps.size "
				+ _goodDeps.size());
	}

	private void checkDependencies(StateChangedEvent event)
	{
		try
		{
			Class<?>[] interfaceClasses = event.getServiceClasses();

			for (int i = 0; i != interfaceClasses.length; i++)
			{
				Class<?> interfaceClass = interfaceClasses[i];

				if ((event.getNewState() == ServiceState.RUNNING)
						&& _waitDeps.contains(interfaceClass))
				{
					_waitDeps.remove(interfaceClass);
					_goodDeps.add(interfaceClass);
				}
				else if (((event.getNewState() == ServiceState.PENDING) || (event.getNewState() == ServiceState.STOPPED))
						&& _goodDeps.contains(interfaceClass))
				{
					_goodDeps.remove(interfaceClass);
					_waitDeps.add(interfaceClass);
				}
			}

			if ((getServiceStatus() == ServiceState.RUNNING)
					&& _waitDeps.size() != 0)
			{
				_controller.stop();

				setNewState(ServiceState.PENDING);
			}
			else if ((getServiceStatus() == ServiceState.PENDING)
					&& _waitDeps.size() == 0)
			{
				_controller.start();

				setNewState(ServiceState.RUNNING);
			}
		}
		catch (ServiceException e)
		{
			LOGGER.error("Could not check dependencies", e);
		}
	}

	private void checkPendingState() throws ServiceException
	{
		if ((_waitDeps.size() == 0)
				&& getServiceStatus() == ServiceState.PENDING)
		{
			LOGGER.info("Starting service " + _serviceConfig.getServiceName());

			_controller.start();

			setNewState(ServiceState.RUNNING);
		}
	}

	private void postStateChangedEvent(StateChangedEvent event)
	{
		_serviceManager.getServicesEventBus().postEvent(event);
	}

	private void initEventBusListener()
	{
		_serviceManager.getServicesEventBus().addEventBusListener(
				new EventBusFilter(StateChangedEvent.class,
						new DependencyStateChangedListener()));
	}

	private ServiceManager _serviceManager;
	private ServiceXMLConfiguration _serviceConfig;

	private IPeerServiceController _controller;

	private ServiceState _currentState = ServiceState.STOPPED;

	private ArrayList<Class<?>> _waitDeps = new ArrayList<Class<?>>();
	private ArrayList<Class<?>> _goodDeps = new ArrayList<Class<?>>();

	private class DependencyStateChangedListener implements IEventBusListener
	{
		private static final long serialVersionUID = 1L;

		public void processEvent(EventObject eventObject)
		{
			if (eventObject instanceof StateChangedEvent)
			{
				checkDependencies((StateChangedEvent) eventObject);
			}
		}
	}
}