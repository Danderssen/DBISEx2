package de.dis2011.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Contract {

	private int contractNumber;
	private Date date;
	private String place;

	public int getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public static Contract load(int contractNumber) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM CONTRACT WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNumber);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Contract ts = new Contract();
				ts.setContractNumber(contractNumber);
				ts.setDate(rs.getDate("date"));
				ts.setPlace(rs.getString("place"));

				rs.close();
				pstmt.close();
				return ts;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Contract> loadAll() {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM CONTRACT";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			ArrayLit<Contract> contracts = new ArrayList<Contract>();
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Contract ts = new Contract();
				ts.setContractNumber(rs.getDate("contract_no"));
				ts.setDate(rs.getDate("date"));
				ts.setPlace(rs.getString("place"));
				
				contracts.add(ts);
			}
			
			rs.close();
			pstmt.close();
			return contracts;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean delete(int contractNumber) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM CONTRACT WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNumber);
			return pstmt.execute();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			String insertSQL = "INSERT INTO CONTRACT (date, place, contract_no) VALUES (?, ?, ?)";

			PreparedStatement pstmt = con.prepareStatement(insertSQL);

			pstmt.setDate(1, getDate());
			pstmt.setString(2, getPlace());
			pstmt.setInt(3, getContractNumber());
			pstmt.executeUpdate();

			pstmt.close();
			
			/*
			String updateSQL = "UPDATE CONTRACT SET date = ?, place = ? WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			pstmt.setDate(1, getDate());
			pstmt.setString(2, getPlace());
			pstmt.setInt(3, getContractNumber());
			pstmt.executeUpdate();

			pstmt.close();
			*/
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Contract ContractFromInput(){
		Contract c = new Contract();
		c.setContractNumber(FormUtil.readInt("ContractNumber"));
		c.setDate(Date.valueOf(FormUtil.readString("Date")));
		c.setPlace(FormUtil.readString("Place"));
		return c;
	}
	
	@Override
	public String toString(){
		return "ContractNumber: " + contractNumber + "\n" + 
			   "Date: " + date.toString() + "\n" + 
			   "Place: " + place + "\n";
	}
}
