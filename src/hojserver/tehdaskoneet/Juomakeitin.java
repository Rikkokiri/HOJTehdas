package hojserver.tehdaskoneet;

/*
- kenelle varattu
- tilavuus 10000 litraa vettä ja 2000 kiloa raaka-ainetta
- prosessoi juomaa 20 sekuntia
 */

public class Juomakeitin {

	private final int vesitilavuus = 10000; //litraa
	private final int ainetilavuus = 2000; //kiloa
	private final int prosessointiaika = 20; //sekuntia
	
	private String kayttaja; //String vai jokin muu?
	
	
	public Juomakeitin(){
	
		kayttaja = null; // ???
		
	}
	
	/**
	 * 
	 * @param k
	 */
	public void setKayttaja(String k){
		kayttaja = k;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getKayttaja(){
		return kayttaja;
	}
	
	
	
}
