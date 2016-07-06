import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import data.DB2ConnectionManager;

public class DataAnalysis {
	
	private static final String[] ARTICLE_LEVEL_HIERARCHIE = {"ARTICLENAME", "PRODUCTGROUPNAME", "PRODUCTFAMILYNAME", "PRODUCTCATEGORYNAME"};
	private static final String[] SHOP_LEVEL_HIERARCHIE = {"SHOPNAME", "CITYNAME", "REGIONNAME", "COUNTRYNAME"};
	private static final String[] DATE_LEVEL_HIERARCHIE = {"D.DAY, D.MONTH, D.QUARTER, D.YEAR", "D.MONTH, D.QUARTER, D.YEAR", "D.QUARTER, D.YEAR", "D.YEAR"};
	

	public static void main(String[] args) {
		int currentArticleLevel = 0; // article, group, family, category
		int currentDateLevel = 0; // day, month, quarter, year
		int currentShopLevel = 0; // shop, city, region, country

		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				System.out.println("Neue Anfrage\n------------");
				System.out.println("Bitte geben Sie die gewünschte Hierarchieebene für den Artikel an (0 = Artikel, ..., 3 = Artikelkategorie): ");
				currentArticleLevel = Math.abs(Integer.parseInt(consoleReader.readLine()) % 4);
				System.out.println("Bitte geben Sie die gewünschte Hierarchieebene für das Datum an (0 = Tag, ..., 3 = Jahr): ");
				currentDateLevel = Math.abs(Integer.parseInt(consoleReader.readLine()) % 4);
				System.out.println("Bitte geben Sie die gewünschte Hierarchieebene für den Verkäufer an (0 = Laden, ..., 3 = Land): ");
				currentShopLevel = Math.abs(Integer.parseInt(consoleReader.readLine()) % 4);
			}
			catch (Exception e) {
				System.out.println("Konnte Eingabe nicht verarbeiten!");
				continue;
			}

			List<String> resultTable = requestWarehouse(currentArticleLevel, currentDateLevel, currentShopLevel);
			
			System.out.println("\nErgebnis\n" + resultTable.size() + " Zeilen\n--------");
			for (int i = 0; i < resultTable.size(); i++) { //i < Math.min(20, resultTable.size()); i++) {
				String s = resultTable.get(i);
				System.out.println(s);
			}
			System.out.println();
		}
	}

	private static List<String> requestWarehouse(int articleLevel, int dateLevel, int shopLevel) {
		List<String> result = null;
		
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();
			PreparedStatement pstmt = con.prepareStatement(createQuery(articleLevel, dateLevel, shopLevel));
			ResultSet rs = pstmt.executeQuery();
			
			result = createOutputForResultSet(articleLevel, dateLevel, shopLevel, rs);
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	private static List<String> createOutputForResultSet(int articleLevel, int dateLevel, int shopLevel, ResultSet rs) throws SQLException {
		List<String> list = new LinkedList<String>();
		
		String usedArticleLevel = ARTICLE_LEVEL_HIERARCHIE[articleLevel];
		String usedShopLevel = SHOP_LEVEL_HIERARCHIE[shopLevel];
		
		while (rs.next()) {
			String year = rs.getString("YEAR");//TBD
			String levelDependentShopName = rs.getString(usedShopLevel);
			String levelDependentArticleName = rs.getString(usedArticleLevel);
			int sales = rs.getInt("SALES");
			
			list.add(String.format("%4s %30s %40s %10d", year, levelDependentShopName, levelDependentArticleName, sales));
		}
		
		return list;
	}
	
	private static String createQuery(int articleLevel, int dateLevel, int shopLevel) {
		String usedArticleLevel = ARTICLE_LEVEL_HIERARCHIE[articleLevel];
		String usedShopLevel = SHOP_LEVEL_HIERARCHIE[shopLevel];
		String usedDateLevel = DATE_LEVEL_HIERARCHIE[dateLevel];
		
		StringBuilder b = new StringBuilder();
		
		b.append("SELECT SUM(S.SALES) AS SALES,");
		b.append("A." + usedArticleLevel + ",");
		b.append("SH." + usedShopLevel + ",");
		b.append(usedDateLevel + " ");
		b.append("FROM SHOP SH, ARTICLE A, SALE_DATE D, SALE S ");
		b.append("WHERE S.SHOPID = SH.SHOPID AND ");
		b.append("	  S.ARTICLEID = A.ARTICLEID AND ");
		b.append("	  S.DATEID = D.DATEID ");
		b.append("GROUP BY ROLLUP(");
		b.append(usedArticleLevel + ",");
		b.append(usedShopLevel + ",");
		b.append(usedDateLevel + ") ");
		b.append("ORDER BY ");
		b.append(usedShopLevel + ",");
		b.append(usedDateLevel + ", ");
		b.append(usedArticleLevel + "");
		
		
		
		//System.out.println(b.toString());
		
		return b.toString();
	}
	
}



/*
 * SELECT SUM(S.SALES) AS SALES, A.ARTICLENAME, SH.CITYNAME, D.QUARTER, D.YEAR
 * FROM SHOP SH, ARTICLE A, SALE_DATE D, SALE S 
 * WHERE S.SHOPID = SH.SHOPID AND
 * S.ARTICLEID = A.ARTICLEID AND 
 * S.DATEID = D.DATEID 
 * GROUP BY GROUPING SETS ((SH.CITYNAME, D.QUARTER), (SH.CITYNAME), (A.ARTICLENAME, D.QUARTER)) 
 * GROUP BY CUBE (SH.CITYNAME, D.QUARTER, A.ARTICLENAME)
 */