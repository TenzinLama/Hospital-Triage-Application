package classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;

public abstract class Record {

	private String time;

	/**
	 * Constructs a Record object with the current time.
	 *
	 * @param time
	 *            The time that the Record was recorded.
	 */
	@SuppressLint("SimpleDateFormat")
	public Record() {
		this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
				.getInstance().getTime());
	}

	/**
	 * Returns the time that this Record was recorded.
	 *
	 * @return The time that this Record was recorded.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time that this Record was recorded.
	 *
	 * @param time
	 *            The time that this Record was recorded.
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Returns this Record's string representation.
	 */
	@Override
	public String toString() {
		return String.format("%s", this.getTime());
	}

}
