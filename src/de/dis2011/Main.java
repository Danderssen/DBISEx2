package de.dis2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import de.dis2011.data.Apartment;
import de.dis2011.data.Contract;
import de.dis2011.data.Estate;
import de.dis2011.data.House;
import de.dis2011.data.Makler;
import de.dis2011.data.Person;
import de.dis2011.data.PurchaseContract;
import de.dis2011.data.Rent;
import de.dis2011.data.Sell;
import de.dis2011.data.TenancyContract;

/**
 * Hauptklasse
 */
public class Main {

	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	static final String PASSWORD = "1234";

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		// Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		// Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Contract-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);

		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
			case MENU_MAKLER:
				showMaklerMenu();
				break;
			case MENU_ESTATE:
				showEstateMenu();
				break;
			case MENU_CONTRACT:
				showContractMenu();
				break;
			case QUIT:
				return;
			}
		}
	}

	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		// Menüoptionen
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter password ");
		String passwd = "";
		try {
			passwd = stdin.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if (!passwd.equals(PASSWORD))
		{
			System.out.println("Wrong password ");
			return;
		}

		final int NEW_MAKLER = 0;
		final int DELETE_MAKLER = 1;
		final int EDIT_MAKLER = 2;
		final int BACK = 3;

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Makler bearbeiten", EDIT_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
			case NEW_MAKLER:
				newMakler();
				break;
			case DELETE_MAKLER:
				deleteMakler();
				break;
			case EDIT_MAKLER:
				editMakler();
				break;
			case BACK:
				return;
			}
		}
	}

	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Makler mit der ID " + m.getId() + " wurde erzeugt.");
	}

	public static void deleteMakler() {
		int id = -1;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter id ");
		try {
			id = Integer.parseInt(stdin.readLine());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		boolean success = Makler.delete(id);
		if (success)
			System.out.println("Delete successful");
		else
			System.out.println("Delete not successful");
	}

	public static void editMakler() {
		int id = -1;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter id ");
		try {
			id = Integer.parseInt(stdin.readLine());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Makler m = Makler.load(id);
		if (m == null)
		{
			System.out.println("Estate agent not found.");
			return;
		}

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Makler mit der ID " + m.getId() + " wurde bearbeitet.");
	}

	public static void showEstateMenu() {
		// Menüoptionen
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter login ");
		String passwd = "";
		String login = "";
		try {
			login = stdin.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Enter password ");
		try {
			passwd = stdin.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Makler m = Makler.load(login);
		if (!passwd.equals(m.getPassword()))
		{
			System.out.println("Wrong password ");
			return;
		}
		int agentID = m.getId();

		final int NEW_ESTATE = 0;
		final int DELETE_ESTATE = 1;
		final int EDIT_ESTATE = 2;
		final int BACK = 3;

		// Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Estate Management");
		estateMenu.addEntry("New Estate", NEW_ESTATE);
		estateMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateMenu.addEntry("Edit Estate", EDIT_ESTATE);
		estateMenu.addEntry("Back to main menu", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = estateMenu.show();

			switch (response) {
			case NEW_ESTATE:
				newEstate(agentID);
				break;
			case DELETE_ESTATE:
				System.out.println("Not implemented yet!");
				// deleteEstate();
				break;
			case EDIT_ESTATE:
				System.out.println("Not implemented yet!");
				// editEstate();
				break;
			case BACK:
				return;
			}
		}
	}

	public static void newEstate(int agentID) {
		final int NEW_HOUSE = 0;
		final int NEW_APARTMENT = 1;
		final int BACK = 2;

		Menu estateMenu = new Menu("New Estate");
		estateMenu.addEntry("New House", NEW_HOUSE);
		estateMenu.addEntry("New Apartment", NEW_APARTMENT);
		estateMenu.addEntry("Back to main menu", BACK);

		while (true) {
			int response = estateMenu.show();

			switch (response) {
			case NEW_HOUSE:
				newHouse(agentID);
				// newEstate();
				// System.out.println("Estate with ID " + e.getId() + " was created.")
				break;
			case NEW_APARTMENT:
				newApartment(agentID);
				break;
			case BACK:
				return;
			}
		}
	}

	public static void newHouse(int agentID) {
		Estate e = Estate.EstateFromInput(agentID);
		House h = new House(e);
		h.setFloors(FormUtil.readInt("Floors"));
		h.setPrice(FormUtil.readInt("Price"));
		h.setGarden(FormUtil.readBool("Garden"));
		h.save();
	}

	public static void newApartment(int agentID) {
		Estate e = Estate.EstateFromInput(agentID);
		Apartment a = new Apartment(e);
		a.setFloor(FormUtil.readInt("Floor"));
		a.setRent(FormUtil.readInt("Rent"));
		a.setRooms(FormUtil.readFloat("Rooms"));
		a.setBalcony(FormUtil.readBool("Balcony"));
		a.setKitchen(FormUtil.readBool("Kitchen"));
		a.save();
	}

	public static void showContractMenu() {
		final int NEW_PERSON = 0;
		final int SIGN_CONTRACT = 1;
		final int OVERVIEW = 2;
		final int BACK = 3;

		Menu contractMenu = new Menu("Contract Management");
		contractMenu.addEntry("Insert Person", NEW_PERSON);
		contractMenu.addEntry("Sign Contract", SIGN_CONTRACT);
		contractMenu.addEntry("Overview of Contracts", OVERVIEW);
		contractMenu.addEntry("Back to main menu", BACK);

		while (true) {
			int response = contractMenu.show();

			switch (response) {
			case NEW_PERSON:
				newPerson();
				break;
			case SIGN_CONTRACT:
				signContract();
				break;
			case OVERVIEW:
				overviewContracts();
				break;
			case BACK:
				return;
			}
		}

		/*
		 * Random random = new Random();
		 * 
		 * Person p = new Person(); p.setAddress("a"); p.setFirstName("a"); p.setName("a"); p.save(); System.out.println("Person with ID " + p.getId() + " created!");
		 * 
		 * PurchaseContract pc = new PurchaseContract(); pc.setContractNumber(random.nextInt()); pc.setDate(new Date(1)); pc.setInstallments(12); pc.setInterestRate(4.5f); pc.setPlace("a"); pc.save();
		 * System.out.println("Purchase contract with contract number " + pc.getContractNumber() + " created!");
		 * 
		 * TenancyContract tc = new TenancyContract(); tc.setContractNumber(random.nextInt()); tc.setDate(new Date(1)); tc.setPlace("a"); tc.setAdditionalCosts(4.2f); tc.setDuration(5);
		 * tc.setStartDate(new Date(1)); tc.save(); System.out.println("Tenancy contract with contract number " + tc.getContractNumber() + " created!");
		 * 
		 * Sell s = new Sell(); s.setHouseId(1); s.setPersonId(p.getId()); s.setPurchaseContractId(pc.getContractNumber()); s.save(); System.out.println("Sell with ID " + s.getId() + " created!");
		 * 
		 * Rent r = new Rent(); r.setApartmentId(2); r.setPersonId(p.getId()); r.setTenancyContractId(tc.getContractNumber()); r.save(); System.out.println("Rent with ID " + r.getId() + " created!");
		 * 
		 * System.out.println("All rents:"); for (Rent rent : Rent.loadAll()) { System.out.println("Rent " + rent.getId()); }
		 * 
		 * System.out.println("All sells:"); for (Sell sell : Sell.loadAll()) { System.out.println("Sell " + sell.getId()); }
		 */
	}

	public static void newPerson() {
		Person p = new Person();
		p.setFirstName(FormUtil.readString("First name"));
		p.setName(FormUtil.readString("Name"));
		p.setAddress(FormUtil.readString("Address"));
		p.save();
	}

	public static void signContract(){
		final int NEW_PURCHASECONTRACT = 0;
		final int NEW_TENANCYCONTRACT = 1;
		final int BACK = 2;
		
		Menu signContractMenu = new Menu("Sign Contract");
		signContractMenu.addEntry("New Purchase Contract", NEW_PURCHASECONTRACT);
		signContractMenu.addEntry("New Tenancy Contract", NEW_TENANCYCONTRACT);
		signContractMenu.addEntry("Back to main menu", BACK);

		//Verarbeite Eingabe
		while (true) {
			int response = signContractMenu.show();

			switch (response) {
			case NEW_PURCHASECONTRACT:
				newPurchaseContract();
				System.out.println("New purchase contract was created.");
				break;
			case NEW_TENANCYCONTRACT:
				newTenancyContract();
				System.out.println("New tenancy contract was created.");
				break;
			case BACK:
				return;
			}
		}
	}

	public static void newPurchaseContract() {
		Contract c = Contract.ContractFromInput();
		PurchaseContract pc = new PurchaseContract(c);
		pc.setInstallments(FormUtil.readInt("Installments"));
		pc.setInterestRate(FormUtil.readFloat("Interest rate"));
		pc.save();
		
		Sell sell = new Sell();
		sell.setPurchaseContractId(c.getContractNumber());
		sell.setHouseId(FormUtil.readInt("House ID"));
		sell.setPersonId(FormUtil.readInt("Person ID"));
		sell.save();
	}

	public static void newTenancyContract() {
		Contract c = Contract.ContractFromInput();
		TenancyContract tc = new TenancyContract(c);
		tc.setStartDate(Date.valueOf(FormUtil.readString("Start Date")));
		tc.setDuration(FormUtil.readInt("Duration"));
		tc.setAdditionalCosts(FormUtil.readFloat("Additional Costs"));
		tc.save();
		
		Rent rent = new Rent();
		rent.setTenancyContractId(c.getContractNumber());
		rent.setApartmentId(FormUtil.readInt("Apartment ID"));
		rent.setPersonId(FormUtil.readInt("Person ID"));
		rent.save();
	}

	public static void overviewContracts() {
		ArrayList<Contract> contracts = Contract.loadAll();

		for (Contract c : contracts) {
			PurchaseContract pc = PurchaseContract.load(c.getContractNumber());
			TenancyContract tc = TenancyContract.load(c.getContractNumber());

			if (pc != null) {
				System.out.println(c.toString() + pc.toString());
			}

			if (tc != null) {
				System.out.println(c.toString() + tc.toString());
			}
		}
	}
}
