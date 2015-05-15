import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Settings {
	public double GST=0,
				latitude=45.325,
				longitude=14.482778;
	
	public Settings(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setTime(int y, int m, int d, int h, int min) {
		Calendar cal = new GregorianCalendar();
		cal.set(y, m, d, h, min, 0);
		GST = 18.697374558 + (24.06570982441908 * daysSince2000(cal.getTime()));
		while (GST>24) {
			GST-=24;
		}
	}
	
	public void setTimeNow() {
		Calendar cal = new GregorianCalendar();
		GST = 18.697374558 + (24.06570982441908 * daysSince2000(cal.getTime()))+1;
		while (GST>24) {
			GST-=24;
		}
	}
	
	private static double daysSince2000(Date date){
		Calendar cal = new GregorianCalendar();
		cal.set(2000, 0, 1, 12, 0, 0); 
		return (double)(date.getTime() - cal.getTime().getTime() - (1000 * 60 * 60)) / (1000 * 60 * 60 * 24);
	}
}
