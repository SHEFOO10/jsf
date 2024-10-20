package tech.shefoo.validator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.validator.FacesValidator;
import org.primefaces.model.file.UploadedFile;


@FacesValidator("imageFileValidator")
public class imageFileValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UploadedFile file = (UploadedFile) value;


        System.out.println("image validator is running");
        if (file == null || file.getSize() == 0) {
            context.addMessage("memberForm:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is required", null));
            return;
        }

        String fileName = file.getFileName();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            context.addMessage("memberForm:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid file type. Only JPG, JPEG, and PNG are allowed.", null));
            return;
        }

        if (file.getSize() > 1048576) { // 1MB
            context.addMessage("memberForm:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "File size too large. Maximum size allowed is 1MB.", null));
            return;
        }
    }
}