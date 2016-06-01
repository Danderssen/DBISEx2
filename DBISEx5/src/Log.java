import java.io.FileWriter;
import java.io.IOException;

public class Log {
	static int	latestLSN = 0;
	static void writeEntry(int pageID, DataTuple tuple)
	{
		tuple.lsn = ++latestLSN;
		try
		{
			FileWriter f = new FileWriter("Log/" + String.format("%06d", latestLSN) + ".txt");
			f.write(latestLSN + "," + tuple.taID + "," + pageID + "," + tuple.userData);
			f.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	
}
