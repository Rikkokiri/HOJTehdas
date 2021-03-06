package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

/*
 * Yläluokka pumpuille
 */

public class Pump extends Thread {

	protected boolean running; //Kertoo, onko pumppu päällä vai ei
	
	
	// ---------- KONSTRUKTORI ---------- //
	
	public Pump(){
		running = false;
	}
	
	
	// ---------- GETTRIT / SETTERIT ---------- //
	
	/**
	 * Method that starts running the pump.
	 */
	public void runPump(){
		running = true;
	}
	
	public void stopPump(){
		running = false;
	}
	
	/**
	 * Methods tells is the pump currently running.
	 * @return
	 */
	public boolean isRunning(){
		return running;
	}

}
