public class Geometry {
	public static double getAltitude(double ra, double dec, Settings settings) {
		double ha = getHa(ra, settings.GST, settings.longitude);
		double alt = getAlt(dec, settings.latitude, ha);
		return alt;
	}
	
	private static double getHa(double ra, double GST, double longitude) {
		return (GST + longitude/15 - ra/15);
	}
	
	private static double getAlt(double dec, double lat, double ha) {
        double haDeg = ha * 15;
        double sinA =
                Math.sin(Math.toRadians(dec))
                * Math.sin(Math.toRadians(lat))
                + Math.cos(Math.toRadians(dec))
                * Math.cos(Math.toRadians(lat))
                * Math.cos(Math.toRadians(haDeg));
        double a = Math.asin(sinA);
        return Math.toDegrees(a);
    }
}
