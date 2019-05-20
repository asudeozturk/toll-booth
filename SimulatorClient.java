
import java.util.Scanner;

public class SimulatorClient{
	public static void main(String[] args) {
		
		try{
			Scanner input = new Scanner(System.in);
			
			System.out.print("Input File: " );
			String file = input.next();
			System.out.print("Manual Toll Booths: ");
			int manual = input.nextInt();
			System.out.print("Automatic Toll Booths: ");
			int automatic = input.nextInt();
			
			while(manual <=0 || automatic <=0 || (automatic+manual)> 6 ){
				if(manual <=0){
					System.out.print("Error. Re-enter a positive integer for manual toll booth: ");
					manual = input.nextInt();
				}
				else if( automatic <= 0){
					System.out.print("Error. Re-enter a positive integer for automatic toll booth: ");
					automatic = input.nextInt();
				}
				else{
					System.out.println("Error. Total number of toll booths is limited to 6.");
					System.out.print("Re-enter Manual Toll Booths: ");
					manual = input.nextInt();
					System.out.print("Re-enter Automatic Toll Booths: ");
					automatic = input.nextInt();
				}	
			}
		
			Simulator s1 = new Simulator(file, manual, automatic);
	
			s1.start();
			s1.tollLineStatistics();
			s1.doneStatistics();
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		
		
	}
}