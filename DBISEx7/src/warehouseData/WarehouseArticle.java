package warehouseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.ProductGroup;
import data.ProductCategory;
import data.ProductFamily;
import data.Article;
import data.DB2ConnectionManager;

public class WarehouseArticle {
	
	private String articleName;
	private String productGroupName;
	private String productFamilyName;
	private String productCategoryName;
	private int    articleID;
	
	public WarehouseArticle(String articleName, String productGroupName, String productFamilyName,
			String productCategoryName, int articleID) {
		this.articleName = articleName;
		this.productGroupName = productGroupName;
		this.productFamilyName = productFamilyName;
		this.productCategoryName = productCategoryName;
		this.articleID = articleID;
	}

	public static List<WarehouseArticle> convertShops(List<Article> articles, List<ProductGroup> productGroups, List<ProductFamily> productFamilies, List<ProductCategory> productCategories)
	{
		Map<Integer, ProductGroup> productGroupMap = new HashMap<Integer, ProductGroup>();
		for (ProductGroup city : productGroups)
			productGroupMap.put(city.getId(), city);
		
		Map<Integer, ProductFamily> productFamilyMap = new HashMap<Integer, ProductFamily>();
		for (ProductFamily region : productFamilies)
			productFamilyMap.put(region.getId(), region);
		
		
		Map<Integer, ProductCategory> productCategoryMap = new HashMap<Integer, ProductCategory>();
		for (ProductCategory country : productCategories)
			productCategoryMap.put(country.getId(), country);

		
		List<WarehouseArticle> list = new LinkedList<WarehouseArticle>();
		
		for (Article s : articles)
		{
			ProductGroup productGroup = productGroupMap.get(s.getProductGroup());
			String productGroupName = productGroup.getName();
			
			ProductFamily productFamily = productFamilyMap.get(productGroup.getProductFamilyID());
			String productFamilyName = productFamily.getName();
			
			ProductCategory productCategory = productCategoryMap.get(productFamily.getProductCategoryID());
			String productCategoryName = productCategory.getName();
			
			WarehouseArticle newArticle = new WarehouseArticle(s.getName(), productGroupName, productFamilyName, productCategoryName, s.getId());
			list.add(newArticle);
		}
		
		return list;
	}
	
	
	public void store()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "INSERT INTO ARTICLE (ArticleID, ArticleName, ProductGroupName, ProductFamilyName, ProductCategoryName) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			pstmt.setInt(0, articleID);
			pstmt.setString(1, articleName);
			pstmt.setString(2, productGroupName);
			pstmt.setString(3, productFamilyName);
			pstmt.setString(4, productCategoryName);
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

	
}
