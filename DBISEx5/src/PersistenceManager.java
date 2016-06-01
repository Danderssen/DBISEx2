import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;

public class PersistenceManager 
{
	static 	PersistenceManager 				instance;
			int								lastID = 0;
			Hashtable<Integer, DataTuple> 	buffer;
			HashSet<Integer>				committedTransactions;
	
	private PersistenceManager()
	{
		buffer = new Hashtable<Integer, DataTuple>();
		committedTransactions = new HashSet<Integer>();
	}
	public static synchronized PersistenceManager getInstance()
	{
		if (instance == null)
			instance = new PersistenceManager();
		return instance;
	}
	
	public synchronized int beginTransaction()
	{
		int id = ++lastID;
		return id;
	}
	
	public synchronized void commit(int taid)
	{
		for(Integer pageID : buffer.keySet())
		{
			DataTuple t = buffer.get(pageID);
			if (t.taID == taid)
			{
				Log.writeEntry(pageID, t);
			}
		}
		committedTransactions.add(taid);
	}
	
	public synchronized void write (int taid, int pageid, String data)
	{
		buffer.put(pageid, new DataTuple(taid, data));
		checkBuffer();
	}
	
	void checkBuffer()
	{
		System.out.println("Buffer size: " + buffer.size());
		if (buffer.size() > 5)
		{
			for(Integer pageID : buffer.keySet())
			{
				DataTuple t = buffer.get(pageID);
				if(committedTransactions.contains(t.taID))
				{
					try
					{
						FileWriter f = new FileWriter("Pages/" + pageID + ".txt");
						f.write(pageID + "," + t.lsn + "," + t.userData);
						f.close();
					} catch (IOException e) {
						System.out.println(e.getLocalizedMessage());
					}
				}
			}
		}
	}
	
	void restore()
	{
		 File f = null;
	     File[] paths;
	      
	      try{      
	         // create new file
	         f = new File("Log/");
	         
	         // returns pathnames for files and directory
	         paths = f.listFiles();
	         
	         // for each pathname in pathname array
	         for(File path:paths)
	         {
	        	 if(path.getName().startsWith("."))
	        		 continue;
	            // prints file and directory paths
	            FileReader reader = new FileReader(path);
	            BufferedReader br = new BufferedReader(reader);
	            String content = br.readLine();	    
	            
	            String[] splitArray = content.split(",");
	            
	            int lsn = Integer.parseInt(splitArray[0]);
	            int taid = Integer.parseInt(splitArray[1]);
	            int pageid = Integer.parseInt(splitArray[2]);
	            String data = splitArray[3];
	            
		        File page = new File("Pages/" + pageid + ".txt");
		        reader = new FileReader(page);
		        br = new BufferedReader(reader);
		        String page_content = br.readLine();
		        
		        if(page_content == null)
		        	continue;
	            String[] splitArrayPage = page_content.split(",");
	            int page_lsn = Integer.parseInt(splitArrayPage[1]);
	            
	            
	            if (page_lsn < lsn)
	            {
	            	System.out.println("Restoring Page " + pageid);
	            	System.out.println("LSN page " + page_lsn);
	            	System.out.println("LSN log " + lsn);
	            	System.out.println("Restored Data: " + data);
	            	System.out.println();

	            	try
					{
						FileWriter writer = new FileWriter("Pages/" + pageid + ".txt");
						writer.write(pageid + "," + lsn + "," + data);
						writer.close();
					} catch (IOException e) {
						System.out.println(e.getLocalizedMessage());
					}
	            	
	            }

	            
	            
	            
	            
	            
	         }
	      }catch(Exception e){
	         // if any error occurs
	         e.printStackTrace();
	      }
	}
	
}
