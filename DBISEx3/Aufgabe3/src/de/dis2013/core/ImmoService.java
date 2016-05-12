package de.dis2013.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.dis2013.data.Haus;
import de.dis2013.data.Immobilie;
import de.dis2013.data.Kaufvertrag;
import de.dis2013.data.Makler;
import de.dis2013.data.Mietvertrag;
import de.dis2013.data.Person;
import de.dis2013.data.Wohnung;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 * 
 * TODO: Aktuell werden alle Daten im Speicher gehalten. Ziel der Übung
 * ist es, schrittweise die Datenverwaltung in die Datenbank auszulagern.
 * Wenn die Arbeit erledigt ist, werden alle Sets dieser Klasse überflüssig.
 */
public class ImmoService {
	//Datensätze im Speicher
	//private Set<Makler> makler = new HashSet<Makler>();
	//private Set<Person> personen = new HashSet<Person>();
	//private Set<Haus> haeuser = new HashSet<Haus>();
	//private Set<Wohnung> wohnungen = new HashSet<Wohnung>();
	//private Set<Mietvertrag> mietvertraege = new HashSet<Mietvertrag>();
	//private Set<Kaufvertrag> kaufvertraege = new HashSet<Kaufvertrag>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	private Session currentSession;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Finde einen Makler mit gegebener Id
	 * @param id Die ID des Maklers
	 * @return Makler mit der ID oder null
	 */
	public Makler getMaklerById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Makler m = (Makler)session.get(Makler.class, id);
		tx.commit();
		session.close();
		return m;
	}
	
	/**
	 * Finde einen Makler mit gegebenem Login
	 * @param login Der Login des Maklers
	 * @return Makler mit der ID oder null
	 */
	public Makler getMaklerByLogin(String login) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		//List<Makler> l = (List<Makler>) (List) session.createQuery("from Makler as makler where makler.login = ?").setString(0, login).list();
		Makler m = (Makler) session.createQuery("from Makler as makler where makler.login = ?").setString(0, login).uniqueResult();
		//Makler m = (Makler)session.get(Makler.class, id);
		tx.commit();
		session.close();
		return m;
	}
	
	/**
	 * Gibt alle Makler zurück
	 */
	public Set<Makler> getAllMakler() {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Makler> l = (List<Makler>) (List)session.createCriteria(Makler.class).list();
		tx.commit();
		session.close();
		Set<Makler> makler = new HashSet<Makler>(l);
		return makler;
	}
	/**
	 * Finde eine Person mit gegebener Id
	 * @param id Die ID der Person
	 * @return Person mit der ID oder null
	 */
	public Person getPersonById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Person p = (Person)session.get(Person.class, id);
		tx.commit();
		session.close();
		return p;
	}
	
	/**
	 * Fügt einen Makler hinzu
	 * @param m Der Makler
	 */
	public void addMakler(Makler m) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(m);
		session.getTransaction().commit();
		session.close();
	//	makler.add(m);
	}
	
	/**
	 * Löscht einen Makler
	 * @param m Der Makler
	 */
	public void deleteMakler(Makler m) {
		Session session = sessionFactory.openSession();
		session.delete(m);
		session.close();
	}
	
	/**
	 * Fügt eine Person hinzu
	 * @param p Die Person
	 */
	public void addPerson(Person p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(p);
		session.getTransaction().commit();
		session.close();
	//	personen.add(p);
	}
	
	/**
	 * Gibt alle Personen zurück
	 */
	public Set<Person> getAllPersons() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Person> l = (List<Person>) (List)session.createCriteria(Person.class).list();
		tx.commit();
		session.close();
		Set<Person> personen = new HashSet<Person>(l);
		return personen;
	}
	
	/**
	 * Löscht eine Person
	 * @param p Die Person
	 */
	public void deletePerson(Person p) {
		Session session = sessionFactory.openSession();
		session.delete(p);
		session.close();	
		}
	
	/**
	 * Fügt ein Haus hinzu
	 * @param h Das Haus
	 */
	public void addHaus(Haus h) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(h);
		session.getTransaction().commit();
		session.close();
	//	haeuser.add(h);
	}
	
	/**
	 * Gibt alle Häuser eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Häuser, die vom Makler verwaltet werden
	 */
	public Set<Haus> getAllHaeuserForMakler(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Haus> l = (List<Haus>) session.createQuery("from Haus as haus where haus.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
		session.close();
		Set<Haus> haeuser = new HashSet<Haus>(l);
		return haeuser;
	}
		
	
	/**
	 * Findet ein Haus mit gegebener ID
	 * @param m Der Makler
	 * @return Das Haus oder null, falls nicht gefunden
	 */
	public Haus getHausById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Haus h = (Haus)session.get(Haus.class, id);
		tx.commit();
		session.close();
		return h;
	}
	
	/**
	 * Löscht ein Haus
	 * @param p Das Haus
	 */
	public void deleteHouse(Haus h) {
		Session session = sessionFactory.openSession();
		session.delete(h);
		session.close();
	}
	
	/**
	 * Fügt eine Wohnung hinzu
	 * @param w die Wohnung
	 */
	public void addWohnung(Wohnung w) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(w);
		session.getTransaction().commit();
		session.close();
	//	wohnungen.add(w);
	}
	
	/**
	 * Gibt alle Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Wohnungen, die vom Makler verwaltet werden
	 */
	public Set<Wohnung> getAllWohnungenForMakler(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Wohnung> l = (List<Wohnung>) session.createQuery("from Wohnung as wohnung where wohnung.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
		session.close();
		Set<Wohnung> wohnungen = new HashSet<Wohnung>(l);
		return wohnungen;
	}
	
	/**
	 * Findet eine Wohnung mit gegebener ID
	 * @param id Die ID
	 * @return Die Wohnung oder null, falls nicht gefunden
	 */
	public Wohnung getWohnungById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Wohnung w = (Wohnung)session.get(Wohnung.class, id);
		tx.commit();
		session.close();
		return w;
	}
	
	/**
	 * Löscht eine Wohnung
	 * @param p Die Wohnung
	 */
	public void deleteWohnung(Wohnung w) {
		Session session = sessionFactory.openSession();
		session.delete(w);
		session.close();
	}
	
	
	/**
	 * Fügt einen Mietvertrag hinzu
	 * @param w Der Mietvertrag
	 */
	public void addMietvertrag(Mietvertrag m) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(m);
		session.getTransaction().commit();
		session.close();
	//	mietvertraege.add(m);
	}
	
	/**
	 * Fügt einen Kaufvertrag hinzu
	 * @param w Der Kaufvertrag
	 */
	public void addKaufvertrag(Kaufvertrag k) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(k);
		session.getTransaction().commit();
		session.close();
	//	kaufvertraege.add(k);
	}
	
	/**
	 * Gibt alle Mietverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Mietverträge, die zu Wohnungen gehören, die vom Makler verwaltet werden
	 */
	public Set<Mietvertrag> getAllMietvertraegeForMakler(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Object> l = session.createQuery("from Mietvertrag as mv, Wohnung as w WHERE mv.wohnung = w.id AND w.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
		//session.close();
		Set<Mietvertrag> mv = new HashSet<Mietvertrag>();
		for (int i = 0; i < l.size();++i)
		{
			Object[] list = (Object[]) l.get(i);
			mv.add((Mietvertrag)list[0]);
		}
		
		
		return mv;
	}
	
	/**
	 * Gibt alle Kaufverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Kaufverträge, die zu Häusern gehören, die vom Makler verwaltet werden
	 */
	public Set<Kaufvertrag> getAllKaufvertraegeForMakler(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Object> l = session.createQuery("from Kaufvertrag as kv, Wohnung as w WHERE kv.haus = w.id AND w.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
	//	session.close();
		Set<Kaufvertrag> kv = new HashSet<Kaufvertrag>();
		for (int i = 0; i < l.size();++i)
		{
			Object[] list = (Object[]) l.get(i);
			kv.add((Kaufvertrag)list[0]);
		}
		return kv;
	}
	
	/**
	 * Findet einen Mietvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Mietvertrag oder null, falls nicht gefunden
	 */
	public Mietvertrag getMietvertragById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Mietvertrag m = (Mietvertrag)session.get(Mietvertrag.class, id);
		tx.commit();
		session.close();
		return m;
	}
	
	/**
	 * Findet alle Mietverträge, die Wohnungen eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Mietverträgen
	 */
	public Set<Mietvertrag> getMietvertragByVerwalter(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Object> l =  session.createQuery("from Mietvertrag as mv, Wohnung as w WHERE mv.wohnung = w.id AND w.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
		//session.close();
		Set<Mietvertrag> mv = new HashSet<Mietvertrag>();
		for (int i = 0; i < l.size();++i)
		{
			Object[] list = (Object[]) l.get(i);
			mv.add((Mietvertrag)list[0]);
		}
		return mv;
	}
	
	/**
	 * Findet alle Kaufverträge, die Häuser eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Kaufverträgen
	 */
	public Set<Kaufvertrag> getKaufvertragByVerwalter(Makler m) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<Object> l =  session.createQuery("from Kaufvertrag as kv, Wohnung as w WHERE kv.wohnung = w.id AND w.verwalter = ?").setInteger(0, m.getId()).list();
		tx.commit();
	//	session.close();
		Set<Kaufvertrag> kv = new HashSet<Kaufvertrag>();
		for (int i = 0; i < l.size();++i)
		{
			Object[] list = (Object[]) l.get(i);
			kv.add((Kaufvertrag)list[0]);
		}
		return kv;
	}
	
	/**
	 * Findet einen Kaufvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Kaufvertrag oder null, falls nicht gefunden
	 */
	public Kaufvertrag getKaufvertragById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Kaufvertrag k = (Kaufvertrag)session.get(Kaufvertrag.class, id);
		tx.commit();
		session.close();
		return k;
	}
	
	public void updateMakler(Makler m)
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(m);
		tx.commit();
		session.close();
	}
	public void updatePerson(Person p)
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(p);
		tx.commit();
		session.close();
	}
	public void updateApartment(Wohnung w)
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(w);
		tx.commit();
		session.close();
	}
	
	/**
	 * Löscht einen Mietvertrag
	 * @param m Der Mietvertrag
	 */
	public void deleteMietvertrag(Mietvertrag m) {
		Session session = sessionFactory.openSession();
		session.delete(m);
		session.close();
	}
	
	/**
	 * Fügt einige Testdaten hinzu
	 */
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		Makler m = new Makler();
		m.setName("Max Mustermann");
		m.setAdresse("Am Informatikum 9");
		m.setLogin("max");
		m.setPasswort("max");
		
		//TODO: Dieser Makler wird im Speicher und der DB gehalten
		this.addMakler(m);

		
		Person p1 = new Person();
		p1.setAdresse("Informatikum");
		p1.setNachname("Mustermann");
		p1.setVorname("Erika");
		
		
		Person p2 = new Person();
		p2.setAdresse("Reeperbahn 9");
		p2.setNachname("Albers");
		p2.setVorname("Hans");
		
		//TODO: Diese Personen werden im Speicher und der DB gehalten
		this.addPerson(p1);
		this.addPerson(p2);

		Haus h = new Haus();
		h.setOrt("Hamburg");
		h.setPlz(22527);
		h.setStrasse("Vogt-Kölln-Straße");
		h.setHausnummer("2a");
		h.setFlaeche(384);
		h.setStockwerke(5);
		h.setKaufpreis(10000000);
		h.setGarten(true);
		h.setVerwalter(m);
		
		
		//TODO: Dieses Haus wird im Speicher und der DB gehalten
		this.addHaus(h);

		
		//Hibernate Session erzeugen
		session = sessionFactory.openSession();
		session.beginTransaction();
		Makler m2 = (Makler)session.get(Makler.class, m.getId());
		Set<Immobilie> immos = m2.getImmobilien();
		Iterator<Immobilie> it = immos.iterator();
		
		while(it.hasNext()) {
			Immobilie i = it.next();
			System.out.println("Immo: "+i.getOrt());
		}
		
		Wohnung w = new Wohnung();
		w.setOrt("Hamburg");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);

		
		w = new Wohnung();
		w.setOrt("Berlin");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);

		Kaufvertrag kv = new Kaufvertrag();
		kv.setHaus(h);
		kv.setVertragspartner(p1);
		kv.setVertragsnummer(9234);
		kv.setDatum(new Date(System.currentTimeMillis()));
		kv.setOrt("Hamburg");
		kv.setAnzahlRaten(5);
		kv.setRatenzins(4);
		this.addKaufvertrag(kv);

		Mietvertrag mv = new Mietvertrag();
		mv.setWohnung(w);
		mv.setVertragspartner(p2);
		mv.setVertragsnummer(23112);
		mv.setDatum(new Date(System.currentTimeMillis()-1000000000));
		mv.setOrt("Berlin");
		mv.setMietbeginn(new Date(System.currentTimeMillis()));
		mv.setNebenkosten(65);
		mv.setDauer(36);
		this.addMietvertrag(mv);
		
		session.close();
	}
}