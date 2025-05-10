package upworksolutions.themagictricks;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import upworksolutions.themagictricks.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageButton profileButton;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialize profile button
        profileButton = findViewById(R.id.profile_button);

        // Set up profile button
        profileButton.setOnClickListener(v -> {
            // Handle profile button click
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });

        // Set up Navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_videos, R.id.navigation_notifications)
                .build();
        
        // Set up the bottom navigation
        NavigationUI.setupWithNavController(binding.navView, navController);
        
        // Set up the toolbar with navigation
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }
}