
public class Main {

	public static void main(String[] args) 
	{
		
		PersistenceManager p = PersistenceManager.getInstance();
		p.restore();
		for(int i = 0; i < 5; ++i)
		{
			final int temp = i;
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					Client c = new Client(temp * 10, (temp + 1) * 10,"" + temp);
					c.run();
				}
			});
			t.start();
		}

	}

}
