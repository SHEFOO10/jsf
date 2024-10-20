package tech.shefoo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

import tech.shefoo.DatabaseConfig;
import tech.shefoo.Skill;

public class SkillDAO {

    private HikariDataSource databaseInstance = DatabaseConfig.getDataSource();

	public List<Skill> getAllSkills() throws SQLException {
        List<Skill> skills = new ArrayList<>();
        String query = "CALL GetAllSkills();";
        try (Connection connection = databaseInstance.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
            	Skill skill = new Skill();
            	skill.setId(resultSet.getInt("id"));
            	skill.setName(resultSet.getString("name"));
                skills.add(skill);
            }
        }
        return skills;
    }

    public void addSkill(Skill skill) throws SQLException {
        String query = "CALL AddSkill(?);";
        try (Connection connection = databaseInstance.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, skill.getName());
            preparedStatement.executeUpdate();
        }
    }
    
    public void addSkillToActivity(int activiyId, int skillId) {
    	String query = "CALL AddActivitySkill(?, ?);";
    	
    	try (Connection connection = databaseInstance.getConnection();
    			PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    		preparedStatement.setInt(1, activiyId);
    		preparedStatement.setInt(2, skillId);
    		preparedStatement.executeUpdate();
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void addSkillToMember(int memberId, int skillId) {
    	
    	String query = "CALL AddMemberSkill(?, ?);";
    	
    	try (Connection connection = databaseInstance.getConnection();
    			PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    		preparedStatement.setInt(1, memberId);
    		preparedStatement.setInt(2, skillId);
    		preparedStatement.executeUpdate();
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
