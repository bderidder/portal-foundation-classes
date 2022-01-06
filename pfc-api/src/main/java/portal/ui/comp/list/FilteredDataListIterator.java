package portal.ui.comp.list;

import java.util.Iterator;

public abstract class FilteredDataListIterator<DataType> implements Iterator<DataType>
{
	private static final long serialVersionUID = 1L;
	
	private static final int END_OF_ITERATOR = Integer.MAX_VALUE;

	public FilteredDataListIterator(Iterator<DataType> dataIterator,
			ISelectionModel selectionModel)
	{
		_dataIterator = dataIterator;
		_selectionModel = selectionModel;

		_currentIndex = 0;
		_currentDataItem = null;

		updateInternalState();
	}

	public boolean hasNext()
	{
		return (_currentIndex != END_OF_ITERATOR);
	}

	public DataType next()
	{
		DataType nextItem = _currentDataItem;

		updateInternalState();

		return nextItem;
	}

	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	private void updateInternalState()
	{
		if (!_dataIterator.hasNext())
		{
			_currentIndex = END_OF_ITERATOR;
			_currentDataItem = null;
		}

		_currentDataItem = null;

		while (_dataIterator.hasNext())
		{
			DataType dataItem = _dataIterator.next();

			if (isInIterator(dataItem, _selectionModel, _currentIndex))
			{
				_currentDataItem = dataItem;

				_currentIndex++;

				break;
			}
			else
			{
				_currentIndex++;
			}
		}

		if (_currentDataItem == null)
		{
			_currentIndex = END_OF_ITERATOR;
			_currentDataItem = null;
		}
	}

	protected abstract boolean isInIterator(DataType dataItem,
			ISelectionModel selectionModel,
			int itemIndex);

	private int _currentIndex;
	private DataType _currentDataItem;

	private Iterator<DataType> _dataIterator;
	private ISelectionModel _selectionModel;
}
