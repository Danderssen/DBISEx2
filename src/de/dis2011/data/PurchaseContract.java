package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseContract extends Contract {
	private int installments;
	private float interestRate;
	
	public int getInstallments() {
		return installments;
	}

	public void setInstallments(int installments) {
		this.installments = installments;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public PurchaseContract() {
	}
	
	public PurchaseContract(Contract c) {
		this.setDate(c.getDate());
		this.setContractNumber(c.getContractNumber());
		this.setPlace(c.getPlace());
	}

	public void save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			super.save();

			String insertSQL = "INSERT INTO PURCHASE_CONTRACT (no_installments, interest_rate, contract_no) VALUES (?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(insertSQL);

			pstmt.setInt(1, getInstallments());
			pstmt.setFloat(2, getInterestRate());
			pstmt.setInt(3, getContractNumber());
			pstmt.executeUpdate();
			pstmt.close();
			
			/*
			//else case for updating probably not used!
			super.save();

			String updateSQL = "UPDATE PURCHASE_CONTRACT SET no_installments = ?, interest_rate = ? WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			pstmt.setInt(1, getInstallments());
			pstmt.setFloat(2, getInterestRate());
			pstmt.setInt(3, getContractNumber());
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

			String selectSQL = "DELETE FROM PURCHASE_CONTRACT WHERE contract_no = ?";
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
	
	public static ArrayList<PurchaseContract> loadAll() {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT CONTRACT_NO FROM PURCHASE_CONTRACT";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			ArrayList<PurchaseContract> contracts = new ArrayList<PurchaseContract>();
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				contracts.add(load(rs.getInt("contract_no")));
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

	public static PurchaseContract load(int contractNumber) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM PURCHASE_CONTRACT WHERE contract_no = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contractNumber);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				PurchaseContract ts = new PurchaseContract(Contract.load(contractNumber));
				ts.setInstallments(rs.getInt("no_installments"));
				ts.setInterestRate(rs.getFloat("interest_rate"));

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
		return super.toString() + "Installments: " + Integer.toString(installments) + "\n" + 
			   "Interest rate: " + Float.toString(interestRate);
	}
}
