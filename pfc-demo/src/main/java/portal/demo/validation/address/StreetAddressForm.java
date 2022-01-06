package portal.demo.validation.address;

import portal.ui.comp.PanelDelegator;
import portal.ui.comp.TextInput;
import portal.validate.IValidatable;
import portal.validate.IValidationContext;
import portal.validate.rule.StringNotEmpty;
import portal.validate.rule.ValidationRuleSet;

public class StreetAddressForm extends PanelDelegator<StreetAddress> implements IValidatable
{
	private static final long serialVersionUID = 1L;

	public StreetAddressForm()
	{
		this(new StreetAddress());
	}

	public StreetAddressForm(StreetAddress streetAddress)
	{
		super(streetAddress);

		initUI();
		createValidation();
	}

	public StreetAddress getStreetAddress()
	{
		return getModel();
	}

	public void validate(IValidationContext context)
	{
		_validateble.validate(context);
	}

	private void initUI()
	{
		_streetInput = new TextInput(getStreetAddress().getStreet());

		_cityInput = new TextInput(getStreetAddress().getCity());

		getContentPanel().add(_streetInput);
		getContentPanel().add(_cityInput);
	}

	private void createValidation()
	{
		StringNotEmpty stringNotEmpty;

		stringNotEmpty = new StringNotEmpty(_streetInput.getModel());
		_streetInput.setValidationRule(stringNotEmpty);

		stringNotEmpty = new StringNotEmpty(_cityInput.getModel());
		_cityInput.setValidationRule(stringNotEmpty);

		ValidationRuleSet ruleSet = new ValidationRuleSet();

		ruleSet.addValidationRule(_streetInput);
		ruleSet.addValidationRule(_cityInput);

		_validateble = ruleSet;
	}

	private TextInput _streetInput;
	private TextInput _cityInput;

	private IValidatable _validateble;
}