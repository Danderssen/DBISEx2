package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Country {
	public Country(int id, String name) {
		this.id = id;
		this.name = name;
	}

	private int id;
	private String name;
	
	public static List<Country> loadAll()
	{
		List<Country> list = new LinkedList<Country>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.LandID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Country pc = new Country(rs.getInt("LANDID"), rs.getString("NAME"));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
