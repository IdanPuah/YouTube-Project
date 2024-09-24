package com.example.youtube_android.ui.comments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Comment;

public class EditCommentDialogFragment extends DialogFragment {
    public interface EditCommentListener {
        void onCommentEdited(Comment comment);
    }

    private Comment comment;
    private EditCommentListener listener;

    public EditCommentDialogFragment(Comment comment, EditCommentListener listener) {
        this.comment = comment;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_comment, null);

        EditText editCommentText = view.findViewById(R.id.editCommentText);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Set the current comment text
        editCommentText.setText(comment.getText());

        saveButton.setOnClickListener(v -> {
            String newCommentText = editCommentText.getText().toString();
            comment.setText(newCommentText); // Update the comment text
            if (listener != null) {
                listener.onCommentEdited(comment);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        return dialog;
    }
}
