
public class Main {

	public static void main(String[] args) 
	{

		PersistenceManager p = PersistenceManager.getInstance();
		
		int t1 = p.beginTransaction();
		int t2 = p.beginTransaction();
		
		p.write(t1, 1, "AAAAAAA");
		p.write(t1, 2, "BBBBBBB");
		p.commit(t1);
	}

}
