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
	
	public int beginTransaction()
	{
		int id = ++lastID;
		return id;
	}
	
	public void commit(int taid)
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
	
	public void write (int taid, int pageid, String data)
	{
		buffer.put(pageid, new DataTuple(taid, data));
		checkBuffer();
	}
	
	void checkBuffer()
	{
		if (buffer.size() > 5)
		{
			
		}
	}
	
}
