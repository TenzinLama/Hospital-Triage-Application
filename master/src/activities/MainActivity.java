package activities;

import global.AppState;

import java.io.IOException;

import me.echeung.triage207.R;
import adapters.PatientsListAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import classes.Physician;
import classes.User;

public class MainActivity extends Activity {

	private PatientsListAdapter adapter;

	private TextView mCurrentUserType;
	private TextView mCurrentUser;
	private Spinner mSort;
	private ListView mPatientsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCurrentUserType = (TextView) findViewById(R.id.logged_in_as);
		mCurrentUser = (TextView) findViewById(R.id.user);
		mSort = (Spinner) findViewById(R.id.sort);
		mPatientsList = (ListView) findViewById(R.id.patients_list);

		mSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				AppState.setPatientsListSort(position);
				updateAdapter();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		updateUserText();
		updateAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (AppState.getCurrentUser() instanceof Physician) {
			menu.findItem(R.id.action_new).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			return true;
		case R.id.action_new:
			startActivityForResult(new Intent(this, NewPatientActivity.class),
					0);
			return true;
		case R.id.action_save:
			try {
				AppState.savePatients();
				Toast.makeText(getApplicationContext(),
						getString(R.string.patients_saved), Toast.LENGTH_SHORT)
						.show();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.file_error), Toast.LENGTH_SHORT)
						.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			updateAdapter();

			try {
				AppState.savePatients();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.file_error), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		invalidateOptionsMenu();

		updateUserText();
		updateAdapter();

		try {
			AppState.savePatients();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.file_error), Toast.LENGTH_SHORT).show();
		}
	}

	public void updateAdapter() {
		if (adapter == null) {
			// Initialize adapter for the list of patients
			adapter = new PatientsListAdapter(this, AppState.getPatientsList());
			mPatientsList.setAdapter(adapter);

			mPatientsList.setEmptyView(findViewById(R.id.empty_list));

			mPatientsList.setClickable(true);
			mPatientsList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent patientActivity = new Intent(
									getBaseContext(), PatientActivity.class);
							patientActivity.putExtra("PATIENT", AppState
									.getPatientsList().get(position)
									.getHealthCard());
							startActivity(patientActivity);
						}

					});
		} else {
			// Update with new list
			adapter.updatePatients(AppState.getPatientsList());
		}
	}

	public void updateUserText() {
		User<?> currentUser = AppState.getCurrentUser();

		if (currentUser != null) {
			mCurrentUserType.setText(String.format(
					"%s ",
					currentUser instanceof Physician ? this
							.getString(R.string.physician) : this
							.getString(R.string.nurse)));

			mCurrentUser.setText(currentUser.getUsername());
		}
	}

	public void signOut(View view) {
		AppState.setLoggedIn(false);
		AppState.setCurrentUser(null);
		startActivity(new Intent(this, LoginActivity.class));
	}

	public void addPatient(View view) {
		startActivity(new Intent(this, NewPatientActivity.class));
	}
}
