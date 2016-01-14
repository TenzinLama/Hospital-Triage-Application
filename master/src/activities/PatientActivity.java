package activities;

import global.AppState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import me.echeung.triage207.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import classes.Nurse;
import classes.Patient;
import classes.Prescription;
import classes.Symptoms;
import classes.Vitals;

public class PatientActivity extends Activity {

	private Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);

		String healthCard = getIntent().getStringExtra("PATIENT");
		patient = AppState.getPatients().get(healthCard);

		// Display name
		((TextView) findViewById(R.id.name)).setText(patient.getName());

		// Display date of birth
		((TextView) findViewById(R.id.dob)).setText(patient.getDob());

		// Display health card number
		((TextView) findViewById(R.id.healthCard)).setText(patient
				.getHealthCard());

		// Display arrival time
		((TextView) findViewById(R.id.arrival)).setText(patient
				.getArrivalTime());

		updateStats();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.patient, menu);

		if (AppState.getCurrentUser() instanceof Nurse) {
			// Hide Physician-related actions if Nurse logged in
			menu.findItem(R.id.action_prescription).setVisible(false);
			((Button) findViewById(R.id.add_prescription))
			.setVisibility(View.GONE);
		} else {
			// Hide Nurse-related actions if Physician logged in
			menu.findItem(R.id.action_vitals).setVisible(false);
			menu.findItem(R.id.action_symptoms).setVisible(false);
			menu.findItem(R.id.action_doctor).setVisible(false);

			((Button) findViewById(R.id.add_vitals)).setVisibility(View.GONE);
			((Button) findViewById(R.id.add_symptoms)).setVisibility(View.GONE);
			((Button) findViewById(R.id.add_time)).setVisibility(View.GONE);
		}

		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_vitals:
			showActivity(VitalsActivity.class);
			return true;
		case R.id.action_symptoms:
			showActivity(SymptomsActivity.class);
			return true;
		case R.id.action_doctor:
			// Get the current time and add it to the Patient's records
			patient.addTimeDoctor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(Calendar.getInstance().getTime()));
			updateStats();
			return true;
		case R.id.action_prescription:
			showActivity(PrescriptionActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Shows the Add Vitals activity.
	 *
	 * @param view
	 *            The button view.
	 */
	public void addVitals(View view) {
		showActivity(VitalsActivity.class);
	}

	/**
	 * Shows the Add Symptoms activity.
	 *
	 * @param view
	 *            The button view.
	 */
	public void addSymptoms(View view) {
		showActivity(SymptomsActivity.class);
	}

	/**
	 * Shows the Add Prescription activity.
	 *
	 * @param view
	 *            The button view.
	 */
	public void addPrescription(View view) {
		showActivity(PrescriptionActivity.class);
	}

	/**
	 * Adds the current time and adds it to the Patient's records
	 *
	 * @param view
	 *            The button view.
	 */
	@SuppressLint("SimpleDateFormat")
	public void addTime(View view) {
		patient.addTimeDoctor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
		updateStats();
	}

	/**
	 * Starts and displays the specified activity, passing in the Patient.
	 *
	 * @param activity
	 *            The activity to display.
	 */
	private void showActivity(Class<?> activity) {
		Intent intent = new Intent(this, activity);
		intent.putExtra("PATIENT", patient.getHealthCard());
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			updateStats();
		}
	}

	/**
	 * Updates the lists of vitals, symptoms, and times seen by the doctor for
	 * the currently viewed Patient. The urgency and isImproving messages are
	 * also updated to reflect changes.
	 */
	private void updateStats() {
		// Display urgency
		Byte urgency = patient.getUrgency();
		TextView urgencyText = (TextView) findViewById(R.id.urgency);
		String strUrgency = "%sUrgent (" + urgency + ")";
		if (urgency < 2) {
			urgencyText.setText(String.format(strUrgency, "Non "));
			urgencyText.setTextColor(getResources().getColor(
					R.color.darker_text));
		} else if (urgency < 3) {
			urgencyText.setText(String.format(strUrgency, "Less "));
			urgencyText.setTextColor(getResources().getColor(
					R.color.less_urgent));
		} else {
			urgencyText.setText(String.format(strUrgency, ""));
			urgencyText.setTextColor(getResources().getColor(R.color.urgent));
		}

		// Display isImproving
		TextView improvingText = (TextView) findViewById(R.id.improving);
		improvingText.setText(patient.getIsImproving() ? "Improving"
				: "Not improving");

		// Display Vitals records
		List<Vitals> vitals = patient.getVitals();
		if (!vitals.isEmpty()) {
			String vitalsText = "";
			for (Vitals v : vitals)
				vitalsText += String.format("\n%s\nTemperature: %s\u00B0C\n",
						v.getTime(), v.getTemperature())
						+ String.format("Blood pressure: %s mmHg / %s mmHg\n",
								v.getSystolicBP(), v.getDiastolicBP())
						+ String.format("Heart rate: %s bpm\n",
								v.getHeartRate());

			((TextView) findViewById(R.id.vitals)).setText(vitalsText);
		}

		// Display Symptoms records
		List<Symptoms> symptoms = patient.getSymptoms();
		if (!symptoms.isEmpty()) {
			String symptomsText = "";
			for (Symptoms s : symptoms)
				symptomsText += String.format("\n%s\n%s\n", s.getTime(),
						s.getSymptoms());

			((TextView) findViewById(R.id.symptoms)).setText(symptomsText);
		}

		// Display Doctor visit records
		List<String> doctor = patient.getTimeDoctor();
		if (!doctor.isEmpty()) {
			String doctorText = "";
			for (String s : doctor)
				doctorText += String.format("\n%s\n", s);

			((TextView) findViewById(R.id.doctor)).setText(doctorText);
		}

		// Display Prescription records
		List<Prescription> prescriptions = patient.getPrescriptions();
		if (!prescriptions.isEmpty()) {
			String prescriptionsText = "";
			for (Prescription p : prescriptions)
				prescriptionsText += String.format(
						"\n%s\nName: %s\nInstructions: %s\n", p.getTime(),
						p.getName(), p.getInstructions());

			((TextView) findViewById(R.id.prescriptions))
					.setText(prescriptionsText);
		}
	}
}
