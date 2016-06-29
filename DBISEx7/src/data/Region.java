package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Region {
	public Region(int id, int landid, String name) {
		this.id = id;
		this.landid = landid;
		this.name = name;
	}

	private int id;
	private int landid;
	private String name;
	
	public static List<Region> loadAll()
	{
		List<Region> list = new LinkedList<Region>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.RegionID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Region pc = new Region(rs.getInt("RegionID"), rs.getInt("LandID"), rs.getString("NAME"));
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

	public int getLandid() {
		return landid;
	}

	public void setLandid(int landid) {
		this.landid = landid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
