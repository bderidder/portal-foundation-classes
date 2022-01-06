package portal.demo.validation;

import portal.model.IModel;
import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;

public class DemoFormModel implements IModel
{
	private static final long serialVersionUID = 1L;

	public DemoFormModel()
	{
		_firstNameModel = new SimplePropertyModel<String>(new String());
		_lastNameModel = new SimplePropertyModel<String>(new String());
	}

	public IPropertyModel<String> getFirstName()
	{
		return _firstNameModel;
	}

	public IPropertyModel<String> getLastName()
	{
		return _lastNameModel;
	}

	private IPropertyModel<String> _firstNameModel;
	private IPropertyModel<String> _lastNameModel;
}