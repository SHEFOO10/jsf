package tech.shefoo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setNational_id(resultSet.getString("national_id"));
                member.setPhone(resultSet.getString("phone"));
                member.setEmail(resultSet.getString("email"));
                member.setAddress(resultSet.getString("address"));
                member.setDate_of_birth(resultSet.getString("date_of_birth"));
                member.setNewRecord(false);
                members.add(member);
            }
        }
        return members;
    }

    public void addMember(Member member) throws SQLException {
        String query = "INSERT INTO members (name, national_id, phone, email, date_of_birth, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getNational_id());
            statement.setString(3, member.getPhone());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getDate_of_birth());
            statement.setString(6, member.getAddress());
            statement.executeUpdate();
        }
    }

    
    public void updateMember(Member member, Member updatedMember) {
        if (member == null) {
            throw new IllegalArgumentException("Member or Member ID cannot be null");
        }

        StringBuilder sql = new StringBuilder("UPDATE members SET ");
        List<Object> parameters = new ArrayList<>();
        boolean hasChanges = false;

        // Compare and append fields to the SQL query
        if (!member.getName().equals(updatedMember.getName())) {
            sql.append("name = ?, ");
            parameters.add(updatedMember.getName());
            hasChanges = true;
        }
        if (!member.getNational_id().equals(updatedMember.getNational_id())) {
            sql.append("national_id = ?, ");
            parameters.add(updatedMember.getNational_id());
            hasChanges = true;
        }
        if (!member.getPhone().equals(updatedMember.getPhone())) {
            sql.append("phone = ?, ");
            parameters.add(updatedMember.getPhone());
            hasChanges = true;
        }
        if (!member.getEmail().equals(updatedMember.getEmail())) {
            sql.append("email = ?, ");
            parameters.add(updatedMember.getEmail());
            hasChanges = true;
        }
        if (!member.getDate_of_birth().equals(updatedMember.getDate_of_birth())) {
            sql.append("date_of_birth = ?, ");
            parameters.add(updatedMember.getDate_of_birth());
            hasChanges = true;
        }
        if (!member.getAddress().equals(updatedMember.getAddress())) {
            sql.append("address = ?, ");
            parameters.add(updatedMember.getAddress());
            hasChanges = true;
        }

        // Ensure there are fields to update
        if (hasChanges) {
            // Remove the trailing comma and space
            sql.setLength(sql.length() - 2);

            // Append the WHERE clause
            sql.append(" WHERE id = ?");
            parameters.add(updatedMember.getId()); // Assuming you have an ID to update

            // Execute the query using PreparedStatement
            try (Connection connection = DatabaseConfig.getDataSource().getConnection(); // Replace with your actual connection method
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
                }
            }
        }
        return member;
    }

    public void deleteMember(int memberId) throws SQLException {
        String query = "DELETE FROM members WHERE id = ?";
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            statement.executeUpdate();
        }
    }
}
