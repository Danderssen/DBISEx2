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

			// Erzeuge Anfrage
			String selectSQL = "INSERT INTO SHOP (ShopID, ShopName, CityName, RegionName, CountryName) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			pstmt.setInt(0, shopID);
			pstmt.setString(1, shopName);
			pstmt.setString(2, cityName);
			pstmt.setString(3, regionName);
			pstmt.setString(4, countryName);
			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
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
