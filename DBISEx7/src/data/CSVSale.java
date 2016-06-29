package data;
import java.util.Date;

public class CSVSale {
	
	private Date date;
	private String shop;
	private String article;
	private int sales;
	private float revenue;
	
	public CSVSale(Date date, String shop, String article, int sales, float revenue) {
		this.date = date;
		this.shop = shop;
		this.article = article;
		this.sales = sales;
		this.revenue = revenue;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	public float getRevenue() {
		return revenue;
	}
	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

}
