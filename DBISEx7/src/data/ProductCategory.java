package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProductCategory {
	public ProductCategory(int id, String name) {
		this.id = id;
		this.name = name;
	}

	private int id;
	private String name;
	
	public static List<ProductCategory> loadAll()
	{
		List<ProductCategory> list = new LinkedList<ProductCategory>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.ProductCategoryID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductCategory pc = new ProductCategory(rs.getInt("PRODUCTCATEGORYID"), rs.getString("NAME"));
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
