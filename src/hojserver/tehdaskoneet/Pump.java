package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class Pump {

	private boolean running;
	
	public Pump(){
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
