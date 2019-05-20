public class Vehicle {
	
	private int uniqueID;
	private int axles;
	private String tollType;
	private int arriveLineTime;
	private int arriveBoothTime;
	private int leaveTime;
	public static int objectNum = 1;
	
	public Vehicle(){}
	
	public Vehicle(int a, String t, int l){
		setUniqueID(objectNum);
		setAxles(a);
		setTollType(t);
		setArriveLineTime(l);
		objectNum++;
	}	
	
	int getUniqueID() { return uniqueID ;}
	int getAxles() { return axles ;}
	String getTollType() { return tollType ;}
	int getArriveLineTime() { return arriveLineTime;}
	int getArriveBoothTime() { return arriveBoothTime ;}
	int getLeaveTime() { return leaveTime;}
	
	public void setUniqueID(int i){  
		uniqueID = ( i > 0 ? i : 0 );
		
	}
	public void setAxles(int a){
		axles = ( a > 0 ? a : 0 );
	}
	public void setTollType(String t){    
		tollType = (t.equals("A") || t.equals("M") ? t: "");
	}
	
	public void setArriveLineTime(int t){
		arriveLineTime = (t > 0 ? t:0);
	}

	public void setArriveBoothTime(int t){
		arriveBoothTime = (t>0 && t>= arriveLineTime ? t:0);
	}

	public void setLeaveTime(int t){
		leaveTime = (t>0 && t>=arriveBoothTime ? t:0);
	}
	
	public String toString(){ 
		return uniqueID + " " + axles + " " + tollType + " " + 
		arriveLineTime + " " + arriveBoothTime + " " + leaveTime;
	}
}
