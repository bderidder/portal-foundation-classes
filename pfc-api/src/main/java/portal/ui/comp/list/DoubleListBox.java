package portal.ui.comp.list;

import java.io.IOException;
import java.util.Iterator;

import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;
import portal.ui.render.StyleRule;

public final class DoubleListBox<DataType> extends ListComponent<DataType>
{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_SIZE = 5;

	public DoubleListBox(IListDataModel<DataType> model, ISelectionModel selection)
	{
		super(model, selection);

		_size = DEFAULT_SIZE;

		init();
	}

	public final void setSize(int pSize)
	{
		_size = pSize;

		_leftListBox.setSize(_size);
		_rightListBox.setSize(_size);
	}

	public final int getSize()
	{
		return _size;
	}

	public void beforeRender() throws RenderException
	{
		super.beforeRender();

		_leftListBox.setStyleRule(getStyleRule());
		_rightListBox.setStyleRule(getStyleRule());
	}

	public void draw(IRenderContext pRenderContext) throws RenderException, IOException
	{
		pRenderContext.includeComponent(_contentPanel);
	}

	private void init()
	{
		_contentPanel = new Panel();
		_contentPanel.setParent(this);
		_contentPanel.setLayout(new FreeMarkerLayout(
				"portal/comp/list/DoubleListPanel.ftl"));

		createButtons();
		createListBoxes();
	}

	private void createListBoxes()
	{
		_leftSelection = new DefaultSelectionModel(
				SelectionMode.MULTIPLE_SELECTION);
		_rightSelection = new DefaultSelectionModel(
				SelectionMode.MULTIPLE_SELECTION);

		_leftListBox = new ListBox<DataType>(new LeftListDataModel(), _leftSelection);
		_rightListBox = new ListBox<DataType>(new RightListDataModel(), _rightSelection);

		_leftListBox.setSize(getSize());
		_rightListBox.setSize(getSize());

		_leftListBox.setStyleRule(new StyleRule("ListBoxDemo"));
		_rightListBox.setStyleRule(new StyleRule("ListBoxDemo"));

		_contentPanel.add(_leftListBox, "LeftListBox");
		_contentPanel.add(_rightListBox, "RightListBox");
	}

	private void createButtons()
	{
		_toRightButton = new Button();
		_toRightButton.setCaption(">");
		_toRightButton.setAction(new ToRightAction());

		_toLeftButton = new Button();
		_toLeftButton.setCaption("<");
		_toLeftButton.setAction(new ToLeftAction());

		_contentPanel.add(_toRightButton, "ToRightButton");
		_contentPanel.add(_toLeftButton, "ToLeftButton");
	}

	private void rightToLeft()
	{
		/*
		 if item n is selected in the right box, unselect the n-th selected item
		 in the parent selection model
		 */

		Iterator<Integer> parentIt = getSelectionModel().iterator();
		int selectedCount = -1;

		Iterator<Integer> rightIt = _rightSelection.iterator();
		while (rightIt.hasNext())
		{
			int selectedItem = rightIt.next().intValue();

			while (parentIt.hasNext() && (selectedCount != selectedItem))
			{
				selectedCount++;
				int parentSelectedItem = parentIt.next().intValue();

				if (selectedCount == selectedItem)
				{
					getSelectionModel().setSelected(parentSelectedItem, false);
				}
			}
		}

		_leftSelection.clearSelection();
		_rightSelection.clearSelection();
	}

	private void leftToRight()
	{
		/*
		 if item n is selected in the left box, select the n-th unselected item
		 in the parent selection model
		 */

		Iterator<Integer> parentIt = getSelectionModel().iterator();
		int index = -1; // the parent data list item we are currently visiting
		int unselectedCount = -1; // the count of unselected items we already encountered

		int parentSelectedItem = Integer.MAX_VALUE;

		if (parentIt.hasNext())
		{
			parentSelectedItem = parentIt.next().intValue();
		}

		Iterator<Integer> leftIt = _leftSelection.iterator();
		while (leftIt.hasNext())
		{
			int selectedItem = leftIt.next().intValue();

			while (selectedItem != unselectedCount)
			{
				index++;

				if (index != parentSelectedItem)
				{
					unselectedCount++;
				}
				else
				{
					if (parentIt.hasNext())
					{
						parentSelectedItem = parentIt.next().intValue();
					}
					else
					{
						parentSelectedItem = Integer.MAX_VALUE;
					}
				}
			}

			getSelectionModel().setSelected(index, true);
		}

		_leftSelection.clearSelection();
		_rightSelection.clearSelection();
	}
	private int _size;
	private Panel _contentPanel;
	private DefaultSelectionModel _leftSelection;
	private DefaultSelectionModel _rightSelection;
	private ListBox<DataType> _leftListBox;
	private ListBox<DataType> _rightListBox;
	private Button _toRightButton;
	private Button _toLeftButton;

	private class LeftListDataModel implements IListDataModel<DataType>
	{
		private static final long serialVersionUID = 1L;

		public Iterator<DataType> getDataIterator()
		{
			return new FilteredDataListIterator<DataType>(
					getListDataModel().getDataIterator(), getSelectionModel())
			{
				protected boolean isInIterator(DataType dataItem,
						ISelectionModel selectionModel,
						int itemIndex)
				{
					return !selectionModel.isSelected(itemIndex);
				}
			};
		}
	}

	private class RightListDataModel implements IListDataModel<DataType>
	{
		private static final long serialVersionUID = 1L;

		public Iterator<DataType> getDataIterator()
		{
			return new FilteredDataListIterator<DataType>(
					getListDataModel().getDataIterator(), getSelectionModel())
			{
				protected boolean isInIterator(Object dataItem,
						ISelectionModel selectionModel,
						int itemIndex)
				{
					return selectionModel.isSelected(itemIndex);
				}
			};
		}
	}

	private class ToRightAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		public void doAction()
		{
			leftToRight();
		}
	}

	private class ToLeftAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		public void doAction()
		{
			rightToLeft();
		}
	}
}
