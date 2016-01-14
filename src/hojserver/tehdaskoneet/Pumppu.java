package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class Pumppu {

	private boolean käynnissä;
	
	public Pumppu(){
		käynnissä = false;
	}
	
	/**
	 * Metodi, joka kertoo onko pumppu parhaillaan käytössä.
	 * @return
	 */
	public boolean onkoKäynnissä(){
		return käynnissä;
	}
	
	
	
}
