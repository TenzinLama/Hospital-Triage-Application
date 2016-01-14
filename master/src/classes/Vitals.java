package classes;

public class Vitals extends Record {

	/** This Vitals's temperature. */
	private final double temperature;

	/** This Vitals's systoolic and diastolic blood pressures. */
	private final double systolicBP, diastolicBP;

	/** This Vitals's heart rate. */
	private final double heartRate;

	/**
	 * Constructs a Vitals with the Patient's temperature, blood pressure, heart
	 * rate, and the current time.
	 *
	 * @param temperature
	 *            This Patient's temperature, in Celsius.
	 * @param systolic
	 *            This Patient's systolic blood pressure, in mmHg.
	 * @param diastolic
	 *            This Patient's diastolic blood pressure, in mmHg.
	 * @param heartRate
	 *            This Patient's heart rate, in bpm.
	 */
	public Vitals(double temperature, double systolic, double diastolic,
			double heartRate) {
		super();
		this.temperature = temperature;
		this.systolicBP = systolic;
		this.diastolicBP = diastolic;
		this.heartRate = heartRate;
	}

	/**
	 * Returns this Vitals's temperature, in Celsius.
	 * 
	 * @return This Vitals's temperature, in Celsius.
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Returns this Vitals's systolic blood pressure, in mmHg.
	 * 
	 * @return This Vitals's systolic blood pressure, in mmHg.
	 */
	public double getSystolicBP() {
		return systolicBP;
	}

	/**
	 * Returns this Vitals's diastolic blood pressure, in mmHg.
	 * 
	 * @return This Vitals's diastolic blood pressure, in mmHg.
	 */
	public double getDiastolicBP() {
		return diastolicBP;
	}

	/**
	 * Returns this Vitals's heart rate, in bpm.
	 * 
	 * @return This Vitals's heart rate, in bpm.
	 */
	public double getHeartRate() {
		return heartRate;
	}

	/**
	 * Returns this Vitals's string representation, in a CSV format.
	 * 
	 * @return This Vitals's string representation, in a CSV format.
	 */
	@Override
	public String toString() {
		return this.getTime() + "=" + this.getTemperature() + ","
				+ this.getSystolicBP() + "," + this.getDiastolicBP() + ","
				+ this.getHeartRate();
	}

}
