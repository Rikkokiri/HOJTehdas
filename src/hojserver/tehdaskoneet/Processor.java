package hojserver.tehdaskoneet;

/*
- kenelle varattu
- tilavuus 10000 litraa vettä ja 2000 kiloa raaka-ainetta
- prosessoi juomaa 20 sekuntia
 */

public class Processor {

	//Muuttujien nimet ovat jotenkin älyttömän typerän kuuluisia, mutta ne nyt tulivat mieleen.
	//Voi - ja varmaan kannattaa - vaihtaa myöhemmin.
	private final int vesitilavuus = 10000; //litraa
	private final int ainetilavuus = 2000; //kiloa
	private final int prosessointiaika = 20; //sekuntia
	
	private int vesimäärä;
	private int ainemäärä;
	
	private String käyttäjä; //String vai jokin muu?
	
	public Processor(){
		käyttäjä = null; // ???
		//Oletuksena keittimessä ei ole vettä eikä raaka-ainetta
		vesimäärä = 0;
		ainemäärä = 0;
		
	} //konstruktori

	// <<<< KÄYTTÄJÄ >>>>
	
	public void setKäyttäjä(String k){
		käyttäjä = k;
	}
	
	public String getKäyttäjä(){
		return käyttäjä;
	}
	
	
	// <<<< VESI JA RAAKA-AINE >>>>
	
	// --- Getterit ---
	public int getVesimäärä(){
		return vesimäärä;
	}
	
	public int getAinemäärä(){
		return ainemäärä;
	}
	
	/**
	 * Metodi, jolla tyhjennetään keitin, kun juoma on valmista.
	 */
	//JÄRKEVÄ????
	public void tyhjennäKeitin(){
		vesimäärä = 0;
		ainemäärä = 0;
	}
	
	
	
}
