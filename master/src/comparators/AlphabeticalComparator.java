package comparators;

import java.util.Comparator;

import classes.Patient;

public class AlphabeticalComparator implements Comparator<Patient> {

	@Override
	public int compare(Patient lhs, Patient rhs) {
		return lhs.getName().compareToIgnoreCase(rhs.getName());
	}

}
