package portal.demo;

import java.io.Serializable;

import portal.ui.Component;

public interface DemoEntry extends Serializable
{
	void init();

	void destroy();

	Component<?> getComponent();
}