import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ZHR {
	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			Process p = Runtime.getRuntime().exec("cmd.exe /k start java -jar " + (new File(ZHR.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getAbsolutePath() + " cmd");
		} else {
			Console c = System.console();
			if (c == null) {
				System.exit(1);
			}
			
			double r = Double.valueOf(c.readLine("Population index = ")).doubleValue();
			double lm = Double.valueOf(c.readLine("Limiting magnitude = ")).doubleValue();
			double RaRad = Double.valueOf(c.readLine("RA of radiant (degrees) = ")).doubleValue();
			double DecRad = Double.valueOf(c.readLine("DEC of radiant (degrees) = ")).doubleValue();
			double latitude = Double.valueOf(c.readLine("Latitude (degrees) = ")).doubleValue();
			double longitude = Double.valueOf(c.readLine("Longitude (degrees) = ")).doubleValue();
			int Y = Integer.valueOf(c.readLine("Year = ")).intValue();
			int M = Integer.valueOf(c.readLine("Month = ")).intValue();
			int D = Integer.valueOf(c.readLine("Day = ")).intValue();
			System.out.print("\n---------------------------\n\n");
			
			Settings sett = new Settings(latitude, longitude);
			
			while(true) {
                System.out.println("Local time:");
				int hour = Integer.valueOf(c.readLine("Hours = ")).intValue();
				int minute = Integer.valueOf(c.readLine("Minutes = ")).intValue();
				sett.setTime(Y, M-1, D, hour, minute);
				double altRad = Geometry.getAltitude(RaRad, DecRad, sett);
				System.out.println("Radiant altitude = " + altRad + "\n");
				
				int N = Integer.valueOf(c.readLine("Number of meteors observed = ")).intValue();
				double Teff = Double.valueOf(c.readLine("Effective time of observation session (minutes) = ")).doubleValue() / 60;
				double HR = N/Teff;
				System.out.print("Hourly rate = " + HR + "\n\n");
				
				double k = Double.valueOf(c.readLine("FOV cloude coverage factor (percentage) = ")).doubleValue()/100;
				double F = 1 / (1 - k);
				System.out.print("F = " + F + "\n\n");
				
				double ZHR = (HR * F * Math.pow(r, (6.5 - lm)))/(Math.sin(Math.toRadians(altRad)));
				System.out.print("ZHR = " + ZHR + "\n--------------------------------------------\n\n");
			}
		}
	}
}

class SyncPipe implements Runnable {
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
		istrm_ = istrm;
		ostrm_ = ostrm;
	}
	
	public void run() {
		try {
			final byte[] buffer = new byte[1024];
			for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
				ostrm_.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private final OutputStream ostrm_;
	private final InputStream istrm_;
}