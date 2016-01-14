package activities;

import global.AppState;
import me.echeung.triage207.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import classes.Nurse;
import classes.Physician;

public class LoginActivity extends Activity {

	private TextView mLoginMessage;
	private EditText mUsername;
	private EditText mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mLoginMessage = (TextView) findViewById(R.id.login_message);
		mUsername = (EditText) findViewById(R.id.login_username);
		mPassword = (EditText) findViewById(R.id.login_password);

		// Handles the soft keyboard Login button
		mPassword
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				boolean handled = false;
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					logIn(null);
					handled = true;
				}
				return handled;
			}
		});
	}

	/** Prevents users from going back to the main activity if not logged in */
	@Override
	public void onBackPressed() {
	}

	/**
	 * Handles the "Log in" button in the login form.
	 *
	 * @param view
	 *            The "Log in" button.
	 */
	public void logIn(View view) {
		// Get the values from the form
		final String username = mUsername.getText().toString();
		final String password = mPassword.getText().toString();

		if (username.isEmpty() || password.isEmpty()) {
			mLoginMessage.setText(R.string.empty_fields);
		} else {
			// Check that the username is valid
			if (AppState.hasNurse(username) || AppState.hasPhysician(username)) {
				// Sign in the Nurse or Physician if the password is correct
				if (AppState.hasNurse(username)) {
					Nurse nurse = AppState.getNurses().get(username);

					if (nurse.getPassword().equals(password)) {
						AppState.setLoggedIn(true);

						// Set the current user text in the main activity
						AppState.setCurrentUser(AppState.getNurses().get(
								username));

						this.finish();
					} else {
						mLoginMessage.setText(R.string.login_error_pass);
					}
				} else if (AppState.hasPhysician(username)) {
					Physician physician = AppState.getPhysicians()
							.get(username);

					if (physician.getPassword().equals(password)) {
						AppState.setLoggedIn(true);

						// Set the current user text in the main activity
						AppState.setCurrentUser(AppState.getPhysicians().get(
								username));

						this.finish();
					} else {
						mLoginMessage.setText(R.string.login_error_pass);
					}
				}
			} else {
				mLoginMessage.setText(R.string.login_error_user);
			}
		}
	}

	public void register(View view) {
		Intent registerIntent = new Intent(this, RegisterActivity.class);
		registerIntent.putExtra("USERNAME", mUsername.getText().toString());
		registerIntent.putExtra("PASSWORD", mPassword.getText().toString());
		startActivity(registerIntent);
	}
}
