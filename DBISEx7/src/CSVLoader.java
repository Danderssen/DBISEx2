import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import data.CSVSale;

public class CSVLoader {
	
	public static List<CSVSale> readCSV(String path)
	{
		List<CSVSale> list = new LinkedList<CSVSale>();
		
		try
		{
			FileReader reader = new FileReader(path);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			SimpleDateFormat format = new SimpleDateFormat("dd.mm.yy");
			int count = 1;
			while((line = br.readLine()) != null)
			{
				++count;
				String[] split = line.split(";");
				if (split.length != 5)
					{
						System.out.println("Split length is not 5! Line: " + count );
						continue;
					}
				Date date;
				int sales;
				float revenue;
				try
				{
					 date = format.parse(split[0]);
				}
				catch(ParseException e)
				{
			         e.printStackTrace();
			         continue;
				}
				String shop = split[1];
				String article = split[2];
				try
				{
					 sales = Integer.parseInt(split[3]);
					 String rev = split[4].replace(",", ".");
					 revenue = Float.parseFloat(rev);
				}
				catch(NumberFormatException e)
				{
			         e.printStackTrace();
			         continue;
				}	
				CSVSale s = new CSVSale(date, shop, article, sales, revenue);
				list.add(s);
				
			}
		}
		catch (Exception e)
		{
	         e.printStackTrace();
		}
        		
		return list;
	}
}
