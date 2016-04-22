package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Rent {

	private int id = -1;
	private int apartmentId;
	private int personId;
	private int tenancyContractId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(int apartmentId) {
		this.apartmentId = apartmentId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public int getTenancyContractId() {
		return tenancyContractId;
	}

	public void setTenancyContractId(int tenancyContractId) {
		this.tenancyContractId = tenancyContractId;
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			if (getId() == -1) {
				String insertSQL = "INSERT INTO RENTS(apartment_id, person_id, contract_id) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				pstmt.setInt(1, getApartmentId());
				pstmt.setInt(2, getPersonId());
				pstmt.setInt(3, getTenancyContractId());
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			}
			else {
				/* Probably not required */
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Rent> loadAll() {
		List<Rent> resultList = new ArrayList<Rent>();
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM RENTS";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Rent ts = new Rent();
				ts.setId(rs.getInt("id"));
				ts.setApartmentId(rs.getInt("apartment_id"));
				ts.setPersonId(rs.getInt("person_id"));
				ts.setTenancyContractId(rs.getInt("contract_id"));
				
				resultList.add(ts);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
