package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {

	private int id = -1;
	private String firstName;
	private String name;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/* Probably not required */
	public static Person load(int id) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM PERSON WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Person ts = new Person();
				ts.setId(id);
				ts.setFirstName(rs.getString("first_name"));
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));

				rs.close();
				pstmt.close();
				return ts;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* Probably not required */
	public static boolean delete(int id) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "DELETE FROM PERSON WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			return pstmt.execute();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			if (getId() == -1) {
				String insertSQL = "INSERT INTO PERSON (first_name, name, address) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			}
			/* Probably not required */
			else {
				String updateSQL = "UPDATE PERSON SET first_name = ?, name = ?, address = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setInt(4, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
