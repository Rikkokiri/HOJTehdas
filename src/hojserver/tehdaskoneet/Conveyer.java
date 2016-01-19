package hojserver.tehdaskoneet;

/*
- Siirtää raaka-ainetta 200 kiloa sekunnissa
- Kuljettimelle annetaan siirrettävä määrä parametrina
- Kuljetin ei voi siirtää enempää kuin vastaanottavaan yksikköön mahtuu
- Raaka-ainetta tulee lisää tehtaaseen yhdessä erässä 40 tonnia
- Siilo tai keitin ei saa ylitäyttyä
 */

public class Conveyer extends Thread {

	protected boolean running;
	protected int limit;
	
	public Conveyer(){
		limit = -1;
	} //konstruktori
	
	public void setRunning(boolean r){
		running = r;
	}
	
	public void setLimit(int l){
		limit = l;
	}
	
	/**
	 * Metodi, joka yksinkertaisesti kertoo, onko ruuvikuljetin parhaillaan käynnissä.
	 * @return
	 */
	public boolean isRunning(){
		return running;
	}

}
