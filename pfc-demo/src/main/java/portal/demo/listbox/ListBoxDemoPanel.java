package portal.demo.listbox;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.comp.Panel;
import portal.ui.comp.list.DefaultListDataModel;
import portal.ui.comp.list.DefaultSelectionModel;
import portal.ui.comp.list.DoubleListBox;
import portal.ui.comp.list.SelectionMode;
import portal.ui.render.FreeMarkerLayout;

public class ListBoxDemoPanel extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public ListBoxDemoPanel()
	{
	}

	public void init()
	{
		createListModels();
		createPanel();
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return this;
	}

	private void createPanel()
	{
		setLayout(new FreeMarkerLayout("portal/demo/listbox/ListBoxDemoPanel.ftl"));

		createDoubleListBox();
	}

	private void createListModels()
	{
		_doubleListModel = new DefaultListDataModel<>();
		_doubleListModel.addElement("Jan");
		_doubleListModel.addElement("Piet");
		_doubleListModel.addElement("Peter");
		_doubleListModel.addElement("Koen");
		_doubleListModel.addElement("Frank");
		_doubleListModel.addElement("Koenraad");

		_doubleSelection = new DefaultSelectionModel(
				SelectionMode.MULTIPLE_SELECTION);

		_doubleSelection.setSelected(1, true);
		_doubleSelection.setSelected(3, true);
		_doubleSelection.setSelected(4, true);
		_doubleSelection.setSelected(5, true);
	}

	private void createDoubleListBox()
	{
		_doubleList = new DoubleListBox<>(_doubleListModel, _doubleSelection);

		add(_doubleList, "DoubleListTest");
	}
	private DefaultListDataModel<String> _doubleListModel;
	private DefaultSelectionModel _doubleSelection;
	private DoubleListBox<String> _doubleList;
}
