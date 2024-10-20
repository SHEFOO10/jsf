package tech.shefoo.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

@FacesValidator("activityValidator")
public class activityValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Integer minAge = (Integer) component.getAttributes().get("minAge");  // Min age passed as attribute
        Integer maxAge = null;

        // Check if value is an integer
        try {
            maxAge = Integer.parseInt(value.toString());  // Max age from input
        } catch (NumberFormatException e) {
            context.addMessage("activityForm:max_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", "Max age must be an integer."));
            return;
        }

        if (minAge != null && maxAge != null) {
            if (minAge < 0) {
                context.addMessage("activityForm:min_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", "Min age must be greater than 0."));
            }

            if (maxAge < 0) {
                context.addMessage("activityForm:max_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", "Max age must be greater than 0."));
            }

            if (maxAge < minAge) {
                context.addMessage("activityForm:max_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", "Max age must be greater than Min age."));
            }
        }
    }
}
