package warehouseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.City;
import data.Country;
import data.DB2ConnectionManager;
import data.Region;
import data.Shop;

public class WarehouseShop {
	private String shopName;
	private String cityName;
	private String regionName;
	private String countryName;
	private int	   shopID;
	private static PreparedStatement preparedStatement;
	private static int batchCount = 0;
	public WarehouseShop(String shopName, String cityName, String regionName, String countryName, int shopID) {
		this.shopName = shopName;
		this.cityName = cityName;
		this.regionName = regionName;
		this.countryName = countryName;
		this.shopID = shopID;
	}
	
	public static List<WarehouseShop> convertShops(List<Shop> shops, List<City> cities, List<Region> regions, List<Country> countries)
	{
		Map<Integer, City> cityMap = new HashMap<Integer, City>();
		for (City city : cities)
			cityMap.put(city.getId(), city);
		
		Map<Integer, Region> regionMap = new HashMap<Integer, Region>();
		for (Region region : regions)
			regionMap.put(region.getId(), region);
		
		Map<Integer, Country> countryMap = new HashMap<Integer, Country>();
		for (Country country : countries)
			countryMap.put(country.getId(), country);

		List<WarehouseShop> list = new LinkedList<WarehouseShop>();
		
		for (Shop s : shops)
		{
			City city = cityMap.get(s.getStadtid());
			String cityName = city.getName();
			
			Region region = regionMap.get(city.getRegionid());
			String regionName = region.getName();
			
			Country country = countryMap.get(region.getLandid());
			String countryName = country.getName();
			
			WarehouseShop newShop = new WarehouseShop(s.getName(), cityName, regionName, countryName, s.getId());
			list.add(newShop);
		}
		
		return list;
	}
	
	public void store()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			if(preparedStatement == null)
			{
				String selectSQL = "MERGE INTO SHOP d (ShopID, ShopName, CityName, RegionName, CountryName) "+
						 		    " USING (VALUES (?,?,?,?,?)) AS m (ShopID, ShopName, CityName, RegionName, CountryName) " +
						 		    " ON d.SHOPID = m.SHOPID " + 
						 		    " WHEN NOT MATCHED THEN " +
						 		    " INSERT (ShopID, ShopName, CityName, RegionName, CountryName) "+
						 		    " VALUES (m.ShopID, m.ShopName, m.CityName, m.RegionName, m.CountryName) " +
						 		    " ELSE IGNORE ";

			/*	String selectSQL = "insert into SALE_DATE (DATEID, DAY, MONTH, QUARTER, YEAR) " + 
						  "select ?, ?, ?, ?, ? " + 
						  "from SALE_DATE " +
						  "where not exists (select * from SALE_DATE where DATEID = ?) ";*/
				preparedStatement = con.prepareStatement(selectSQL);
			}
			
	/*		// Erzeuge Anfrage
			String selectSQL = "INSERT INTO SHOP (ShopID, ShopName, CityName, RegionName, CountryName) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);*/

			preparedStatement.setInt(1, shopID);
			preparedStatement.setString(2, shopName);
			preparedStatement.setString(3, cityName);
			preparedStatement.setString(4, regionName);
			preparedStatement.setString(5, countryName);

			preparedStatement.addBatch();

			if(batchCount > 100)
			{
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
		System.out.println("closing date statement");
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			preparedStatement.executeBatch();
			//con.commit();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

}
