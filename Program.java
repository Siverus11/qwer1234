package qwerty;

public class Program {

	public static void main(String[] args) {
		Plane boeing747 = new Plane("White",700,1000,500,4,3,2000,200);
		Plane a320 = new Plane(4,3,2000,250);
		System.out.println(boeing747.getMaxspeed());
		System.out.println(a320.getEngines());
	}

}
