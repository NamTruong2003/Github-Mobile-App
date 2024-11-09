package com.example.githubbrowser.notification;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.githubbrowser.Constant;
import com.example.githubbrowser.Handler;
import com.example.githubbrowser.R;
import com.example.githubbrowser.Trending;
import com.example.githubbrowser.api.GitHubApiService;
import com.example.githubbrowser.api.RetrofitClient;
import com.example.githubbrowser.UserInformation;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    private static final String url = "https://api.github.com/notifications?all=true";
    private LinearLayout holdContentLayout;
    private ProgressDialog progressDialog;
    private static String GITHUB_TOKEN ;
    private Button inboxButton;
    private Button repositoryButton;
    private Button resetFiltersButton;
    private TextView notificationTextView, longNotificationTextView, unreadOnTextView, unreadOnAfterImageTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        GITHUB_TOKEN = UserInformation.getAccessToken();
        holdContentLayout = view.findViewById(R.id.content_view);
        inboxButton = view.findViewById(R.id.button_inbox_notification);
        repositoryButton = view.findViewById(R.id.button_Repository_notification);
//        resetFiltersButton = view.findViewById(R.id.btnResetFilters);
//
//        notificationTextView = view.findViewById(R.id.text_notification_after_image);
//        longNotificationTextView = view.findViewById(R.id.text_long_notification);
//        unreadOnTextView = view.findViewById(R.id.text_unread_on);
//        unreadOnAfterImageTextView = view.findViewById(R.id.text_unread_on_after_image);

        // Set up button actions
//        resetFiltersButton.setOnClickListener(v -> {
//            resetAllFilters();
//            Toast.makeText(getActivity(), "Filters reset", Toast.LENGTH_SHORT).show();
//        });
//
//        inboxButton.setOnClickListener(v -> openInboxBottomSheet());
//        repositoryButton.setOnClickListener(v -> openRepositoryBottomSheet());
//
//        ToggleButton unreadToggleButton = view.findViewById(R.id.button_Unread_notification);
//        unreadToggleButton.setOnClickListener(v -> toggleUnreadNotifications(unreadToggleButton.isChecked()));
        new updateContent().execute();
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


    public class updateContent extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String contentString = null;
            String contributorString =null;
            try {
                contentString = handler.httpServiceCall(url,GITHUB_TOKEN);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (contentString != null){
                try {
                    JSONArray jsonArray = new JSONArray(contentString);

                    for (int i =0;i< jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String unread = jsonObject1.getString("unread");
                        JSONObject subject =jsonObject1.getJSONObject("subject");

                        String title = subject.getString("title");
                        JSONObject repo = jsonObject1.getJSONObject("repository");
                        String full_name = repo.getString("full_name");
                        String des = repo.getString("description");

                        Log.i("test",unread+title+full_name+des);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addViewContent(unread,title,full_name,des);
                            }
                        });




                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);

                }


            }
            return null;
        }
    }
    public void addViewContent(String unread,String title, String full_name,String des ){
        View viewContent = LayoutInflater.from(requireContext()).inflate(R.layout.notification_card,null);
        LinearLayout Unread = viewContent.findViewById(R.id.unread_notification);
        TextView textViewName = viewContent.findViewById(R.id.repository_fullname);

        TextView textViewTitle = viewContent.findViewById(R.id.notification_title);
        if(des.equals("null")){
            des = "No Description provided";
        }

        TextView textViewDes = viewContent.findViewById(R.id.description_notification);
//        if(unread.equals("true")){
//            Unread.setBackgroundColor(Color.parseColor("ECF9FF"));
//        }
//        else{
//            Unread.setBackgroundColor(Color.parseColor("FFFFFF"));
//        }
        textViewName.setText(full_name);
        textViewTitle.setText(title);
        textViewDes.setText(des);

        holdContentLayout.addView(viewContent);

    }

}

