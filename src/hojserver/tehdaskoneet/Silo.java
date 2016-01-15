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
		//Oletusarvoisesti siilo on tyhjä
		täyttöaste = 0;
	}
	
	//---- GETTERS AND SETTERS ----
	
	public int getKapasiteetti(){
		return kapasiteetti;
	}
	
	public int getTäyttöaste(){
		return täyttöaste;
	}
	
	/**
	 * 
	 * @param täyttö
	 */
	public void resetTäyttöaste(int täyttö){ 
		if(täyttö <= kapasiteetti){
			täyttöaste = täyttö;
		} else{
			täyttöaste = kapasiteetti;
		}
	}
	
	/**
	 * 
	 * @param lisäys
	 */
	public void lisääSiiloon(int lisäys){
		if(täyttöaste + lisäys <= kapasiteetti){
			täyttöaste += lisäys;
		}
		else{
			//TODO
		}
	}
	
	
}
