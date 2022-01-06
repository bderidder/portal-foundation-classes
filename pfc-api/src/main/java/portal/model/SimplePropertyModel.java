package portal.model;

public class SimplePropertyModel<PropertyType> implements
		IPropertyModel<PropertyType>
{
	private static final long serialVersionUID = 1L;

	/*
	 * We force users of this class to supply a value on construction.
	 * This value can be null.
	 */
	public SimplePropertyModel(PropertyType pValue)
	{
		_propertyValue = pValue;
	}

	public PropertyType getValue()
	{
		return _propertyValue;
	}

	public void setValue(PropertyType pValue)
	{
		_propertyValue = pValue;
	}

	@Override
	public String toString()
	{
		return _propertyValue.toString();
	}

	@Override
	public int hashCode()
	{
		return _propertyValue.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SimplePropertyModel<?>)
		{
			return _propertyValue
					.equals(((SimplePropertyModel<?>) obj)._propertyValue);
		}
		else
		{
			return false;
		}
	}

	private PropertyType _propertyValue;
}
