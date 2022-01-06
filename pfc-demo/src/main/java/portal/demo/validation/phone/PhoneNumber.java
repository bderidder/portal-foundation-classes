package portal.demo.validation.phone;

import portal.model.IModel;
import portal.model.IPropertyModel;
import portal.model.SimplePropertyModel;

public class PhoneNumber implements IModel
{
	private static final long serialVersionUID = 1L;
	
	public PhoneNumber()
	{
		_countryCode = new SimplePropertyModel<String>(new String());
		_areaCode = new SimplePropertyModel<String>(new String());
		_localNumber = new SimplePropertyModel<String>(new String());
	}

	public IPropertyModel<String> getCountryCode()
	{
		return _areaCode;
	}

	public IPropertyModel<String> getAreaCode()
	{
		return _countryCode;
	}

	public IPropertyModel<String> getLocalNumber()
	{
		return _localNumber;
	}

	private IPropertyModel<String> _countryCode;
	private IPropertyModel<String> _areaCode;
	private IPropertyModel<String> _localNumber;
}