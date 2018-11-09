package qwerty;

public class Transport {
	private int engines;
	private int wheels;
	private int windows;
	private int takeoffspeed;
	public Transport(int e,int w,int win,int ts) {
		this.engines = e;
		this.wheels = w;
		this.windows = win;
		this.takeoffspeed = ts;
	}
	public void fly(int fll,int ffl) {
		System.out.println("flying 10 min");
	}
	
	
	public int getEngines() {
		return engines;
	}
	public void setEngines(int engines) {
		this.engines = engines;
	}
	public int getWheels() {
		return wheels;
	}
	public void setWheels(int wheels) {
		this.wheels = wheels;
	}
	public int getWindows() {
		return windows;
	}
	public void setWindows(int windows) {
		this.windows = windows;
	}
	public int getTakeoffspeed() {
		return takeoffspeed;
	}
	public void setTakeoffspeed(int takeoffspeed) {
		this.takeoffspeed = takeoffspeed;
	}
	
}
