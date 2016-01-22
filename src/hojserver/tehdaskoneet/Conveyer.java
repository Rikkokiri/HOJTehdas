package hojserver.tehdaskoneet;

/*
- Siirtää raaka-ainetta 200 kiloa sekunnissa
- Kuljettimelle annetaan siirrettävä määrä parametrina
- Kuljetin ei voi siirtää enempää kuin vastaanottavaan yksikköön mahtuu
- Raaka-ainetta tulee lisää tehtaaseen yhdessä erässä 40 tonnia
- Siilo tai keitin ei saa ylitäyttyä
 */

/*
 * Ruuvikuljettimien yläluokka
 */

public class Conveyer extends Thread {

	protected boolean running;
	protected final int waitTime = 10;
	protected final int transferAmount = 2;
	
	
	 // -------- KONSTRUKTORI -------- //
	
	public Conveyer(){
	}
	
	
	// -------- GETTERIT / SETTERIT -------- //
	
	public void setRunning(boolean r){
		running = r;
	}

	public boolean isRunning(){
		return running;
	}

	public void setLimit(int l){
		
	}

}
