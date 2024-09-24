package com.example.youtube_android.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.youtube_android.R;

public class PopupWindowHelper {
    public static void showLogoutPopup(Context context, View anchorView, LogoutCallback callback) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.logout_popup, null);

        // Initialize popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        // Set background and focusable to true
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);

        // Set animation if needed
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        // Set click listener for logout button
        Button btnLogout = popupView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            LoggedManager loggedManager = LoggedManager.getInstance();

            // Invoke the callback

            callback.onLogout();


            loggedManager.setLogged(false);

            popupWindow.dismiss(); // Dismiss the popup after action
        });


        // Show the popup at the bottom of the anchor view
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }
}
