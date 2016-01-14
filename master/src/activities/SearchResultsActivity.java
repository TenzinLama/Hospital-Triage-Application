package activities;

import global.AppState;
import me.echeung.triage207.R;
import adapters.PatientsListAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchResultsActivity extends Activity {

	private PatientsListAdapter adapter;
	private ListView mResulsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);

		mResulsList = (ListView) findViewById(R.id.search_results_list);

		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			updateAdapter(query);
		}
	}

	public void updateAdapter(final String query) {
		if (adapter == null) {
			// Initialize adapter for the list of patients
			adapter = new PatientsListAdapter(this,
					AppState.getPatientsList(query));
			mResulsList.setAdapter(adapter);

			mResulsList.setEmptyView(findViewById(R.id.empty_results_list));

			mResulsList.setClickable(true);
			mResulsList
			.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					Intent patientActivity = new Intent(
							getBaseContext(), PatientActivity.class);
					patientActivity.putExtra("PATIENT", AppState
							.getPatientsList(query).get(position)
							.getHealthCard());
					startActivity(patientActivity);
				}

			});
		} else {
			// Update with new list
			adapter.updatePatients(AppState.getPatientsList(query));
		}
	}
}
