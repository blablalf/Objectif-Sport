package beetlejuice.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import beetlejuice.objectifsport.R;
import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.adapters.SportAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The type My sports fragment.
 */
public class MySportsFragment extends Fragment {

    private SportAdapter sportAdapter;

    /**
     * New instance my sports fragment.
     *
     * @return the my sports fragment
     */
    public static MySportsFragment newInstance() {
        return new MySportsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate
        View view = inflater.inflate(R.layout.my_sports_fragment, container, false);

        // create adapter with the model and context
        sportAdapter = new SportAdapter(view.getContext(), DataManager.getSports());
        ListView listView = view.findViewById(R.id.sports_list);
        listView.setAdapter(sportAdapter);

        // get FloatingActionButton and populate it
        FloatingActionButton addSportButton = view.findViewById(R.id.add_sport);
        addSportButton.setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            AddSportDialogFragment addSportDialogFragment = AddSportDialogFragment.newInstance();
            assert fm != null;
            addSportDialogFragment.show(fm, "fragment_add_sport");
        });

        return view;
    }

    /**
     * Gets sport adapter.
     *
     * @return the sport adapter
     */
// useful to notify
    public SportAdapter getSportAdapter() {
        return sportAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        sportAdapter.notifyDataSetChanged();
    }
}