package hojserver.tehdaskoneet;

public class SiloConveyer extends Conveyer {

	private Silo[] siilot;

	public SiloConveyer(Silo[] s){
		super();
		siilot = s;
	}
	
	public void run(){
		while(true){
			for (int i = 0; i < 4; i++){
				if (siilot[i].getDegreeOfFilling() <= siilot[i].getCapacity() 
						&& (siilot[i].getTila() == KoneenTila.FREE || siilot[i].getTila() == KoneenTila.FILLING) && siilot[i].isReserved()
						&& !running ){
					//TODO KORJAA!!!
					
					siilot[i].setTila(KoneenTila.FILLING);
					siilot[i].addToSilo(20);
					System.out.println(siilot[i].getDegreeOfFilling() + " " + i);
					
				}//if	
			}//for
			synchronized(this){
				try{
					this.wait(100);
				}catch (Exception e){System.out.println(e);}
			}
			
		}//while
	}//run
	
}

