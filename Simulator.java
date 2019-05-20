import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Simulator {
	public static final int MAX_LINE_LENGTH=25,		// maximum length of a TollBooth Line
							SIMULATION_LENGTH=7200;	// number of seconds in the simulation
	public static final String AUTO="A", MANUAL="M";	// Vehicle Toll Type Designations (from input file)
	private static final int MANUAL_DELAY=4, AUTO_DELAY=2;	// observed booth delay (in seconds) per axle
	private static final int MAX_SIMULATION_VEHICLES=20000;	// for size of array to store done Vehicles

	private TollBoothLine [] manualLine;		// array of MANUAL Toll Booth Line
	private TollBoothLine [] automaticLine;		// array of AUTOMATIC Toll Booth Line
	private Scanner input;				// Scanner object for reading input file
	private int second;					// the simulation loop counter
	private Vehicle [] doneArray;		// array to store DONE Vehicles for later statistics calculation
	private int doneCount;				// number of Vehicles DONE
	
	
	public Simulator (String fileName, int manualTollCount, int autoTollCount) throws FileNotFoundException
	{	
		try{
			manualLine = new TollBoothLine[manualTollCount]; 
			automaticLine = new TollBoothLine[autoTollCount]; 
			doneArray = new Vehicle[MAX_SIMULATION_VEHICLES];

			second=1;
			doneCount=0;

			for (int i=0;i<manualTollCount ;i++ )
				manualLine[i]=new TollBoothLine(MAX_LINE_LENGTH);
			for (int i=0;i<autoTollCount ;i++ )
				automaticLine[i]=new TollBoothLine(MAX_LINE_LENGTH);

			File inputFile = new File( fileName );
			input = new Scanner(inputFile);
		}
		catch(Exception ex) { System.out.println(ex.getMessage());}
	}

	// Method to run the simulation
	public void start(){
		Vehicle v = readNewCar(input);
		
		while(second<=SIMULATION_LENGTH)
		{
			
			// add new Vehicles
			while (v!=null && (second == v.getArriveLineTime()))
			{
				
				TollBoothLine temp = findShortLine(v.getTollType());
				
				if (!temp.addVehicleEnd(v))
					System.out.println(v.getTollType() + " Lines Full");
					
				// if first Vehicle in line, set its booth and leave times
				if (temp.getLength() == 1)	
				{
					Vehicle vv = temp.copyVehicleStart();
					vv.setArriveBoothTime(second);
					if (vv.getTollType().equals(MANUAL))
						vv.setLeaveTime(second+vv.getAxles()*MANUAL_DELAY);
					else
						vv.setLeaveTime(second+vv.getAxles()*AUTO_DELAY);
					temp.replaceVehicleStart(vv);
				}
				v = readNewCar(input);
			}
			
			
			// remove Vehicles that are done from all MANUAL lines
			for (int i=0; i<manualLine.length; i++ )
				while (manualLine[i].getLength()>0 &&
					manualLine[i].copyVehicleStart().getLeaveTime() == second)
				{
					doneArray[doneCount]=manualLine[i].removeVehicleStart();
					doneArray[doneCount].setLeaveTime(second);
				
					// if still a Vehicle in line, set its booth and leave times
					if (manualLine[i].getLength() > 0)
					{
						Vehicle temp = manualLine[i].copyVehicleStart();
						temp.setArriveBoothTime(second);
						temp.setLeaveTime(second + temp.getAxles()*MANUAL_DELAY);
						manualLine[i].replaceVehicleStart(temp);
						
					}
				
					doneCount++;
				}
				
			
			// remove Vehicles that are done from all AUTOMATIC lines
			for (int i=0; i<automaticLine.length; i++ )
				while (automaticLine[i].getLength()>0 && 
					automaticLine[i].copyVehicleStart().getLeaveTime() == second)

				{
					doneArray[doneCount]=automaticLine[i].removeVehicleStart();
					doneArray[doneCount].setLeaveTime(second);

					// if still a Vehicle in line, set its booth and leave times
					if (automaticLine[i].getLength() > 0)
					{
						Vehicle temp = automaticLine[i].copyVehicleStart();
						temp.setArriveBoothTime(second);
						temp.setLeaveTime(second + temp.getAxles()*AUTO_DELAY);
						automaticLine[i].replaceVehicleStart(temp);
					}

					doneCount++;
					
				}
			second++;
	
		}
	}


	// Method to read, create and return a Vehicle, or return null
	public Vehicle readNewCar(Scanner text){
		
		String [] t;
		Vehicle newCar = new Vehicle(0,"",0);
		
		if(text.hasNext()){
			t = text.nextLine().split("\\s+");
			newCar.setArriveLineTime(Integer.parseInt(t[0]));
			newCar.setAxles(Integer.parseInt(t[1]));
			newCar.setTollType(t[2]);	
		}
		else
			newCar = null;
		
		return newCar;
	}

	// Method to find and return the shortest TollBoothLine of the given tollType
	public TollBoothLine findShortLine(String type){
		TollBoothLine s = new TollBoothLine(0);
		
		if(type.equals(MANUAL)){
			s = manualLine[0];
			for (int i = 1; i < manualLine.length; i++){
				if(manualLine[i].getLength() < s.getLength()) 
					s = manualLine[i];	
			}		
		}
		else if(type.equals(AUTO)){
			s = automaticLine[0];
			for (int i = 1; i < automaticLine.length; i++){
				if(automaticLine[i].getLength() < s.getLength()) 
					s = automaticLine[i];
			}	
		}
		else 
			s = null;
		
		return s;
	}
	
	// Method to calculate and output the Toll Line Statistics
		public void tollLineStatistics() {
			
			for(int i =0; i < manualLine.length;i++){
				System.out.println("Manual Line #" + (i+1)+ 
				" Maximum Length = " + manualLine[i].getMaxLength());
			}
			for(int i =0; i < automaticLine.length;i++){
				System.out.println("Automatic Line #" + (i+1)+ 
				" Maximum Length = " + automaticLine[i].getMaxLength() );
			}
		}

	// Method to calculate and output the DONE Vehicle Statistics
		public void doneStatistics() {
			
			int mMaxWait =0, aMaxWait =0;
			int mTemp=0, aTemp=0;
			int mWait =0, aWait =0;
			int mCount =0, aCount =0;
			
			for(int i =0; i<doneCount; i++){
				if(doneArray[i].getTollType().equals(MANUAL)){
					mMaxWait = doneArray[i].getArriveBoothTime() - 
					doneArray[i].getArriveLineTime();
					break;
				}
				if(doneArray[i].getTollType().equals(AUTO)){
					aMaxWait = doneArray[i].getArriveBoothTime() - 
					doneArray[i].getArriveLineTime();
					break;
				}
			}
		
			for(int i =0; i < doneCount;i++) {
				if(doneArray[i].getTollType().equals(MANUAL)){
					mTemp =  doneArray[i].getArriveBoothTime() - doneArray[i].getArriveLineTime();
					
					if(mTemp > mMaxWait) {
						mMaxWait = mTemp;
					}
					mWait += mTemp;
					mCount++;
				}
				if(doneArray[i].getTollType().equals(AUTO)){
					aTemp =  doneArray[i].getArriveBoothTime() - doneArray[i].getArriveLineTime();
					
					if(aTemp > aMaxWait){ 
						aMaxWait = aTemp;
					}
					aWait += aTemp;
					aCount++;
				}
				
			}

			System.out.println("Max Manual Wait = " + mMaxWait);
			System.out.println("Max Automatic Wait = " + aMaxWait);
			double mAverage = (double)mWait/(double)mCount;
			double aAverage = (double)aWait/ (double)aCount;
			System.out.println("Avg Manual Wait = " + mAverage);
			System.out.println("Avg Auto Wait = " + aAverage);
			
		}
		
}