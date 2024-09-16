package tech.shefoo;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.sql.SQLException;
import java.util.List;



@ManagedBean
@ViewScoped
public class MemberBean {

	private List<Member> members;
    private MemberDAO memberDAO = new MemberDAO();
    private Member newMember = new Member();
    private Member selectedMember;

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
            try {
                if (newMember.getId() == 0) {
                    memberDAO.addMember(newMember);
                } else {
                    memberDAO.updateMember(selectedMember, newMember);
                }
                newMember = new Member(); // Reset the form
                loadMembers(); // Refresh the list
            } catch (SQLException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", "An error occurred while accessing the database."));
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
