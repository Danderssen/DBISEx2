import java.util.Random;

public class Client {
	private int minPageInclusive;
	private	int maxPageExclusive;
	private PersistenceManager persistenceManager;
	private Random randomGenerator;
	private String clientID;
	
	public Client(int minPageIncl, int maxPageExcl, String clientID)
	{
		minPageInclusive = minPageIncl;
		maxPageExclusive = maxPageExcl;
		persistenceManager = PersistenceManager.getInstance();
		randomGenerator = new Random(System.currentTimeMillis());
		this.clientID = clientID;
			}
	public void run()
	{
		
		int taid = persistenceManager.beginTransaction();
	    int numberOfWrites = randomGenerator.nextInt(10) + 5;		
	    
	    for(int i = 0; i < numberOfWrites; ++i)
	    {
		    int pageID = randomGenerator.nextInt((maxPageExclusive - minPageInclusive)) + minPageInclusive;		
		    persistenceManager.write(taid, pageID, clientID + " " + taid + " " + pageID + " " + i + " " + numberOfWrites);
		    try {
		    	Thread.sleep(300);
		    }
		    catch (InterruptedException e)
		    {
		    	System.out.println(e.getLocalizedMessage());
		    }
	    }
		persistenceManager.commit(taid);
		run();
	}
}
