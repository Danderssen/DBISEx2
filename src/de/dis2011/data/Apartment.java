package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment extends Estate {
	private int		floor;
	private int		rent;
	private float	rooms;
	private boolean	balcony;
	private boolean	kitchen;
	
	public Apartment(Estate e) {
		this.setId(e.getId());
		this.setCity(e.getCity());
		this.setPostalCode(e.getPostalCode());
		this.setStreet(e.getStreet());
		this.setStreetNumber(e.getStreetNumber());
		this.setSquareArea(e.getSquareArea());
	}

	
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getRent() {
		return rent;
	}
	public void setRent(int rent) {
		this.rent = rent;
	}
	public float getRooms() {
		return rooms;
	}
	public void setRooms(float rooms) {
		this.rooms = rooms;
	}
	public boolean isBalcony() {
		return balcony;
	}
	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	public boolean isKitchen() {
		return kitchen;
	}
	public void setKitchen(boolean kitchen) {
		this.kitchen = kitchen;
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
				String insertSQL = "INSERT INTO APARTMENT(id, floor, rent, rooms, balcony, kitchen) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setInt(2, getFloor());
				pstmt.setInt(3, getRent());
				pstmt.setFloat(4, getRooms());
				pstmt.setInt(5, isBalcony() ? 1 : 0);
				pstmt.setInt(6, isKitchen() ? 1 : 0);
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
				String updateSQL = "UPDATE APARTMENT SET floor = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getRent());
				pstmt.setFloat(3, getRooms());
				pstmt.setInt(4, isBalcony() ? 1 : 0);
				pstmt.setInt(5, isKitchen() ? 1 : 0);
				pstmt.setInt(6, getId());
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
			String selectSQL = "DELETE FROM APARTMENT WHERE id = ?";
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
	
	public static Apartment load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM APARTMENT WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment ts = new Apartment(Estate.load(id));
				ts.setFloor(rs.getInt("floor"));
				ts.setRent(rs.getInt("rent"));
				ts.setRooms(rs.getFloat("rooms"));
				ts.setBalcony(rs.getInt("balcony") == 1 ? true : false);
				ts.setKitchen(rs.getInt("kitchen") == 1 ? true : false);

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
