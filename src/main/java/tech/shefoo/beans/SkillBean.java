package tech.shefoo.beans;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;


import tech.shefoo.Skill;
import tech.shefoo.dao.SkillDAO;

@ManagedBean
@ViewScoped
public class SkillBean {

    private String newSkillName; // Name of the new skill to be created
    private String selectedSkill; // Currently selected skill from dropdowns
    private String memberName; // Name of the member to whom the skill will be assigned
    private List<Skill> skills; // List of available skills

    SkillDAO skilldao = new SkillDAO();

    
    @PostConstruct
    public void init() {
    	System.out.println("test");
    	System.out.println("test");
    	System.out.println("test");

    }
//    @PostConstruct
//    void init() {
//        // Initialize the skills list from the database
//        try {
//        	System.out.println("skills loaded !");
//            skills = skilldao.getAllSkills();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // You may want to handle this more gracefully in a production application
//        }
//    }

    // Getters and Setters
    public String getNewSkillName() {
        return newSkillName;
    }

    public void setNewSkillName(String newSkillName) {
        this.newSkillName = newSkillName;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getSelectedSkill() {
        return selectedSkill;
    }

    public void setSelectedSkill(String selectedSkill) {
        this.selectedSkill = selectedSkill;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    // Method to create a new skill
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

    // Method to add selected skill to an activity (implementation depends on your logic)
    public void addSkillToActivity() {
        if (selectedSkill != null) {
            // Logic to add selectedSkill to the activity (not fully defined in your code)
            // Example: activityDao.addSkillToActivity(activityName, selectedSkill);
            System.out.println("Skill added to activity: " + selectedSkill);
        }
    }

    // Method to assign the selected skill to a member
    public void assignSkillToMember() {
        if (selectedSkill != null && memberName != null && !memberName.isEmpty()) {
            // Logic to assign selectedSkill to the member (not fully defined in your code)
            // Example: memberDao.assignSkillToMember(memberName, selectedSkill);
            System.out.println("Assigned skill: " + selectedSkill + " to member: " + memberName);
        }
    }
}
