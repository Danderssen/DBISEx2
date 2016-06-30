package data;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Article {
	public Article(int id, int productGroup, String name, float price) {
		this.id = id;
		this.productGroup = productGroup;
		this.name = name;
		this.price = price;
	}

	private int id;
	private int productGroup;
	private String name;
	private float price;
	
	public static List<Article> loadAll()
	{
		List<Article> list = new LinkedList<Article>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM DB2INST1.ArticleID";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if(rs.getString("NAME").contains("Lavatherm"))
					System.out.println(rs.getString("NAME"));
				/*String name = new String();
				try
				{
					name = Arrays.toString(rs.getString("NAME").getBytes("ISO-8859-1"));
				}
				catch(UnsupportedEncodingException e)
				{
					System.out.println(e.getMessage());
				}*/
				Article pc = new Article(rs.getInt("ArticleID"), rs.getInt("ProductGroupID"), rs.getString("NAME"), rs.getFloat("PREIS"));
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

	public int getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(int productGroup) {
		this.productGroup = productGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
