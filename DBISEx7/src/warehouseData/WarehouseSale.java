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
	
	private static PreparedStatement preparedStatement;
	private static int batchCount = 0;
	
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
			//System.out.println("Date: " + dateID);
			
			WarehouseSale sale = new WarehouseSale(articleID, shopID, dateID, s.getSales(), s.getRevenue());
			list.add(sale);
		}		
		return list;
	}
	
	public void store()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			if(preparedStatement == null)
			{
				String selectSQL = "MERGE INTO SALE d (DATEID, ShopID, ArticleID, Sales, Revenue) "+
						 		    " USING (VALUES (?,?,?,?,?)) AS m (DATEID, ShopID, ArticleID, Sales, Revenue) " +
						 		    " ON (d.DATEID = m.DATEID) AND (d.ShopID = m.SHOPID) AND (d.ArticleID = m.ARTICLEID) " + 
						 		    " WHEN NOT MATCHED THEN " +
						 		    " INSERT (DATEID, ShopID, ArticleID, Sales, Revenue) "+
						 		    " VALUES (m.DATEID, m.ShopID, m.ArticleID, m.Sales, m.Revenue) " +
						 		    " ELSE IGNORE ";

				preparedStatement = con.prepareStatement(selectSQL);
			}
			++batchCount;
			preparedStatement.setInt(1, dateID);
			preparedStatement.setInt(2, shopID);
			preparedStatement.setInt(3, articleID);
			preparedStatement.setInt(4, sales);
			preparedStatement.setFloat(5, revenue);
			
			preparedStatement.addBatch();

			// Führe Anfrage aus
			if (batchCount > 1000) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
				batchCount = 0;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void closeStatement()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			preparedStatement.executeBatch();
			preparedStatement.close();
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
	
	public static List<WarehouseSale> loadAll()
	{
		List<WarehouseSale> list = new LinkedList<WarehouseSale>();
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM SALE";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				WarehouseSale pc = new WarehouseSale(rs.getInt("DATEID"), rs.getInt("ARTICLEID"), rs.getInt("SHOPID"), rs.getInt("SALES"), rs.getFloat("REVENUE"));
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
	
	public static void printTable(int dateHierarchy, int regionHierarchy, int articleHierarchy)
	{
		List<WarehouseSale> allSales = WarehouseSale.loadAll();
		
		for(int date = 0; date < allSales.size(); ++date)
		{
			if(date > 9)
				break;
			
			
		}
		
		
	}
}
