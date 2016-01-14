package activities;

import global.AppState;
import me.echeung.triage207.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import classes.Patient;
import classes.Prescription;

public class PrescriptionActivity extends Activity {

	private Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescription);

		String healthCard = getIntent().getStringExtra("PATIENT");
		patient = AppState.getPatients().get(healthCard);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.prescription, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			String medication = ((EditText) findViewById(R.id.medication))
					.getText().toString();
			String instructions = ((EditText) findViewById(R.id.instructions))
					.getText().toString();

			// Add the Prescription to the Patient object iff it's not blank
			if (!medication.isEmpty() && !instructions.isEmpty()) {
				if (medication.indexOf('=') < 1
						&& instructions.indexOf('=') < 1) {
					patient.addPrescription(new Prescription(medication,
							instructions));
					this.finish();
					return true;
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.equals_error, Toast.LENGTH_SHORT).show();
					return false;
				}
			}

			return true;
		case R.id.action_discard:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
