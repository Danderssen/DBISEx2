package de.dis2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.dis2011.data.Makler;

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
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Contract-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					showMaklerMenu();
					break;
				case MENU_ESTATE:
					showEstateMenu();
					break;
				case MENU_CONTRACT:
					//showContractMenu();
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
		//Menüoptionen
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
		
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Makler bearbeiten", EDIT_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
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
	
	public static void showEstateMenu() {
		//Menüoptionen
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

		final int NEW_ESTATE = 0;
		final int DELETE_ESTATE = 1;
		final int EDIT_ESTATE = 2;
		final int BACK = 3;
		
		
		//Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Estate Management");
		estateMenu.addEntry("New Estate", NEW_ESTATE);
		estateMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateMenu.addEntry("Edit Estate", EDIT_ESTATE);
		estateMenu.addEntry("Back to main menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
			case NEW_ESTATE:
					newEstate();
					break;
			case DELETE_ESTATE:
					deleteEstate();
					break;
			case EDIT_ESTATE:
					editEstate();
					break;
			case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
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
		if(success)
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
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde bearbeitet.");
	}
}
