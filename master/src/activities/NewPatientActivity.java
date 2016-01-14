package activities;

import global.AppState;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.echeung.triage207.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import classes.Patient;

public class NewPatientActivity extends Activity {

	private String birthday;
	private String currentTime;

	private EditText mLastName;
	private EditText mFirstName;
	private EditText mHealthCard;
	private Button mDob;

	// Based on code from http://goo.gl/GxYXO4
	// Update the label on the date of birth button when the date picker is used
	private final Calendar calendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			updateDobLabel();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);

		mLastName = (EditText) findViewById(R.id.new_lastname);
		mFirstName = (EditText) findViewById(R.id.new_firstname);
		mHealthCard = (EditText) findViewById(R.id.new_healthcard);

		currentTime = DateFormat.getDateTimeInstance().format(new Date());
		((TextView) findViewById(R.id.current_time)).setText(currentTime);

		birthday = "";
		mDob = (Button) findViewById(R.id.dob);
		// Set the date picker dialog to show the current date when opened
		mDob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(NewPatientActivity.this, date, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			createPatient();
			return true;
		case R.id.action_discard:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Updates the date of birth button text. */
	private void updateDobLabel() {
		birthday = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
				.format(calendar.getTime());
		mDob.setText("Date of birth: " + birthday);
	}

	private void createPatient() {
		final String lastName = mLastName.getText().toString();
		final String firstName = mFirstName.getText().toString();
		final String healthCard = mHealthCard.getText().toString();

		if (firstName.isEmpty() || lastName.isEmpty() || healthCard.isEmpty()
				|| birthday.isEmpty()) {
			Toast.makeText(this, getString(R.string.empty_fields),
					Toast.LENGTH_SHORT).show();
		} else {
			if (AppState.getPatient(healthCard) == null) {
				AppState.addPatient(new Patient(firstName, lastName, birthday,
						healthCard, currentTime));

				try {
					AppState.savePatients();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.file_error), Toast.LENGTH_SHORT)
							.show();
				}

				Toast.makeText(this, getString(R.string.patient_added),
						Toast.LENGTH_SHORT).show();

				this.finish();
			} else {
				Toast.makeText(this, getString(R.string.patient_exists),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
