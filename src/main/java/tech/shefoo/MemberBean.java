package tech.shefoo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


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
        try {
        	if (newMember.getId() == 0) {
        		memberDAO.addMember(newMember);
        	} else {
        		memberDAO.updateMember(selectedMember, newMember);
        	}
        	newMember = new Member(); // Reset the form        		
            loadMembers(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
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
}
