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
			
			System.out.println("Pocetni podaci:");
			double r = Double.valueOf(c.readLine("Populacijski indeks = ")).doubleValue();
			double lm = Double.valueOf(c.readLine("Limitna magnituda = ")).doubleValue();
			double RaRad = Double.valueOf(c.readLine("Ra radianta (u stupnjevima) = ")).doubleValue();
			double DecRad = Double.valueOf(c.readLine("Dec radianta (u stupnjevima) = ")).doubleValue();
			double latitude = Double.valueOf(c.readLine("Latituda promatraca (u stupnjevima) = ")).doubleValue();
			double longitude = Double.valueOf(c.readLine("Longituda promatraca (u stupnjevima) = ")).doubleValue();
			int Y = Integer.valueOf(c.readLine("Godina = ")).intValue();
			int M = Integer.valueOf(c.readLine("Mjesec = ")).intValue();
			int D = Integer.valueOf(c.readLine("Dan = ")).intValue();
			System.out.print("\n---------------------------\n\n");
			
			Settings sett = new Settings(latitude, longitude);
			
			while(true) {
				int hour = Integer.valueOf(c.readLine("Sati (Lokalno) = ")).intValue();
				int minute = Integer.valueOf(c.readLine("Minute (Lokalno) = ")).intValue();
				sett.setTime(Y, M-1, D, hour, minute);
				double altRad = Geometry.getAltitude(RaRad, DecRad, sett);
				System.out.println("Visina radianta = " + altRad + "\n");
				
				int N = Integer.valueOf(c.readLine("Broj meteora = ")).intValue();
				double Teff = Double.valueOf(c.readLine("Vrijeme (u minutama) = ")).doubleValue() / 60;
				double HR = N/Teff;
				System.out.print("HR = " + HR + "\n\n");
				
				double k = Double.valueOf(c.readLine("Korekcija vidnog polja (naoblaka) = ")).doubleValue();
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