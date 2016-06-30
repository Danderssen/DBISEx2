package warehouseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import data.CSVSale;
import data.DB2ConnectionManager;

public class WarehouseDate {
	
	private Date date;
	private int dateID;
	private int day;
	private int month;
	private int quarter;
	private int year;
	
	private static PreparedStatement preparedStatement;
	private static int batchCount = 0;
	
	public WarehouseDate(Date date) {
		this.date = date;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		this.month = cal.get(Calendar.MONTH) + 1; //ZERO BASED - pure Java logic!
		this.quarter = (this.month / 4) + 1;
		this.year = cal.get(Calendar.YEAR);
		
		this.dateID = year * 366 + month * 31 + day;
	}
	
	public static List<WarehouseDate> convertShops(List<CSVSale> sales)
	{
		List<WarehouseDate> list = new LinkedList<WarehouseDate>();
		
		for (CSVSale s : sales)
		{
			WarehouseDate newDate = new WarehouseDate(s.getDate());
			
			//Needed to ignore the multiple equal date "instances"
			if (list.contains(newDate)) {
				list.add(newDate);
			}
		}
		
		return list;
	}
	
	
	public void store()
	{
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			//String selectSQL = "INSERT INTO SALE_DATE (DATEID, DAY, MONTH, QUARTER, YEAR) VALUES (?, ?, ?, ?, ?) where not exists (select * from SALE_DATE where DATEID = ?)";
		//	String selectSQL = "IF NOT EXISTS (select 1 from SALE_DATE where DATEID = ?) BEGIN INSERT INTO SALE_DATE (DATEID, DAY, MONTH, QUARTER, YEAR) VALUES (?, ?, ?, ?, ?) END";
			/*String selectSQL = "insert into SALE_DATE (DATEID, DAY, MONTH, QUARTER, YEAR) " + 
								  "select ?, ?, ?, ?, ? " + 
								  "from SALE_DATE " +
								  "where not exists (select * from SALE_DATE where DATEID = ?) ";*/
			
			if(preparedStatement == null)
			{
				String selectSQL = "MERGE INTO SALE_DATE d (DATEID, DAY, MONTH, QUARTER, YEAR) "+
						 		    " USING (VALUES (?,?,?,?,?)) AS m (DATEID, DAY, MONTH, QUARTER, YEAR) " +
						 		    " ON d.DATEID = m.DATEID " + 
						 		    " WHEN NOT MATCHED THEN " +
						 		    " INSERT (DATEID, DAY, MONTH, QUARTER, YEAR) "+
						 		    " VALUES (m.DATEID, m.DAY, m.MONTH, m.QUARTER, m.YEAR) " +
						 		    " ELSE IGNORE ";

			/*	String selectSQL = "insert into SALE_DATE (DATEID, DAY, MONTH, QUARTER, YEAR) " + 
						  "select ?, ?, ?, ?, ? " + 
						  "from SALE_DATE " +
						  "where not exists (select * from SALE_DATE where DATEID = ?) ";*/
				preparedStatement = con.prepareStatement(selectSQL);
			}
			++batchCount;
			preparedStatement.setInt(1, dateID);
			preparedStatement.setInt(2, day);
			preparedStatement.setInt(3, month);
			preparedStatement.setInt(4, quarter);
			preparedStatement.setInt(5, year);
			// Führe Anfrage aus
			preparedStatement.addBatch();

			if(batchCount > 1000)
			{
				System.out.println("Executing batch");
				int[] result = preparedStatement.executeBatch();
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

	public Date getDate() {
		return date;
	}

	public int getDay() {
		return day;
	}
	
	public int getDateId() {
		return dateID;
	}

	public int getMonth() {
		return month;
	}

	public int getQuarter() {
		return quarter;
	}

	public int getYear() {
		return year;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	public void setDateId(int dateId) {
		this.dateID = dateId;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WarehouseDate other = (WarehouseDate) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		}
		else if (!date.equals(other.date))
			return false;
		return true;
	}
	
	
}
