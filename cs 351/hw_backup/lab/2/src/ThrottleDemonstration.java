import edu.uwm.cs351.Throttle;



public class ThrottleDemonstration {
	public static void main(String[] args) {
		final int SIZE = 8;

		Throttle small = new Throttle(SIZE);

		System.out.println("I am now shifting a Throttle fully on, and then I");
		System.out.println("will shift it back to the shutoff position.");

		small.shift(SIZE);
		while(small.isOn()) {
			System.out.printf("The flow is now %5.3f\n", small.getFlow());
			small.shift(-1);
		}

		System.out.println("The flow is now off.");
	}
}