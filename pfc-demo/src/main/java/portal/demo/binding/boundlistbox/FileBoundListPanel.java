package portal.demo.binding.boundlistbox;

import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class FileBoundListPanel extends Panel
{
	//private static final Log LOGGER = LogFactory.getLog(VelocityFileBoundList.class);
	private static final long serialVersionUID = 1L;

	public FileBoundListPanel()
	{
		createPanel();
	}

	private void createPanel()
	{
		setLayout(new FreeMarkerLayout(
				"portal/demo/binding/boundlistbox/FileBoundList.ftl"));

//		createNewBinding();
	}
//	private void createNewBinding()
//	{
//		try
//		{
//			//IDataProvider dataSource = new JDBCDataProvider("java:/MyLatteDB");
//			IDataProvider dataSource = new CSVDataProvider();
//
//			IDataProviderSession session = dataSource.createSession();
//
//			IRowset rowset = null;
//
//			if (session.supportsInterface(ICreateCommand.class))
//			{
//				ICreateCommand createCommand = (ICreateCommand) session.getInterface(ICreateCommand.class);
//
//				ICommand cmd = createCommand.createCommand();
//				cmd.setText("select * from latte_user where enable='Y'");
//
//				rowset = cmd.execute();
//			}
//			else if (session.supportsInterface(IOpenRowset.class))
//			{
//				IOpenRowset openRowset = (IOpenRowset) session.getInterface(IOpenRowset.class);
//
//				//rowset = openRowset.openRowset("latte_user");
//				rowset = openRowset.openRowset("/home/bderidder/test.csv");
//			}
//			else
//			{
//				return;
//			}
//
//			//OfflineRowset offlineRowset = new OfflineRowset(rowset);
//
//			//rowset.release();
//			session.close();
//
//			_newBindingBinder = new ListDataModelBinding();
//
//			_newBindingBinder.setRowset(rowset);
//			_newBindingBinder.setColumnIndex(6);
//
//			_newBindingListBox = new DoubleListBox(_newBindingBinder,
//					new DefaultSelectionModel(SelectionMode.MULTIPLE_SELECTION));
//
//			add(_newBindingListBox, "BindingListBox");
//		}
//		catch (BindingException e)
//		{
//			e.printStackTrace();
//		}
//		catch (OptionalInterfaceException e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	private ListDataModelBinding _newBindingBinder;
//	private DoubleListBox _newBindingListBox;
}
