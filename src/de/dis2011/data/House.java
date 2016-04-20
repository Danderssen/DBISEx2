package de.dis2011.data;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class House extends Estate {
	public House(Estate e) {
		this.setId(e.getId());
		this.setCity(e.getCity());
		this.setPostalCode(e.getPostalCode());
		this.setStreet(e.getStreet());
		this.setStreetNumber(e.getStreetNumber());
		this.setSquareArea(e.getSquareArea());
		this.setAgentId(e.getAgentId());
	}

	private int			floors;
	private int			price;
	private boolean		garden;
	public int getFloors() {
		return floors;
	}
	public void setFloors(int floors) {
		this.floors = floors;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isGarden() {
		return garden;
	}
	public void setGarden(boolean garden) {
		this.garden = garden;
	}
	
	public void save()
	{
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				super.save();
				String insertSQL = "INSERT INTO HOUSE(id, floors, price, garden) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setInt(2, getFloors());
				pstmt.setInt(3, getPrice());
				pstmt.setInt(4, isGarden() ? 1 : 0);
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				super.save();
				String updateSQL = "UPDATE HOUSE SET floors = ?, price = ?, garden = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getFloors());
				pstmt.setInt(2, getPrice());
				pstmt.setInt(3, isGarden() ? 1 : 0);
				pstmt.setInt(4, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean delete(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM HOUSE WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			boolean success = pstmt.execute();
			boolean estateSuccess = Estate.delete(id);
			return success && estateSuccess;

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static House load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM HOUSE WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				House ts = new House(Estate.load(id));
				ts.setFloors(rs.getInt("floors"));
				ts.setPrice(rs.getInt("price"));
				ts.setGarden(rs.getInt("garden") == 1 ? true : false);

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
