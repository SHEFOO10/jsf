package tech.shefoo.beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;


import tech.shefoo.Skill;

import tech.shefoo.dao.SkillDAO;

@ManagedBean
@ViewScoped
public class SkillBean {

    private List<Skill> skills;


	private int selectedMemberId;
    private int selectedActivityId;
    private List<String> selectedSkills = new ArrayList<>();
    private String newSkillName;
    public String getNewSkillName() {
		return newSkillName;
	}

	public void setNewSkillName(String newSkillName) {
		this.newSkillName = newSkillName;
	}

	SkillDAO skilldao = new SkillDAO();



    public List<Skill> getSkills() {
    	if (skills == null) {
    		try {
    			setSkills(skilldao.getAllSkills());
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}    		
    	}
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

//    // Method to create a new skill
   public String createSkill() {
       if (newSkillName != null && !newSkillName.isEmpty()) {
           Skill newSkill = new Skill(newSkillName);
           try {
               skilldao.addSkill(newSkill); // Assuming addSkill() adds the skill to the database
               skills = skilldao.getAllSkills(); // Refresh the skills list
               newSkillName = ""; // Reset the input field
           } catch (SQLException e) {
               e.printStackTrace();
               // Handle the error (e.g., show a message to the user)
           }
       }
       return null; // Stay on the same page
   }

//    // Method to add selected skill to an activity (implementation depends on your logic)
//    public void addSkillToActivity() {
//        if (selectedSkill != null) {
//            // Logic to add selectedSkill to the activity (not fully defined in your code)
//            // Example: activityDao.addSkillToActivity(activityName, selectedSkill);
//            System.out.println("Skill added to activity: " + selectedSkill);
//        }
//    }

//    // Method to assign the selected skill to a member
//    public void assignSkillToMember() {
//        if (selectedSkillForMember != null && memberName != null && !memberName.isEmpty()) {
//            // Logic to assign selectedSkill to the member (not fully defined in your code)
//            // Example: memberDao.assignSkillToMember(memberName, selectedSkill);
//            System.out.println("Assigned skill: " + selectedSkillForMember + " to member: " + memberName);
//            memberName = "";
//        }
//    }

 // Method to add selected skills to activity
    public void addSkillsToActivity() {
    	for (String skillId : selectedSkills) {
        	int skillintId = Integer.parseInt(skillId);
            skilldao.addSkillToActivity(selectedActivityId, skillintId);
        }
        selectedSkills.clear(); // Clear selection after adding
    }

    // Method to add selected skills to member
    public void addSkillsToMember() {
        for (String skillId : selectedSkills) {
        	 int skillintId = Integer.parseInt(skillId);
            skilldao.addSkillToMember(selectedMemberId, skillintId);
        }
        selectedSkills.clear(); // Clear selection after adding
    }

    public int getSelectedMemberId() {
		return selectedMemberId;
	}

	public void setSelectedMemberId(int selectedMemberId) {
		this.selectedMemberId = selectedMemberId;
	}

	public int getSelectedActivityId() {
		return selectedActivityId;
	}

	public void setSelectedActivityId(int selectedActivityId) {
		this.selectedActivityId = selectedActivityId;
	}

	
	public List<String> getSelectedSkills() {
		return selectedSkills;
	}

	public void setSelectedSkills(List<String> selectedSkills) {
		this.selectedSkills = selectedSkills;
	}
}
