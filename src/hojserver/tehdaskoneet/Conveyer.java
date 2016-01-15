package hojserver.tehdaskoneet;

/*
- Siirtää raaka-ainetta 200 kiloa sekunnissa
- Kuljettimelle annetaan siirrettävä määrä parametrina
- Kuljetin ei voi siirtää enempää kuin vastaanottavaan yksikköön mahtuu
- Raaka-ainetta tulee lisää tehtaaseen yhdessä erässä 40 tonnia
- Siilo tai keitin ei saa ylitäyttyä
 */

public class Conveyer {

	private boolean käynnissä;
	
	public Conveyer(){
		
	} //konstruktori
	
	/**
	 * Metodi, joka yksinkertaisesti kertoo, onko ruuvikuljetin parhaillaan käynnissä.
	 * @return
	 */
	public boolean onkoKäynnissä(){
		return käynnissä;
	}

}
