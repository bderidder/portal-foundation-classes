package portal.model;

public interface IPropertyModel<T> extends IModel
{
	T getValue();
	void setValue(T pValue);
}
