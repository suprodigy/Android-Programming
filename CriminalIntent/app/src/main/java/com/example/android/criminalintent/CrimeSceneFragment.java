package com.example.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by jr on 2017-02-03.
 */

public class CrimeSceneFragment extends DialogFragment {

    private static final String ARG_PATH = "path";

    private ImageView mImageView;

    public static CrimeSceneFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);

        CrimeSceneFragment fragment = new CrimeSceneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path = getArguments().getString(ARG_PATH);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_photo, null);

        mImageView = (ImageView) view.findViewById(R.id.crime_photo);

        if (path == null) {
            mImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(path, getActivity());
            mImageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.crime_photo_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
