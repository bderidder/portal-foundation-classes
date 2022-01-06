package portal.demo.validation.address;

import portal.model.IModel;
import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;

public class StreetAddress implements IModel
{
	private static final long serialVersionUID = 1L;
	
	public StreetAddress()
	{
		_street = new SimplePropertyModel<String>(new String());
		_city = new SimplePropertyModel<String>(new String());
	}

	public IPropertyModel<String> getStreet()
	{
		return _street;
	}

	public IPropertyModel<String> getCity()
	{
		return _city;
	}

	private IPropertyModel<String> _street;
	private IPropertyModel<String> _city;
}