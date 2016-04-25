package de.dis2011.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TenancyContract extends Contract {

	private Date startDate;
	private int duration;
	private float additionalCosts;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public float getAdditionalCosts() {
		return additionalCosts;
	}

	public void setAdditionalCosts(float additionalCosts) {
		this.additionalCosts = additionalCosts;
	}
	
	public TenancyContract() {
	}

	public TenancyContract(Contract c) {
		this.setDate(c.getDate());
		this.setContractNumber(c.getContractNumber());
		this.setPlace(c.getPlace());
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			super.save();
			
			String insertSQL = "INSERT INTO TENANCY_CONTRACT(start_date, duration, additional_costs, contract_no) VALUES (?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(insertSQL);

			pstmt.setDate(1, getStartDate());
			pstmt.setInt(2, getDuration());
			pstmt.setFloat(3, getAdditionalCosts());
			pstmt.setInt(4, getContractNumber());
			pstmt.executeUpdate();
			pstmt.close();
			
			/*
			//else case for updating - probably not used!
			super.save();
			String updateSQL = "UPDATE TENANCY_CONTRACT SET start_date = ?, duration = ?, additional_costs = ? WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			pstmt.setDate(1, getStartDate());
			pstmt.setInt(2, getDuration());
			pstmt.setFloat(3, getAdditionalCosts());
			pstmt.setInt(4, getContractNumber());
			pstmt.executeUpdate();

			pstmt.close();
			*/
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean delete(int contractNumber) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "DELETE FROM TENANCY_CONTRACT WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNumber);
			boolean success = pstmt.execute();
			boolean contractSuccess = Contract.delete(contractNumber);
			return success && contractSuccess;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static TenancyContract load(int contractNumber) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM TENANCY_CONTRACT WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNumber);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TenancyContract ts = new TenancyContract(Contract.load(contractNumber));
				ts.setStartDate(rs.getDate("start_date"));
				ts.setDuration(rs.getInt("duration"));
				ts.setAdditionalCosts(rs.getFloat("additional_costs"));

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
	
	@Override
	public String toString(){
		return "Start date: " + startDate.toString() + "\n" + 
			   "Duration: " + Integer.toString(duration) + "\n" +
			   "Additional Costs: " + Float.toString(additionalCosts);
	}
}
