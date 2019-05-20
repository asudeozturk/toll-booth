public class TollBoothLine{
	private Vehicle[] vehicles;
	private int limit;
	private int currentNum;
	private int maxLength;
	
	public TollBoothLine(int size){ 
		limit = size;
		vehicles = new Vehicle[limit];	
	}
	
	int getLength() { return currentNum;}
	int getMaxLength() { return maxLength;}
	
	public boolean addVehicleEnd(Vehicle v) { 
		boolean condition = false;
		for(int i = 0; i < vehicles.length; i++) {
			if(vehicles[i] == null) {
				vehicles[i] = v;
				condition = true;
				currentNum++;
				if(currentNum > maxLength) 
					maxLength++;
				break;				
			}	
		}
		return condition;
	}
	
	public Vehicle removeVehicleStart() { 
		Vehicle obj= null;
		if(vehicles != null){
			obj = copyVehicleStart();
			
			for(int i =0; i < currentNum; i++) {
				if(i == (currentNum -1)){
					vehicles[i] = null;
					currentNum--;
				}
				else {
					vehicles[i] = vehicles[i+1];
				}		
			}	
		}
		return obj;
	}
	
	public Vehicle copyVehicleStart() { 
		Vehicle copy = new Vehicle();
		if(vehicles != null){
			copy.setUniqueID(vehicles[0].getUniqueID());
			copy.setAxles(vehicles[0].getAxles());
			copy.setTollType(vehicles[0].getTollType());
			copy.setArriveLineTime(vehicles[0].getArriveLineTime());
			copy.setArriveBoothTime(vehicles[0].getArriveBoothTime());
			copy.setLeaveTime(vehicles[0].getLeaveTime());
		}
		else
			copy =null;
		return copy;
	}
	
	public boolean replaceVehicleStart(Vehicle v) { 
		boolean condition = true;
		if(vehicles != null){
			vehicles[0] = v;
		}
		else 
			condition = false;
		return condition;
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