package hojserver.tehdaskoneet;

/*
- raaka-aineen tyyppi
- täyttöaste 0-10000 kiloa
- siiloa ei voi tyhjentää ja täyttää samaan aikaan
- Samasta siilosta voi ottaa raaka-ainetta vain yksi kuljetin kerrallaan

 */

public class Siilo {

	private final int kapasiteetti = 10000; //kiloa
	private int täyttöaste; //0-10000 kiloa
	
	
	public Siilo(){
		
	}
	
	//---- GETTERS AND SETTERS ----
	
	public int getTäyttöaste(){
		return täyttöaste;
	}
	
	public void setTäyttöaste(int täyttö){

	}
	
	
	
}
