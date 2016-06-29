package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProductGroup {
	public ProductGroup(int id, int productFamilyID, String name) {
		this.id = id;
		this.productFamilyID = productFamilyID;
		this.name = name;
	}

	private int id;
	private int productFamilyID;
	private String name;
	
	public static List<ProductGroup> loadAll()
	{
		List<ProductGroup> list = new LinkedList<ProductGroup>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.ProductGroupID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductGroup pc = new ProductGroup(rs.getInt("ProductGroupID"), rs.getInt("ProductFamilyID"), rs.getString("NAME"));
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

	public int getProductFamilyID() {
		return productFamilyID;
	}

	public void setProductFamilyID(int productFamilyID) {
		this.productFamilyID = productFamilyID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
