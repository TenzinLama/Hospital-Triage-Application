package classes;

import global.AppState;

public class Nurse extends User<Nurse> {

	public Nurse() {
		super(null, null);
	}

	public Nurse(String username, String password) {
		super(username, password);
	}

	/**
	 * Records when the Patient has been seen by a doctor at a certain time.
	 *
	 * @param patient
	 *            The Patient seen by a doctor.
	 * @param time
	 *            The time of the doctor's visit.
	 */
	public void recordPatientSeen(Patient patient, String time) {
		patient.addTimeDoctor(time);
	}

	/**
	 * Records a Patient in the ER database.
	 *
	 * @param patient
	 *            The Patient to record.
	 */
	public void recordPatientData(Patient patient) {
		AppState.addPatient(patient);
	}

	/**
	 * Records a Patient's vitals.
	 *
	 * @param patient
	 *            A Patient with vital signs being recorded.
	 * @param vitals
	 *            The Patient's vital signs.
	 */
	public void addVitals(Patient patient, Vitals vitals) {
		patient.addVitals(vitals);
	}

	/**
	 * Records a Patient's symptoms.
	 *
	 * @param patient
	 *            A Patient with symptoms being recorded.
	 * @param symptoms
	 *            The Patient's symptoms.
	 */
	public void addSymptoms(Patient patient, Symptoms symptoms) {
		patient.addSymptoms(symptoms);
	}

	/**
	 * Parses the contents of fields and instantiates this Nurse object. It is
	 * then added to the ER database.
	 *
	 * @param fields
	 *            An array of contents used to instantiate this Nurse.
	 */
	@Override
	public void scan(String[] fields) {
		Nurse nurse = new Nurse(fields[0], fields[1]);
		AppState.addNurse(nurse);
	}

}
