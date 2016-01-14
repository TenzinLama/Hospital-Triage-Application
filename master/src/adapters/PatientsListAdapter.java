package adapters;

import java.util.List;

import me.echeung.triage207.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import classes.Patient;

public class PatientsListAdapter extends ArrayAdapter<Patient> {

	static class ViewHolder {
		TextView mName;
		TextView mHealthCard;
		TextView mInfo;
		int position;
	}

	public PatientsListAdapter(Context context, List<Patient> objects) {
		super(context, R.layout.patient_list_item, objects);
	}

	public void updatePatients(List<Patient> patients) {
		this.clear();
		for (Patient patient : patients) {
			this.insert(patient, this.getCount());
		}
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;

		if (view == null) {
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			view = inflater.inflate(R.layout.patient_list_item, parent, false);

			holder = new ViewHolder();
			holder.mName = (TextView) view.findViewById(R.id.list_item_name);
			holder.mHealthCard = (TextView) view
					.findViewById(R.id.list_item_healthcard);
			holder.mInfo = (TextView) view.findViewById(R.id.list_item_info);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Patient patient = this.getItem(position);

		if (patient != null) {

			// Show patient name and info
			holder.mName.setText(patient.getName());
			holder.mHealthCard.setText(patient.getHealthCard());
			holder.mInfo.setText(String.format("%s | Urgency: %s",
					patient.getIsImproving() ? "Improving" : "Not improving",
							patient.getUrgency()));
		}

		return view;
	}
}
