package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class City {
	public City(int id, int regionid, String name) {
		this.id = id;
		this.regionid = regionid;
		this.name = name;
	}

	private int id;
	private int regionid;
	private String name;
	
	public static List<City> loadAll()
	{
		List<City> list = new LinkedList<City>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.StadtID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				City pc = new City(rs.getInt("StadtID"), rs.getInt("RegionID"), rs.getString("NAME"));
				list.add(pc);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(list.size());
		
		return list;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRegionid() {
		return regionid;
	}

	public void setRegionid(int regionid) {
		this.regionid = regionid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
