package upworksolutions.themagictricks.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.MagicCategoryAdapter;
import upworksolutions.themagictricks.model.MagicCategory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements MagicCategoryAdapter.OnCategoryClickListener {
    private RecyclerView recyclerView;
    private MagicCategoryAdapter adapter;
    private List<MagicCategory> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        setupCategories();
        adapter = new MagicCategoryAdapter(categories, this);
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    private void setupCategories() {
        categories = new ArrayList<>();
        categories.add(new MagicCategory(1, "Card Magic", "Learn amazing card tricks", 
            Arrays.asList("Card tricks", "Sleight of hand", "Card flourishes")));
        categories.add(new MagicCategory(2, "Coin Magic", "Master coin manipulation", 
            Arrays.asList("Coin vanishes", "Transformations", "Coin matrix")));
        categories.add(new MagicCategory(3, "Mentalism", "Read minds and predict", 
            Arrays.asList("Mind reading", "Predictions", "Mental magic")));
        categories.add(new MagicCategory(4, "Illusions", "Create magical illusions", 
            Arrays.asList("Stage illusions", "Grand effects", "Large-scale magic")));
        categories.add(new MagicCategory(5, "Close-up Magic", "Intimate magic tricks", 
            Arrays.asList("Table magic", "Close-up effects", "Micro magic")));
        categories.add(new MagicCategory(6, "Rope Magic", "Rope manipulation tricks", 
            Arrays.asList("Rope cuts", "Restorations", "Rope routines")));
        categories.add(new MagicCategory(7, "Stage Magic", "Large-scale magic effects", 
            Arrays.asList("Stage performances", "Illusions", "Grand illusions")));
        categories.add(new MagicCategory(8, "Street Magic", "Street performance magic", 
            Arrays.asList("Street performances", "Impromptu magic", "Street illusions")));
    }

    @Override
    public void onCategoryClick(MagicCategory category) {
        Bundle args = new Bundle();
        args.putInt("category_id", category.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_categoryVideosFragment, args);
    }
} 