package com.example.githubbrowser.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.githubbrowser.R;
import com.example.githubbrowser.api.GitHubApiService;
import com.example.githubbrowser.api.RetrofitClient;
import com.example.githubbrowser.UserInformation;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    private static final String BASE_URL = "https://api.github.com/";

    private Button inboxButton;
    private Button repositoryButton;
    private Button resetFiltersButton;
    private TextView notificationTextView, longNotificationTextView, unreadOnTextView, unreadOnAfterImageTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchNotifications(); // Fetch notifications on fragment creation
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        inboxButton = view.findViewById(R.id.button_inbox_notification);
        repositoryButton = view.findViewById(R.id.button_Repository_notification);
        resetFiltersButton = view.findViewById(R.id.btnResetFilters);

        notificationTextView = view.findViewById(R.id.text_notification_after_image);
        longNotificationTextView = view.findViewById(R.id.text_long_notification);
        unreadOnTextView = view.findViewById(R.id.text_unread_on);
        unreadOnAfterImageTextView = view.findViewById(R.id.text_unread_on_after_image);

        // Set up button actions
        resetFiltersButton.setOnClickListener(v -> {
            resetAllFilters();
            Toast.makeText(getActivity(), "Filters reset", Toast.LENGTH_SHORT).show();
        });

        inboxButton.setOnClickListener(v -> openInboxBottomSheet());
        repositoryButton.setOnClickListener(v -> openRepositoryBottomSheet());

        ToggleButton unreadToggleButton = view.findViewById(R.id.button_Unread_notification);
        unreadToggleButton.setOnClickListener(v -> toggleUnreadNotifications(unreadToggleButton.isChecked()));

        return view;
    }

    private void updateButtonText(String newText) {
        inboxButton.setText(newText);
    }

    private void openInboxBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        RadioGroup radioGroup = bottomSheetView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = bottomSheetView.findViewById(checkedId);
            updateButtonText(radioButton.getText().toString());
            Toast.makeText(getActivity(), "Selected: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
        });

        bottomSheetView.findViewById(R.id.backButton_bottom_sheet).setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void openRepositoryBottomSheet() {
        View bottomSheetView2 = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_2, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView2);
        bottomSheetDialog.show();

        bottomSheetView2.findViewById(R.id.backButton_bottom_sheet2).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetView2.findViewById(R.id.button_search_notification).setOnClickListener(v -> {
            TextView titleTextView = bottomSheetView2.findViewById(R.id.title_repoditory_notification);
            TextView editText = bottomSheetView2.findViewById(R.id.edit_text_repository_notification);
            titleTextView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Searching notifications...", Toast.LENGTH_SHORT).show();
        });
    }

    private void toggleUnreadNotifications(boolean isChecked) {
        if (isChecked) {
            notificationTextView.setVisibility(View.GONE);
            longNotificationTextView.setVisibility(View.GONE);
            unreadOnTextView.setVisibility(View.VISIBLE);
            unreadOnAfterImageTextView.setVisibility(View.VISIBLE);
            resetFiltersButton.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Unread notifications enabled", Toast.LENGTH_SHORT).show();
        } else {
            unreadOnAfterImageTextView.setVisibility(View.GONE);
            unreadOnTextView.setVisibility(View.GONE);
            longNotificationTextView.setVisibility(View.VISIBLE);
            notificationTextView.setVisibility(View.VISIBLE);
            resetFiltersButton.setVisibility(View.GONE);
            resetAllFilters();
            Toast.makeText(getActivity(), "Unread notifications disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetAllFilters() {
        notificationTextView.setVisibility(View.VISIBLE);
        longNotificationTextView.setVisibility(View.VISIBLE);
        unreadOnAfterImageTextView.setVisibility(View.GONE);
        unreadOnTextView.setVisibility(View.GONE);
        resetFiltersButton.setVisibility(View.GONE);
        updateButtonText("Inbox");
    }

    private void fetchNotifications() {
        String token = UserInformation.getAccessToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        GitHubApiService apiService = RetrofitClient.getClient(BASE_URL).create(GitHubApiService.class);
        Call<List<Notification>> call = apiService.getNotifications("token " + token);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notifications = response.body();
                    if (!notifications.isEmpty()) {
                        notificationTextView.setText(notifications.get(0).getReason());
                        Log.d("NotificationFragment", "Notifications fetched successfully");
                    } else {
                        notificationTextView.setText("No notifications available.");
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
