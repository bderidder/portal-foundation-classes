package portal.demo.validation;

import portal.demo.validation.phone.PhoneNumberForm;
import portal.ui.comp.PanelDelegator;
import portal.ui.comp.TextInput;
import portal.ui.render.FreeMarkerLayout;
import portal.validate.IValidationContext;
import portal.validate.rule.StringNotEmpty;
import portal.validate.rule.ValidationRuleSet;

public class DemoForm extends PanelDelegator<DemoFormModel>
{
	private static final long serialVersionUID = 1L;

	public DemoForm(DemoFormModel demoFormModel)
	{
		super(demoFormModel);

		initUI();

		createValidator();
	}

	public void validate(IValidationContext validationContext)
	{
		_ruleSet.validate(validationContext);
	}

	private void initUI()
	{
		getContentPanel().setLayout(
				new FreeMarkerLayout("portal/demo/validation/DemoForm.ftl"));

		_firstNameInput = new TextInput(getModel().getFirstName());

		_lastNameInput = new TextInput(getModel().getLastName());

		_phoneNumberForm = new PhoneNumberForm();

		getContentPanel().add(_firstNameInput, "FirstName");
		getContentPanel().add(_lastNameInput, "LastName");
		getContentPanel().add(_phoneNumberForm, "PhoneNumber");
	}

	private void createValidator()
	{
		_ruleSet = new ValidationRuleSet();

		StringNotEmpty firstNameNotEmpty = new StringNotEmpty(
				getModel().getFirstName());
		_firstNameInput.setValidationRule(firstNameNotEmpty);

		StringNotEmpty lastNameNotEmpty = new StringNotEmpty(
				getModel().getLastName());
		_lastNameInput.setValidationRule(lastNameNotEmpty);

		_ruleSet.addValidationRule(_firstNameInput);
		_ruleSet.addValidationRule(_lastNameInput);
		_ruleSet.addValidationRule(_phoneNumberForm);
	}
	private TextInput _firstNameInput;
	private TextInput _lastNameInput;
	private PhoneNumberForm _phoneNumberForm;
	private ValidationRuleSet _ruleSet;
}
