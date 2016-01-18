package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class Pump extends Thread {

	private boolean running;
	private Processor origin;
	private Tank target;
	
	public Pump(){
		running = false;
		origin = null;
		target = null;
	}
	
	/**
	 * Method that starts running the pump.
	 */
	public void runPump(){
		running = true;
	}
	
	public void stopPump(){
		running = false;
		//origin = null;
		//target = null;
	}
	
	/**
	 * Methods tells is the pump currently running.
	 * @return
	 */
	public boolean isRunning(){
		return running;
	}

}
