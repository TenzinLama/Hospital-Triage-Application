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
import classes.Symptoms;

public class SymptomsActivity extends Activity {

	private Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symptoms);

		String healthCard = getIntent().getStringExtra("PATIENT");
		patient = AppState.getPatients().get(healthCard);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.symptoms, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			String symptoms = ((EditText) findViewById(R.id.descriptions))
			.getText().toString();

			// Add the symptoms to the Patient object iff it's not blank
			if (!symptoms.isEmpty()) {
				if (symptoms.indexOf('=') < 1) {
					patient.addSymptoms(new Symptoms(symptoms));
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
