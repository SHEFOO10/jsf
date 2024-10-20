package tech.shefoo.validator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import tech.shefoo.dao.MemberDAO;


@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

   
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = value.toString();
        MemberDAO memberDAO = new MemberDAO();
        int memberId = (int) component.getAttributes().get("memberId");
        
        System.out.println(memberId);

        if (memberDAO.isEmailExist(email)) {
		context.addMessage("memberForm:email",
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Email already exists."));
		}
    }
}
