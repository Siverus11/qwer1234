package qwerty;

public class Plane extends Transport {
	private String color;
	private int maxspeed;
	private int maxhight;
	private int seats;
	public Plane(String c,int ms,int mh, int s, int e,int w,int win,int ts){
		super(e,w,win,ts);
		this.color = c;
		this.maxspeed = ms;
		this.maxhight = mh;
		this.seats = s;
	}
	public Plane(int e,int w,int win,int ts) {
		super(e,w,win,ts);
	}
	
	void fly(int fl) {
		System.out.println("flying 5 min");
	}
	void takeoff() {
		System.out.println("taking off...");
	}
	void landing() {
		System.out.println("landing...");
	}
	
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getMaxspeed() {
		return maxspeed;
	}
	public void setMaxspeed(int maxspeed) {
		this.maxspeed = maxspeed;
	}
	public int getMaxhight() {
		return maxhight;
	}
	public void setMaxhight(int maxhight) {
		this.maxhight = maxhight;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	
	
}
