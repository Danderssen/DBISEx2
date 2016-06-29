package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Shop {
	public Shop(int id, int stadtid, String name) {
		this.id = id;
		this.stadtid = stadtid;
		this.name = name;
	}

	private int id;
	private int stadtid;
	private String name;
	
	public static List<Shop> loadAll()
	{
		List<Shop> list = new LinkedList<Shop>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.ShopID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Shop pc = new Shop(rs.getInt("ShopID"), rs.getInt("StadtID"), rs.getString("NAME"));
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

	public int getStadtid() {
		return stadtid;
	}

	public void setStadtid(int stadtid) {
		this.stadtid = stadtid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
