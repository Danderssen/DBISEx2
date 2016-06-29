import java.util.List;

import data.Article;
import data.CSVLoader;
import data.CSVSale;
import data.Country;
import data.ProductCategory;
import data.ProductFamily;
import data.ProductGroup;
import data.Region;
import data.Shop;
import data.City;

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
		System.out.println(list.size());
	}

}
