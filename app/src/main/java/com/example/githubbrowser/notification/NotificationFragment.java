package com.example.githubbrowser.notification;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

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

    private Button button1;
    private Button button2;
    private Button resetFiltersButton;
    private TextView textView1, textView2, textView3, textView4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchNotifications(); // Fetch notifications on fragment creation
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        button1 = view.findViewById(R.id.button_inbox_notification);
        button2 = view.findViewById(R.id.button_Repository_notification);
        resetFiltersButton = view.findViewById(R.id.btnResetFilters);

        textView1 = view.findViewById(R.id.text_notification_after_image);
        textView2 = view.findViewById(R.id.text_long_notification);
        textView3 = view.findViewById(R.id.text_unread_on);
        textView4 = view.findViewById(R.id.text_unread_on_after_image);

        // Reset Filters Button action
        resetFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllFilters();
                Toast.makeText(getActivity(), "Filters reset", Toast.LENGTH_SHORT).show(); // Toast added
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheet();
                Toast.makeText(getActivity(), "Inbox button clicked", Toast.LENGTH_SHORT).show(); // Toast added
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheet2();
                Toast.makeText(getActivity(), "Repository button clicked", Toast.LENGTH_SHORT).show(); // Toast added
            }
        });

        ToggleButton toggleButton = view.findViewById(R.id.button_Unread_notification);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()) {
                    textView1.setVisibility(View.GONE);
                    textView2.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.VISIBLE);
                    resetFiltersButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Unread notifications enabled", Toast.LENGTH_SHORT).show(); // Toast added
                } else {
                    textView4.setVisibility(View.GONE);
                    textView3.setVisibility(View.GONE);
                    textView2.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    resetFiltersButton.setVisibility(View.GONE);
                    resetAllFilters();
                    Toast.makeText(getActivity(), "Unread notifications disabled", Toast.LENGTH_SHORT).show(); // Toast added
                }
            }
        });

        return view;
    }

    public void updateButtonText(String newText) {
        button1.setText(newText);
    }

    private void clickOpenBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        RadioGroup radioGroup = bottomSheetView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = bottomSheetView.findViewById(checkedId);
                String selectedText = radioButton.getText().toString();
                updateButtonText(selectedText);
                Toast.makeText(getActivity(), "Selected: " + selectedText, Toast.LENGTH_SHORT).show(); // Toast added
            }
        });
        Button backButton = bottomSheetView.findViewById(R.id.backButton_bottom_sheet);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void clickOpenBottomSheet2() {
        View bottomSheetView2 = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_2, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView2);
        bottomSheetDialog.show();
        Button backButton = bottomSheetView2.findViewById(R.id.backButton_bottom_sheet2);
        Button searchButton = bottomSheetView2.findViewById(R.id.button_search_notification);

        TextView textView1 = bottomSheetView2.findViewById(R.id.title_repoditory_notification);
        TextView textView2 = bottomSheetView2.findViewById(R.id.edit_text_repository_notification);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Searching notifications...", Toast.LENGTH_SHORT).show(); // Toast added
            }
        });
    }

    private void resetAllFilters() {
        // Ensure the view is not null before accessing its elements
        if (getView() != null) {
            TextView textViewAllCaughtUp = getView().findViewById(R.id.text_notification_after_image);
            TextView textViewTakeABreak = getView().findViewById(R.id.text_long_notification);
            TextView textViewNoNotifications = getView().findViewById(R.id.text_unread_on_after_image);
            TextView textViewUseFewerFilters = getView().findViewById(R.id.text_unread_on);

            // Reset all views to the original state
            textViewAllCaughtUp.setVisibility(View.VISIBLE);
            textViewTakeABreak.setVisibility(View.VISIBLE);
            textViewNoNotifications.setVisibility(View.GONE);
            textViewUseFewerFilters.setVisibility(View.GONE);

            // Hide the reset filters button again
            resetFiltersButton.setVisibility(View.GONE);

            // Reset the button text back to "Inbox"
            updateButtonText("Inbox");
        }
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
                        textView1.setText(notifications.get(0).getReason());
                        // Update UI with notification details
                        for (Notification notification : notifications) {
                            Log.d("NotificationFragment", "Notification ID: " + notification.getId());
                        }
                        // Show success notification
                        Toast.makeText(getActivity(), "Notifications fetched successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        textView1.setText("No notifications available.");
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
