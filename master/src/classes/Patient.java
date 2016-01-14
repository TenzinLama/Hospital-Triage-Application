package classes;

import global.AppState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

public class Patient extends Person<Patient> {

	/** This Patient's first name, last name, and date of birth. */
	private String firstName, lastName, dob;

	/** This Patient's health card number. */
	private String healthCard;

	/** This Patient's arrival time. */
	private String arrivalTime;

	/** This Patient's prescriptions. */
	private List<Prescription> prescriptions;

	/** This Patient's symptoms records. */
	private List<Symptoms> symptoms;

	/** This Patient's vitals records. */
	private List<Vitals> vitals;

	/** This Patient's urgency. */
	private Byte urgency;

	/** The times that this Patient has been seen by a doctor. */
	private List<String> timeDoctor;

	/** True if this Patient is improving. */
	private Boolean isImproving;

	/** Empty constructor for FileHelper */
	public Patient() {
	}

	public Patient(String firstName, String lastName, String dob,
			String healthCard, String arrivalTime) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.healthCard = healthCard;

		this.arrivalTime = arrivalTime;

		this.prescriptions = new ArrayList<Prescription>();
		this.symptoms = new ArrayList<Symptoms>();
		this.vitals = new ArrayList<Vitals>();

		this.urgency = 0;
		this.timeDoctor = new ArrayList<String>();

		this.isImproving = false;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return String.format("%s, %s", this.getLastName(), this.getFirstName());
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getHealthCard() {
		return healthCard;
	}

	public void setHealthCard(String healthCard) {
		this.healthCard = healthCard;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(List<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public void addPrescription(Prescription prescription) {
		this.prescriptions.add(0, prescription);
	}

	public List<Symptoms> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List<Symptoms> symptoms) {
		this.symptoms = symptoms;
	}

	public void addSymptoms(Symptoms symptoms) {
		this.symptoms.add(0, symptoms);
	}

	public List<Vitals> getVitals() {
		return vitals;
	}

	public void setVitals(List<Vitals> vitals) {
		this.vitals = vitals;
	}

	public void addVitals(Vitals vitals) {
		this.vitals.add(0, vitals);
		updateUrgencyImproving();
	}

	public Byte getUrgency() {
		return urgency;
	}

	public void setUrgency(Byte urgency) {
		this.urgency = urgency;
	}

	public List<String> getTimeDoctor() {
		return timeDoctor;
	}

	public void setTimeDoctor(List<String> timeDoctor) {
		this.timeDoctor = timeDoctor;
	}

	public void addTimeDoctor(String timeDoctor) {
		this.timeDoctor.add(0, timeDoctor);
	}

	public Boolean getIsImproving() {
		return isImproving;
	}

	public void setIsImproving(Boolean isImproving) {
		this.isImproving = isImproving;
	}

	public void updateUrgencyImproving() {
		if (!this.getVitals().isEmpty()) {
			byte urgency = 0;
			Vitals v = this.getVitals().get(0);

			// age < 2 years
			if (calculateAge(this.getDob()) < 2)
				urgency += 1;

			// Temp >= 39
			if (v.getTemperature() >= 39.0)
				urgency += 1;

			// BP: sys >= 140 or dia >= 90
			if (v.getSystolicBP() >= 140.0 || v.getDiastolicBP() >= 90.0)
				urgency += 1;

			// heart rate: >= 100 or <= 50
			if (v.getHeartRate() >= 100.0 || v.getHeartRate() <= 50)
				urgency += 1;

			byte prevUrgency = this.urgency;
			this.setUrgency(urgency);
			this.setIsImproving(!(prevUrgency < urgency));
		}
	}

	/**
	 * Calculates and returns this Patient's age relative to the current date.
	 *
	 * @param dob
	 *            This Patient's date of birth.
	 * @return This Patient's age relative to the current date.
	 */
	@SuppressLint("SimpleDateFormat")
	private int calculateAge(String dob) {
		int year, month, day, yearDob, monthDob, dayDob, age;

		// Date of birth
		yearDob = Integer.parseInt(dob.substring(0, 4));
		monthDob = Integer.parseInt(dob.substring(5, 7));
		dayDob = Integer.parseInt(dob.substring(8, 10));

		// Today's date
		SimpleDateFormat dateFormat;
		Date date = new Date();

		dateFormat = new SimpleDateFormat("yyyy");
		year = Integer.parseInt(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("MM");
		month = Integer.parseInt(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("dd");
		day = Integer.parseInt(dateFormat.format(date));

		// Calculate age
		age = year - yearDob;

		// Birthday checks
		if (month < monthDob)
			--age;
		if (month == monthDob && day < dayDob)
			--age;

		return age;
	}

	/**
	 * Parses the contents of fields and instantiates a Patient object. It is
	 * then added to the ER database.
	 *
	 * @param fields
	 *            An array of contents used to instantiate the object.
	 */
	@Override
	public void scan(String[] fields) {
		Patient patient = new Patient(fields[0], fields[1], fields[2],
				fields[3], fields[4].replace(";", ","));
		scanRecords(patient, new String[] { fields[5], fields[6], fields[7],
				fields[8], fields[9], fields[10] });

		AppState.addPatient(patient);
	}

	/**
	 * Parses the fields that contain this Patient's records and adds it to the
	 * Patient object.
	 *
	 * @param patient
	 *            The Patient to add the records to.
	 * @param fields
	 *            The records to add to the Patient.
	 */
	private void scanRecords(Patient patient, String[] fields) {
		// symptoms
		if (fields[0].indexOf(';') > 1) {
			for (String s : fields[0].split(";")) {
				String[] symptom = s.split("=");
				Symptoms newSymptoms = new Symptoms(
						symptom[1].replace(".", ","));
				newSymptoms.setTime(symptom[0]);
				patient.addSymptoms(newSymptoms);
			}
		}

		// vitals
		if (fields[1].indexOf(';') > 1) {
			for (String v : fields[1].split(";")) {
				String[] vital = v.split("=");
				String[] numbers = vital[1].split(",");
				Vitals newVitals = new Vitals(Double.parseDouble(numbers[0]),
						Double.parseDouble(numbers[1]),
						Double.parseDouble(numbers[2]),
						Double.parseDouble(numbers[3]));
				newVitals.setTime(vital[0]);
				patient.addVitals(newVitals);
			}
		}

		// urgency
		patient.setUrgency(Byte.valueOf(fields[2]));

		// isImproving
		patient.setIsImproving(Boolean.parseBoolean(fields[3]));

		// timeDoctor
		if (fields[4].indexOf(';') > 1)
			for (String t : fields[4].split(";"))
				patient.addTimeDoctor(t);

		// prescriptions
		if (fields[5].indexOf(';') > 1) {
			for (String p : fields[5].split(";")) {
				String[] prescription = p.split("=");
				String[] parts = prescription[1].split(",");
				Prescription newPrescription = new Prescription(
						parts[0].replace(".", ","), parts[1].replace(".", ","));
				newPrescription.setTime(prescription[0]);
				patient.addPrescription(newPrescription);
			}
		}
	}

	/**
	 * Returns this Patient's string representation, that includes of all of the
	 * Patient's information and records, in CSV format.
	 *
	 * @return This Patient's string representation, in CSV format.
	 */
	@Override
	public String toString() {
		// Symptoms records, from oldest to newest
		String symptomsList = "";
		if (this.getSymptoms().isEmpty())
			symptomsList = " ";
		else
			for (Symptoms s : this.getSymptoms())
				symptomsList = s.toString() + ";" + symptomsList;

		// Vitals records, from oldest to newest
		String vitalsList = "";
		if (this.getVitals().isEmpty())
			vitalsList = " ";
		else
			for (Vitals v : this.getVitals())
				vitalsList = v.toString() + ";" + vitalsList;

		// Times seen by doctor, from oldest to newest
		String timeDoctorList = "";
		if (this.getTimeDoctor().isEmpty())
			timeDoctorList = " ";
		else
			for (String t : this.getTimeDoctor())
				timeDoctorList = t.toString() + ";" + timeDoctorList;

		// Prescription records, from oldest to newest
		String prescriptionList = "";
		if (this.getPrescriptions().isEmpty())
			prescriptionList = " ";
		else
			for (Prescription p : this.getPrescriptions())
				prescriptionList = p.toString() + ";" + prescriptionList;

		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
				this.firstName, this.lastName, this.dob, this.healthCard,
				this.arrivalTime.replace(",", ";"), symptomsList, vitalsList,
				this.urgency, this.isImproving, timeDoctorList,
				prescriptionList);
	}
}
