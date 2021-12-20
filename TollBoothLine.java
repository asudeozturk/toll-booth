import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;

public class TollBoothLine{

	private Queue<Vehicle> vehicles;
	private int limit;
	private int currentNum;
	private int maxLength;

	public TollBoothLine(int size){
		limit = size;
		vehicles = new LinkedList<>();
		currentNum=0;
	}

	public int getLength() { return currentNum; }
	public int getMaxLength() { return maxLength; }
	public Vehicle getFirst() { return vehicles.peek(); }
	public String getTollType() {
		return vehicles.peek().getTollType();
	}

	//If space available, add new vehicle at the end of the line
	public boolean addVehicleEnd(Vehicle v) {
		if(currentNum < limit){
			vehicles.add(v);
			currentNum++;
			if(currentNum > maxLength)
				maxLength++;
			return true;
		}
		return false;
	}

	//If line isn't empty, remove the first vehicle on the line
	public Vehicle removeVehicleStart(){
		if(currentNum ==0)
			return null;

		currentNum--;
		return vehicles.remove();
	}

/*	//Copy the first vehicle on the line
	public Vehicle copyVehicleStart() {

		if(currentNum ==0) {
			return null;
		}

		Vehicle copy = new Vehicle();
		Vehicle vFirst = vehicles.peek();
		copy.setUniqueID(vFirst.getUniqueID());
		copy.setAxles(vFirst.getAxles());
		copy.setTollType(vFirst.getTollType());
		copy.setArriveLineTime(vFirst.getArriveLineTime());
		copy.setArriveBoothTime(vFirst.getArriveBoothTime());
		copy.setLeaveTime(vFirst.getLeaveTime());

		return copy;
	}*/

	//Update first vehicles arrival and leave times
	public boolean updateVehicleStart(int arrivalTime, int delayTime) {
		if(currentNum ==0) {
			return false;
		}
		int leaveTime= arrivalTime+vehicles.peek().getAxles()* delayTime;
		vehicles.peek().setArriveBoothTime(arrivalTime);
		vehicles.peek().setLeaveTime(leaveTime);
		return true;
	}


	public String toString() {
		String output= "";
		Iterator iterator = vehicles.iterator();
    while (iterator.hasNext()) {
        output +=  iterator.next().toString() + "\n";
    }
		output += "Maximum length: " + maxLength;
		return output;
	}

}
