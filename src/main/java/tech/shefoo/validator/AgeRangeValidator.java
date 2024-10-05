package tech.shefoo.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;


@FacesValidator("ageRangeValidator")
public class AgeRangeValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Integer minAge = (Integer) component.getAttributes().get("minAge");
        Integer maxAge = (Integer) value;

        
        System.out.println(minAge + " " + maxAge);
        if (minAge != null && maxAge != null && maxAge <= minAge) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", "Max age must be greater than Min age."));
        }
    }
}

