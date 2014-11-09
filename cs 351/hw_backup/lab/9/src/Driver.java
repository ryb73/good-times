import edu.uwm.cs351.Car;
import edu.uwm.cs351.Track;


public class Driver {

	public static void main(String[] args) {
		Track north = new Track("north");
		Track south = new Track("south");
		Track midlands = new Track("midlands");
		midlands.addCar(new Car("fluffies", 2));
		midlands.addCar(new Car("iron", 20));
		midlands.addCar(new Car("beans", 6));
		midlands.addCar(new Car("widgets", 9));

		//now we have a track that looks like:
		//		      widgets   beans   iron   fluffies  
		
		//use the operations in specified in Track.h to produce the following
		//on the midlands track:
		//		      fluffies  beans   widgets   iron   
		north.addCar(midlands.removeCar()); // widgets
		south.addCar(midlands.removeCar()); // beans
		north.addCar(midlands.removeCar()); // iron
		south.addCar(midlands.removeCar()); // fluffies
		midlands.addCar(north.removeCar()); // iron
		midlands.addCar(north.removeCar()); // widgets
		midlands.addCar(south.removeCar()); // fluffies
		north.addCar(midlands.removeCar()); // fluffies
		midlands.addCar(south.removeCar()); // beans
		midlands.addCar(north.removeCar()); // fluffies
		
		//then uncomment these lines:
		
		if (midlands.willTrainPassdepot()) {
			System.out.println("This Train has passed the depot successfully.");
		}
		else {
			System.out.println("This Train has heavy loads towards the back, will not pass.");   
		}
		
		return;
	}

}
