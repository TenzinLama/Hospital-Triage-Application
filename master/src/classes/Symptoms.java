package classes;

public class Symptoms extends Record {

	private final String symptoms;

	public Symptoms(String symptoms) {
		super();
		this.symptoms = symptoms;
	}

	public String getSymptoms() {
		return symptoms;
	}

	/**
	 * Returns this Symptoms's string representation, in a CSV format.
	 *
	 * @return This Symptoms's string representation, in a CSV format.
	 */
	@Override
	public String toString() {
		return String.format("%s=%s", this.getTime(), this.getSymptoms()
				.replace(",", "."));
	}

}
