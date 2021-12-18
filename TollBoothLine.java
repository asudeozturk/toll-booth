public class TollBoothLine{

	private Vehicle[] vehicles;
	private int limit;
	private int currentNum;
	private int maxLength;

	public TollBoothLine(int size){
		limit = size;
		vehicles = new Vehicle[limit];
	}

	int getLength() { return currentNum; }
	int getMaxLength() { return maxLength; }


	//If space available, add new vehicle at the end of the line
	public boolean addVehicleEnd(Vehicle v) {
		for(int i = 0; i < vehicles.length; i++) {
			if(vehicles[i] == null) {
				vehicles[i] = v;
				currentNum++;
				if(currentNum > maxLength)
					maxLength++;
				return true;
			}
		}
		return false;
	}

	//If line isn't empty, remove the first vehicle on the line
	//Move other vehicles to front
	public Vehicle removeVehicleStart() {
		Vehicle obj= null;
		if(vehicles != null){
			obj = copyVehicleStart();
			for(int i =0; i < currentNum; i++) {
					vehicles[i] = vehicles[i+1];
			}
			vehicles[currentNum -1] = null;
			currentNum--;
		}
		return obj;
	}

	//Copy the first vehicle on the line
	public Vehicle copyVehicleStart() {
		Vehicle copy = null;
		if(vehicles != null){
			copy = new Vehicle();
			copy.setUniqueID(vehicles[0].getUniqueID());
			copy.setAxles(vehicles[0].getAxles());
			copy.setTollType(vehicles[0].getTollType());
			copy.setArriveLineTime(vehicles[0].getArriveLineTime());
			copy.setArriveBoothTime(vehicles[0].getArriveBoothTime());
			copy.setLeaveTime(vehicles[0].getLeaveTime());
		}
		return copy;
	}

	//Put given vehicle at the beginning of line
	public boolean replaceVehicleStart(Vehicle v) {
		if(vehicles != null){
			vehicles[0] = v;
			return true;
		}
		return false;
	}

	public String toString() {
		String output= "";
		for(int i =0; i < vehicles.length; i++){
			if(vehicles[i] != null)
				output +=  vehicles[i].toString() + "\n";
		}
		return output + "Maximum length: " + maxLength;
	}

}
