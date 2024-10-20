package tech.shefoo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tech.shefoo.DatabaseConfig;
import tech.shefoo.Member;
import tech.shefoo.Skill;

public class MemberDAO {

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "CALL GetAllMembers();";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setProfileImg("/uploads/" + resultSet.getString("image_path"));
                member.setName(resultSet.getString("name"));
                member.setNational_id(resultSet.getString("national_id"));
                member.setPhone(resultSet.getString("phone"));
                member.setEmail(resultSet.getString("email"));
                member.setAddress(resultSet.getString("address"));
                member.setDate_of_birth(resultSet.getString("date_of_birth"));
                member.setNewRecord(false);
                member.setSkills(getMemberSkills(resultSet.getInt("id")));
                members.add(member);
            }
        }
        return members;
    }

    public void addMember(Member member) throws SQLException {
        String query = "CALL AddMember(?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getNational_id());
            statement.setString(3, member.getPhone());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getDate_of_birth());
            statement.setString(6, member.getAddress());
            statement.setString(7, member.getProfileImg());
            statement.executeUpdate();
        }
    }

    public int membersCount() {
    	String query = "select count(*) from members";
    	int count = 0;
    	try (Connection connection = DatabaseConfig.getDataSource().getConnection();
    			PreparedStatement statement = connection.prepareStatement(query);
			    ResultSet resultSet = statement.executeQuery()) {
    		if (resultSet.next()) {
                count = resultSet.getInt(1);  // Get the first column from the result set
            }
    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return count;
    }
    
    public void updateMember(Member updatedMember) {
        String query = "CALL UpdateMember(?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
        		System.out.println(updatedMember.getName());
        	   statement.setInt(1, updatedMember.getId());
               statement.setString(2, updatedMember.getName());
               statement.setString(3, updatedMember.getNational_id());
               statement.setString(4, updatedMember.getPhone());
               statement.setString(5, updatedMember.getEmail());
               statement.setString(6, updatedMember.getDate_of_birth());
               statement.setString(7, updatedMember.getAddress());
               statement.setString(8, updatedMember.getProfileImg());
               statement.executeUpdate();
       } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public Member getMemberById(int memberId) throws SQLException {
        Member member = null;
        String query = "SELECT * FROM members WHERE id = ?";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    member = new Member();
                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setNational_id(resultSet.getString("national_id"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setEmail(resultSet.getString("email"));
                    member.setAddress(resultSet.getString("address"));
                    member.setDate_of_birth(resultSet.getString("date_of_birth"));
                    member.setProfileImg("/uploads/" + resultSet.getString("image_path"));
                    member.setSkills(getMemberSkills(resultSet.getInt("id")));
                }
            }
        }
        return member;
    }

    public void deleteMember(int memberId) throws SQLException {
        String query = "CALL DeleteMember(?);";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            statement.executeUpdate();
        }
    }
    
    
    public List<Skill> getMemberSkills(int memberId) throws SQLException {
    	String query = "CALL GetMemberSkills("+ memberId +");";
    	List<Skill> skills = new ArrayList<>();
    	
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
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

	public boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM members WHERE email = ?";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
}
