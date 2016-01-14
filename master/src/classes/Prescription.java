package classes;

public class Prescription extends Record {

	private String name;
	private String instructions;

	public Prescription(String name, String instructions) {
		super();
		this.name = name;
		this.instructions = instructions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * Returns this Prescription's string representation, in a CSV format.
	 *
	 * @return This Prescription's string representation, in a CSV format.
	 */
	@Override
	public String toString() {
		return String.format("%s=%s,%s", this.getTime(), this.getName(), this
				.getInstructions().replace(",", "."));
	}
}
