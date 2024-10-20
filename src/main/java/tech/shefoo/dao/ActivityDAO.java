package tech.shefoo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

import tech.shefoo.Activity;
import tech.shefoo.DatabaseConfig;
import tech.shefoo.Member;
import tech.shefoo.Skill;

import java.time.LocalDate;
import java.time.Period;


public class ActivityDAO {

    private MemberDAO memberDAO = new MemberDAO();
    private HikariDataSource databaseInstance = DatabaseConfig.getDataSource();
    
    
    public static int calculateMemberAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        
        // Current date
        LocalDate currentDate = LocalDate.now();
        
        // Calculate the period between the birth date and the current date
        Period period = Period.between(birthDate, currentDate);
        
        // Return the number of years
        return period.getYears();
    }
    
    public int activitiesCount() {
    	String query = "CALL ActivitiesCount()";
    	
        try (Connection connection = databaseInstance.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
        			if (resultSet.next()) {
        				System.out.println(resultSet.getInt(1));
        				int count = resultSet.getInt(1);
        				return count;
        			}
        	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0;
    }
    
    
    public void joinActivity(int activity_id, int member_id) throws SQLException {
    	String query = "CALL AddMemberActivity(?, ?)";

    	
	     try (Connection connection = databaseInstance.getConnection();
	          PreparedStatement statement = connection.prepareStatement(query)) {
	    	 statement.setInt(1, member_id);
	         statement.setInt(2, activity_id);
	         statement.executeUpdate();
	     }
    }
    
    
    public void leaveActivity(int activity_id, int member_id) throws SQLException {
    	String query = "DELETE FROM member_activities WHERE activity_id = ? AND  member_id = ?";
	     try (Connection connection = databaseInstance.getConnection();
	          PreparedStatement statement = connection.prepareStatement(query)) {
	         statement.setInt(1, activity_id);
	         statement.setInt(2, member_id);
	         statement.executeUpdate();
	     }
    }
    
    
    public List<Activity> getAllActivites() throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "CALL GetAllActivities();";
        try (Connection connection = databaseInstance.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
            	Activity activity = new Activity();
            	activity.setId(resultSet.getInt("id"));
                activity.setName(resultSet.getString("name"));
                activity.setDescription(resultSet.getString("description"));
                activity.setMin_age(resultSet.getInt("min_age"));
                activity.setMax_age(resultSet.getInt("max_age"));                
                activity.setNewRecord(false);
                activity.setSkills(this.getActivitySkills(activity.getId()));
                activities.add(activity);
            }
        }
        return activities;
    }

    public List<Activity> getAvialableActivites(int memberId) throws SQLException {
        List<Activity> availableActivities = new ArrayList<>();
        Member member = memberDAO.getMemberById(memberId);
        if (member == null)
        	return availableActivities;


        
        LocalDate dateOfBirth = LocalDate.parse(member.getDate_of_birth());

        
        int age = calculateMemberAge(dateOfBirth);
        
        
        
        String query = "SELECT * FROM activities WHERE (min_age <= ? AND max_age >= ?) AND id NOT IN (select activity_id from member_activities where member_id = ?)";
        try (Connection connection = databaseInstance.getConnection();
    		PreparedStatement statement = connection.prepareStatement(query)
			) {
        	
        	statement.setInt(1, age);
        	statement.setInt(2, age);
        	statement.setInt(3, memberId);
        	ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	Activity activity = new Activity();
            	activity.setId(resultSet.getInt("id"));
                activity.setName(resultSet.getString("name"));
                activity.setDescription(resultSet.getString("description"));
                activity.setMin_age(resultSet.getInt("min_age"));
                activity.setMax_age(resultSet.getInt("max_age"));
                activity.setNewRecord(false);
                activity.setSkills(this.getActivitySkills(activity.getId()));
                availableActivities.add(activity);
            }
        }
        return availableActivities;
    }

    public List<Activity> getJoinedActivities(int memberId) throws SQLException {
    	List<Activity> joinedActivities = new ArrayList<>();
        Member member = memberDAO.getMemberById(memberId);
        if (member == null)
        	return joinedActivities;

        
        
        
        String query = "SELECT * FROM activities WHERE id in (select activity_id from member_activities where member_id = ?)";
        try (Connection connection = databaseInstance.getConnection();
    		 PreparedStatement statement = connection.prepareStatement(query);
        ) {
        	
        	statement.setInt(1, memberId);
        	ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	Activity activity = new Activity();
            	activity.setId(resultSet.getInt("id"));
                activity.setName(resultSet.getString("name"));
                activity.setDescription(resultSet.getString("description"));
                activity.setMin_age(resultSet.getInt("min_age"));
                activity.setMax_age(resultSet.getInt("max_age"));      
                activity.setNewRecord(false);
                activity.setSkills(this.getActivitySkills(activity.getId()));
                joinedActivities.add(activity);
            }
        }
        return joinedActivities;
    }
    
    
    
    public void addActivity(Activity activity) throws SQLException {
        String query = "CALL AddActivity(?, ?, ?, ?)";
        try (Connection connection = databaseInstance.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, activity.getName());
            statement.setString(2, activity.getDescription());
            statement.setInt(3, activity.getMin_age());
            statement.setInt(4, activity.getMax_age());
            statement.executeUpdate();
        }
    }

    
    public void updateActivity(Activity activity, Activity updatedActivity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }

        StringBuilder sql = new StringBuilder("UPDATE activities SET ");
        List<Object> parameters = new ArrayList<>();
        boolean hasChanges = false;

        // Compare and append fields to the SQL query
        if (!activity.getName().equals(updatedActivity.getName())) {
            sql.append("name = ?, ");
            parameters.add(updatedActivity.getName());
            hasChanges = true;
        }
        if (!activity.getDescription().equals(updatedActivity.getDescription())) {
            sql.append("description = ?, ");
            parameters.add(updatedActivity.getDescription());
            hasChanges = true;
        }
        if (!activity.getMin_age().equals(updatedActivity.getMin_age())) {
            sql.append("min_age = ?, ");
            parameters.add(updatedActivity.getMin_age());
            hasChanges = true;
        }
        if (!activity.getMax_age().equals(updatedActivity.getMax_age())) {
            sql.append("max_age = ?, ");
            parameters.add(updatedActivity.getMax_age());
            hasChanges = true;
        }

        // Ensure there are fields to update
        if (hasChanges) {
            // Remove the trailing comma and space
            sql.setLength(sql.length() - 2);

            // Append the WHERE clause
            sql.append(" WHERE id = ?");
            parameters.add(updatedActivity.getId()); // Assuming you have an ID to update

            // Execute the query using PreparedStatement
            try (Connection connection = databaseInstance.getConnection(); // Replace with your actual connection method
                 PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {

                // Set parameters
                for (int i = 0; i < parameters.size(); i++) {
                    pstmt.setObject(i + 1, parameters.get(i));
                }

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No changes detected, no update performed.");
        }
    }

    public Activity getActivityById(int activityId) throws SQLException {
        Activity activity = null;
        String query = "SELECT * FROM activities WHERE id = ?";
        try (Connection connection = databaseInstance.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    activity = new Activity();
                    activity.setId(resultSet.getInt("id"));
                    activity.setName(resultSet.getString("name"));
                    activity.setDescription(resultSet.getString("description"));
                    activity.setMin_age(resultSet.getInt("min_age"));
                    activity.setMax_age(resultSet.getInt("max_age"));
                  
                }
            }
        }
        return activity;
    }

    public void deleteActivity(int activityId) throws SQLException {
        String query = "CALL DeleteActivity(?);";
        try (Connection connection = databaseInstance.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, activityId);
            statement.executeUpdate();
        }
    }


	public List<Skill> getActivitySkills(int activityId) {
		String query = "CALL GetActivitySkills(?)";
		
		List<Skill> activitySkills = new ArrayList<>();
		try (Connection connection = databaseInstance.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, activityId);
				
				try (ResultSet result = statement.executeQuery()) {
					while (result.next()) {
		            	Skill skill = new Skill();
		            	skill.setId(result.getInt("id"));
		            	skill.setName(result.getString("name"));
		            	activitySkills.add(skill);
		            }
				}
				
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activitySkills;
		
	}
}
