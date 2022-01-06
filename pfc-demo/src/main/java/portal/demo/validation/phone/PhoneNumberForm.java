package portal.demo.validation.phone;

import portal.ui.comp.PanelDelegator;
import portal.ui.comp.TextInput;
import portal.validate.IValidatable;
import portal.validate.IValidationContext;
import portal.validate.rule.StringNotEmpty;
import portal.validate.rule.ValidationRuleSet;

public class PhoneNumberForm extends PanelDelegator<PhoneNumber> implements IValidatable
{
    private static final long serialVersionUID = 1L;

    public PhoneNumberForm()
    {
        this(new PhoneNumber());
    }

    public PhoneNumberForm(PhoneNumber pPhoneNumber)
    {
        super(pPhoneNumber);

        initUI();
        createValidation();
    }

    public PhoneNumber getPhoneNumber()
    {
        return getModel();
    }

    @Override
    public void validate(IValidationContext context)
    {
        _validateble.validate(context);
    }

    private void initUI()
    {
        _countryCodeInput = new TextInput(getPhoneNumber().getCountryCode());
        _countryCodeInput.setSize(3);
        _countryCodeInput.setMaximumLength(3);

        _areaCodeInput = new TextInput(getPhoneNumber().getAreaCode());
        _areaCodeInput.setSize(3);
        _areaCodeInput.setMaximumLength(3);

        _localNumberInput = new TextInput(getPhoneNumber().getLocalNumber());
        _localNumberInput.setSize(8);
        _localNumberInput.setMaximumLength(12);

        getContentPanel().add(_countryCodeInput);
        getContentPanel().add(_areaCodeInput);
        getContentPanel().add(_localNumberInput);
    }

    private void createValidation()
    {
        StringNotEmpty stringNotEmpty;

        stringNotEmpty = new StringNotEmpty(_countryCodeInput.getModel());
        _countryCodeInput.setValidationRule(stringNotEmpty);

        stringNotEmpty = new StringNotEmpty(_areaCodeInput.getModel());
        _areaCodeInput.setValidationRule(stringNotEmpty);

        stringNotEmpty = new StringNotEmpty(_localNumberInput.getModel());
        _localNumberInput.setValidationRule(stringNotEmpty);

        ValidationRuleSet ruleSet = new ValidationRuleSet();

        ruleSet.addValidationRule(_countryCodeInput);
        ruleSet.addValidationRule(_areaCodeInput);
        ruleSet.addValidationRule(_localNumberInput);

        _validateble = ruleSet;
    }
    private TextInput _countryCodeInput;
    private TextInput _areaCodeInput;
    private TextInput _localNumberInput;
    private IValidatable _validateble;
}
