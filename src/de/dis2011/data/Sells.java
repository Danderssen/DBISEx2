package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Sells {

	private int id;
	private int houseId;
	private int personId;
	private int purchaseContractId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public int getPurchaseContractId() {
		return purchaseContractId;
	}

	public void setPurchaseContractId(int purchaseContractId) {
		this.purchaseContractId = purchaseContractId;
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			if (getId() == -1) {
				String insertSQL = "INSERT INTO SELLS(house_id, person_id, contract_id) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				pstmt.setInt(1, getHouseId());
				pstmt.setInt(2, getPersonId());
				pstmt.setInt(3, getPurchaseContractId());
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
	
	public static List<Sells> loadAll() {
		List<Sells> resultList = new ArrayList<Sells>();
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM SELLS";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Sells ts = new Sells();
				ts.setId(rs.getInt("id"));
				ts.setHouseId(rs.getInt("sells_id"));
				ts.setPersonId(rs.getInt("person_id"));
				ts.setPurchaseContractId(rs.getInt("contract_id"));
				
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
