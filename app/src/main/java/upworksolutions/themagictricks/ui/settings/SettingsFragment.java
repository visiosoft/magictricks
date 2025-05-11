package upworksolutions.themagictricks.ui.settings;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private static final String PRIVACY_POLICY_URL = "https://mypaperlessoffice.org/mprivacy.html";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set up privacy policy click listener
        binding.privacyPolicyCard.setOnClickListener(v -> showPrivacyPolicyOptions());
        
        // Set up terms of service click listener
        binding.termsOfServiceCard.setOnClickListener(v -> openTermsOfService());
        
        // Set up about click listener
        binding.aboutCard.setOnClickListener(v -> openAbout());
    }

    private void showPrivacyPolicyOptions() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("View Privacy Policy")
            .setItems(new String[]{"View Online", "View Offline"}, (dialog, which) -> {
                if (which == 0) {
                    openPrivacyPolicyOnline();
                } else {
                    openPrivacyPolicyOffline();
                }
            })
            .show();
    }

    private void openPrivacyPolicyOnline() {
        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "No internet connection. Showing offline version.", Toast.LENGTH_SHORT).show();
            openPrivacyPolicyOffline();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Could not open browser. Showing offline version.", Toast.LENGTH_SHORT).show();
            openPrivacyPolicyOffline();
        }
    }

    private void openPrivacyPolicyOffline() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_settingsFragment_to_privacyPolicyFragment);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(requireContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void openTermsOfService() {
        // TODO: Implement terms of service
    }

    private void openAbout() {
        // TODO: Implement about section
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 