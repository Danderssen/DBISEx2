package warehouseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.CSVSale;
import data.DB2ConnectionManager;

public class WarehouseSale {
	private int articleID;
	private int shopID;
	private int dateID;
	private int sales;
	private float revenue;
	
	public WarehouseSale(int articleID, int shopID, int dateID, int sales, float revenue) 
	{
		this.articleID = articleID;
		this.shopID = shopID;
		this.dateID = dateID;
		this.sales = sales;
		this.revenue = revenue;
	}

	public static List<WarehouseSale> convertSales(List<CSVSale> sales, List<WarehouseDate> dates, List<WarehouseArticle> articles, List<WarehouseShop> shops)
	{
		List<WarehouseSale> list = new LinkedList<WarehouseSale>();
		
		Map<String, WarehouseArticle> articleMap = new HashMap<>();
		for (WarehouseArticle a : articles)
			articleMap.put(a.getArticleName(), a);
		
		Map<String, WarehouseShop> shopMap = new HashMap<>();
		for (WarehouseShop a : shops)
			shopMap.put(a.getShopName(), a);
		
		Map<Date, WarehouseDate> dateMap = new HashMap<>();
		for (WarehouseDate a : dates)
			dateMap.put(a.getDate(), a);
		
		for (CSVSale s : sales)
		{
			int articleID = articleMap.get(s.getArticle()).getArticleID();
			int shopID = shopMap.get(s.getShop()).getShopID();
			int dateID = dateMap.get(s.getDate()).getDateId();
			
			WarehouseSale sale = new WarehouseSale(articleID, shopID, dateID, s.getSales(), s.getRevenue());
			list.add(sale);
		}		
		return list;
	}
	
	public void store()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "INSERT INTO SALE (DATEID, ShopID, ArticleID, Sales, Revenue) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			pstmt.setInt(0, dateID);
			pstmt.setInt(1, shopID);
			pstmt.setInt(2, articleID);
			pstmt.setInt(3, sales);
			pstmt.setFloat(4, revenue);
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getDateId() {
		return dateID;
	}

	public void setDateId(int dateId) {
		this.dateID = dateId;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public float getRevenue() {
		return revenue;
	}

	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}
}
