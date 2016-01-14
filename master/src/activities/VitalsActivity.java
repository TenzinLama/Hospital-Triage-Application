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
import classes.Vitals;

public class VitalsActivity extends Activity {

	private Patient patient;

	private EditText mTemp;
	private EditText mSys;
	private EditText mDia;
	private EditText mHeart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vitals);

		String healthCard = getIntent().getStringExtra("PATIENT");
		patient = AppState.getPatients().get(healthCard);

		mTemp = (EditText) findViewById(R.id.temperature);
		mSys = (EditText) findViewById(R.id.systolic);
		mDia = (EditText) findViewById(R.id.diastolic);
		mHeart = (EditText) findViewById(R.id.heart_rate);

		// Display previous vitals, if they exist
		if (!patient.getVitals().isEmpty()) {
			Vitals prevVitals = patient.getVitals().get(0);
			mTemp.append(String.valueOf(prevVitals.getTemperature()));
			mSys.append(String.valueOf(prevVitals.getSystolicBP()));
			mDia.append(String.valueOf(prevVitals.getDiastolicBP()));
			mHeart.append(String.valueOf(prevVitals.getHeartRate()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.vitals, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			addVitals();
			return true;
		case R.id.action_discard:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Adds the Vitals to the Patient's records. */
	private void addVitals() {
		if (mTemp.getText().toString().isEmpty()
				|| mSys.getText().toString().isEmpty()
				|| mDia.getText().toString().isEmpty()
				|| mHeart.getText().toString().isEmpty()) {
			Toast.makeText(this, getString(R.string.empty_fields),
					Toast.LENGTH_SHORT).show();
		} else {
			final Double newTemp = Double.parseDouble(mTemp.getText()
					.toString());
			final Double newSys = Double.parseDouble(mSys.getText().toString());
			final Double newDia = Double.parseDouble(mDia.getText().toString());
			final Double newHeart = Double.parseDouble(mHeart.getText()
					.toString());

			patient.addVitals(new Vitals(newTemp, newSys, newDia, newHeart));
			this.finish();
		}
	}
}
