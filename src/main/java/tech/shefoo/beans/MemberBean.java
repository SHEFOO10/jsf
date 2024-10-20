package tech.shefoo.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import tech.shefoo.Member;
import tech.shefoo.dao.MemberDAO;
import tech.shefoo.validator.EmailValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@ManagedBean
@ViewScoped
public class MemberBean {

    private List<Member> members;
    private MemberDAO memberDAO = new MemberDAO();
    private Member newMember = new Member();
    private Member selectedMember;

    private String uploadPath;
    private UploadedFile file;

    private String generateRandomFilename(String originalFileName) {
        if (originalFileName == null)
            return null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + fileExtension; // Append the original file extension
    }

    @PostConstruct
    public void init() {
        // Get the FacesContext and then the ServletContext
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

        // Get the real path for the uploads directory
        uploadPath = servletContext.getRealPath("/resources/uploads");

        // Create the uploads directory if it doesn't exist
        try {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // Create the uploads directory here
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String uploadProfileImg(UploadedFile file) {
        String randomImgName = null;
        if (file != null) {
            try (InputStream input = file.getInputStream()) {
                // Save the uploaded file to the uploads directory
                randomImgName = generateRandomFilename(file.getFileName());
                if (randomImgName == null)
                    return "placeholder.jpg";
                Path filePath = Paths.get(uploadPath, randomImgName);
                Files.copy(input, filePath);
                System.out.println("File successfully uploaded: " + filePath);
                return randomImgName;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                setFile(null);
            }
        } else {
            System.out.println("No file selected!");
        }
        return "placeholder.jpg";
    }

    public void loadMembers() {
        try {
            members = memberDAO.getAllMembers();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public List<Member> getMembers() {
        if (members == null) {
            loadMembers();
        }
        return members;
    }

    public Member getNewMember() {
        return newMember;
    }

    public void setNewMember(Member Member) {
        this.newMember = Member;
    }

    public void addMember() {
        validate();
        if (FacesContext.getCurrentInstance().getMessageList().isEmpty()) {

            String image_path = uploadProfileImg(file);

            newMember.setProfileImg(image_path);
            try {
                if (newMember.getId() == 0) {
                    memberDAO.addMember(newMember);
                } else {
                    memberDAO.updateMember(newMember);
                }
                newMember = new Member(); // Reset the form
                loadMembers(); // Refresh the list
            } catch (SQLException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Database Error", "An error occurred while accessing the database."));
                e.printStackTrace();
            }
        }
    }

    public void editMember(Integer id) {
        // Find the member by ID and populate `newMember` for editing
        for (Member member : members) {
            if (member.getId() == id) {
                try {
                    setNewMember((Member) member.clone());
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                setSelectedMember(member);
                break;
            }
        }
    }

    public void deleteMember(int memberId) {
        try {
            memberDAO.deleteMember(memberId);
            loadMembers(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void resetInputs() {
        newMember = new Member(); // Reset the form
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

    private Validator emailValidator = new EmailValidator();

    public Validator getEmailValidator() {
        return emailValidator;
    }

    public void setEmailValidator(Validator emailValidator) {
        this.emailValidator = emailValidator;
    }

    
    public void validate() {
        FacesContext context = FacesContext.getCurrentInstance();
    
        if (newMember.getName() == null || newMember.getName().isEmpty()) {
            context.addMessage("memberForm:name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Name is required."));
        }
    
        if (newMember.getNational_id() == null || newMember.getNational_id().isEmpty()) {
            context.addMessage("memberForm:national_id", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "National ID is required."));
        }
    
        if (newMember.getPhone() == null || newMember.getPhone().isEmpty()) {
            context.addMessage("memberForm:phone", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Phone is required."));
        }
    
        if (newMember.getEmail() == null || newMember.getEmail().isEmpty()) {
            context.addMessage("memberForm:email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Email is required."));
        }
    
        if (newMember.getDate_of_birth() == null || newMember.getDate_of_birth().isEmpty()) {
            context.addMessage("memberForm:dob", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Date of Birth is required."));
        }
    
        if (newMember.getAddress() == null || newMember.getAddress().isEmpty()) {
            context.addMessage("memberForm:address", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Address is required."));
        }
    }

}
