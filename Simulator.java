import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

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
	private ArrayList<Vehicle> mVehiclesLeft;		// array to store vehicles that left for later statistics calculation
	private ArrayList<Vehicle> aVehiclesLeft;		// array to store vehicles that left for later statistics calculation


	public Simulator (String fileName, int manualTollCount, int autoTollCount) throws FileNotFoundException{
		try{
			manualLine = new TollBoothLine[manualTollCount];
			automaticLine = new TollBoothLine[autoTollCount];
			mVehiclesLeft= new ArrayList<Vehicle>();
			aVehiclesLeft= new ArrayList<Vehicle>();

			second=1;

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
		while(second<=SIMULATION_LENGTH){
			// add new Vehicles
			while (v!=null && (second == v.getArriveLineTime())){
				TollBoothLine tbl = findShortLine(v.getTollType());

				if (!tbl.addVehicleEnd(v))
					System.out.println(v.getTollType() + " Lines Full");
				// if first Vehicle in line, set its booth and leave times
				if (tbl.getLength() == 1){
					if (tbl.getTollType().equals(MANUAL))
						tbl.updateVehicleStart(second, MANUAL_DELAY);
					else
						tbl.updateVehicleStart(second, AUTO_DELAY);
				}
				v = readNewCar(input);
			}

			removeVehiclesLeft();
			second++;
		}
	}


	// Method to read, create and return a Vehicle, or return null
	public Vehicle readNewCar(Scanner text){
		if(text.hasNext()){
			String [] token = text.nextLine().split("\\s+");
			return new Vehicle(Integer.parseInt(token[1]), //axles
												token[2],										 //toll type
												Integer.parseInt(token[0])); //arrivalTime
		}
		return null;
	}

	// Method to find and return the shortest TollBoothLine of the given tollType
	public TollBoothLine findShortLine(String type){
		TollBoothLine shortest = null;
		if(type.equals(MANUAL)){
			shortest = manualLine[0];
			for (int i = 1; i < manualLine.length; i++){
				if(manualLine[i].getLength() < shortest.getLength())
					shortest = manualLine[i];
			}
		}
		else if(type.equals(AUTO)){
			shortest = automaticLine[0];
			for (int i = 1; i < automaticLine.length; i++){
				if(automaticLine[i].getLength() < shortest.getLength())
					shortest = automaticLine[i];
			}
		}

  	return shortest;
	}
	// remove Vehicles that are done from all lines
	public void removeVehiclesLeft() {

		for (int i=0; i<manualLine.length; i++ ){
			TollBoothLine tbl = manualLine[i];
			while (tbl.getLength()>0 && tbl.getFirst().getLeaveTime() == second){
				Vehicle vRemoved = tbl.removeVehicleStart();
				vRemoved.setLeaveTime(second);
				mVehiclesLeft.add(vRemoved);
				// if still a Vehicle in line, set its booth and leave times
				tbl.updateVehicleStart(second, MANUAL_DELAY);
			}
		}

		for (int i=0; i<automaticLine.length; i++ ) {
			TollBoothLine tbl = automaticLine[i];
			while (tbl.getLength()>0 && tbl.getFirst().getLeaveTime() == second){
				Vehicle vRemoved = tbl.removeVehicleStart();
				vRemoved.setLeaveTime(second);
				aVehiclesLeft.add(vRemoved);
				// if still a Vehicle in line, set its booth and leave times
			 tbl.updateVehicleStart(second, AUTO_DELAY);
			}
		}
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

		int mWait =0, aWait =0, waitTemp;
		int mMaxWait = mVehiclesLeft.get(0).getArriveBoothTime() - mVehiclesLeft.get(0).getArriveLineTime();
		int aMaxWait = aVehiclesLeft.get(0).getArriveBoothTime() - aVehiclesLeft.get(0).getArriveLineTime();

		for(Vehicle v : mVehiclesLeft) {
			waitTemp =  v.getArriveBoothTime() - v.getArriveLineTime();
			if(waitTemp > mMaxWait)
				mMaxWait = waitTemp;

			mWait += waitTemp;
		}

		for(Vehicle v : aVehiclesLeft) {
			waitTemp =  v.getArriveBoothTime() - v.getArriveLineTime();
			if(waitTemp > aMaxWait)
				aMaxWait = waitTemp;

			aWait += waitTemp;
		}

		double mAverage = (double)mWait/(double)mVehiclesLeft.size();
		double aAverage = (double)aWait/(double)aVehiclesLeft.size();

		System.out.println("Max Manual Wait = " + mMaxWait);
		System.out.println("Max Automatic Wait = " + aMaxWait);
		System.out.println("Avg Manual Wait = " + mAverage);
		System.out.println("Avg Auto Wait = " + aAverage);

	}

}
