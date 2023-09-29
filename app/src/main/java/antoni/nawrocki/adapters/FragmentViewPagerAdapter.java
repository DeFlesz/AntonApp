package antoni.nawrocki.adapters;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Adapter for handling switching between the pages
 */
public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragmentCollection = new ArrayList<>();

    /**
     * Constructor for the page switcher
     * @param fragment default fragment
     * @param values ArrayList of fragments between which switching will take place
     */
    public FragmentViewPagerAdapter(@NonNull Fragment fragment, ArrayList<Fragment> values) {
        super(fragment);
        this.fragmentCollection = values;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragmentCollection.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragmentCollection.size();
    }
}
