import java.util.LinkedList;
import java.util.List;

import data.Article;
import data.CSVLoader;
import data.CSVSale;
import data.City;
import data.Country;
import data.ProductCategory;
import data.ProductFamily;
import data.ProductGroup;
import data.Region;
import data.Shop;
import warehouseData.WarehouseArticle;
import warehouseData.WarehouseDate;
import warehouseData.WarehouseSale;
import warehouseData.WarehouseShop;

public class Main {
	
	/*
	 * TODO: - Tabellen erstellen in SQL
	 * 		 - ETL verknüpfen
	 * 		 - 1.2
	 */

	public static void main(String[] args) 
	{
		List<CSVSale> list = CSVLoader.readCSV("sales.csv");
		List<ProductCategory> productCategories = ProductCategory.loadAll();
		List<ProductFamily> productFamilies = ProductFamily.loadAll();
		List<ProductGroup> productGroups = ProductGroup.loadAll();
		List<Article> articles = Article.loadAll();
		
		List<Country> laender = Country.loadAll();
		List<Region> regionen = Region.loadAll();
		List<City> staedte = City.loadAll();
		List<Shop> shops = Shop.loadAll();
		
		
		List<WarehouseArticle> articleList = WarehouseArticle.convertArticles(articles, productGroups, productFamilies, productCategories);
		List<WarehouseShop> shopList = WarehouseShop.convertShops(shops, staedte, regionen, laender);
		
		for(WarehouseArticle a : articleList)
			a.store();
		WarehouseArticle.closeStatement();
		
		for(WarehouseShop s : shopList)
			s.store();
		
		WarehouseShop.closeStatement();
		
		List<WarehouseDate> dateList = new LinkedList<WarehouseDate>();
		for (CSVSale s : list)
			dateList.add(new WarehouseDate(s.getDate()));
		
		for (WarehouseDate d  :dateList)
			d.store();
		
		WarehouseDate.closeStatement();
		
		List<WarehouseSale> salesList = WarehouseSale.convertSales(list, dateList, articleList, shopList);
		for(WarehouseSale s : salesList)
			s.store();
		WarehouseSale.closeStatement();
		
		
	}

}
