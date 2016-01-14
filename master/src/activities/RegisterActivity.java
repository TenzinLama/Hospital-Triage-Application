package activities;

import global.AppState;

import java.io.IOException;

import me.echeung.triage207.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import classes.Nurse;
import classes.Physician;

public class RegisterActivity extends Activity {

	private TextView mRegisterMessage;
	private EditText mUsername;
	private EditText mPassword;
	private EditText mPasswordConfirm;
	private RadioButton mRadioNurse;
	private RadioButton mRadioPhysician;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mRegisterMessage = (TextView) findViewById(R.id.register_message);
		mUsername = (EditText) findViewById(R.id.register_username);
		mPassword = (EditText) findViewById(R.id.register_password);
		mPasswordConfirm = (EditText) findViewById(R.id.register_password_confirm);
		mRadioNurse = (RadioButton) findViewById(R.id.radio_nurse);
		mRadioPhysician = (RadioButton) findViewById(R.id.radio_physician);

		mUsername.setText(getIntent().getStringExtra("USERNAME"));
		mPassword.setText(getIntent().getStringExtra("PASSWORD"));

		// Handles the soft keyboard Register button
		mPasswordConfirm
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				boolean handled = false;
				if (id == R.id.register || id == EditorInfo.IME_NULL) {
					register(null);
					handled = true;
				}
				return handled;
			}
		});
	}

	public void register(View view) {
		final String username = mUsername.getText().toString();
		final String password = mPassword.getText().toString();
		final String passwordConfirm = mPasswordConfirm.getText().toString();
		final boolean isNurse = mRadioNurse.isChecked();
		final boolean isPhysician = mRadioPhysician.isChecked();

		if (username.isEmpty() || password.isEmpty()
				|| passwordConfirm.isEmpty()) {
			mRegisterMessage.setText(R.string.empty_fields);
		} else {
			if (!password.equals(passwordConfirm)) {
				mRegisterMessage.setText(R.string.register_password_mismatch);
			} else {
				if (isNurse) {
					AppState.addNurse(new Nurse(username, password));

					try {
						AppState.saveNurses();
					} catch (IOException e) {
						Toast.makeText(getApplicationContext(),
								R.string.file_error, Toast.LENGTH_SHORT).show();
					}

					this.finish();
				} else if (isPhysician) {
					AppState.addPhysician(new Physician(username, password));

					try {
						AppState.savePhysicians();
					} catch (IOException e) {
						Toast.makeText(getApplicationContext(),
								R.string.file_error, Toast.LENGTH_SHORT).show();
					}
					this.finish();
				} else {
					mRegisterMessage.setText(R.string.register_choose_type);
				}
			}
		}
	}

	public void closeDialog(View view) {
		this.finish();
	}
}
