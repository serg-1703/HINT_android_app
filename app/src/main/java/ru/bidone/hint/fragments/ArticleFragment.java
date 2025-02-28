package ru.bidone.hint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import ru.bidone.hint.R;
import ru.bidone.hint.utils.dbWrapper;

public class ArticleFragment extends Fragment {

    View rootView;
    int artID;
    String artName;
    dbWrapper db;

    public ArticleFragment(int artID, String artName, dbWrapper db) {
        this.artID = artID;
        this.db = db;
        this.artName = artName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_article, container, false);
            rootView.findViewById(R.id.ivBack).setOnClickListener(v -> getFragmentManager().popBackStack());
            ((TextView) rootView.findViewById(R.id.tvTitle)).setText(artName);
            drawArt();
        }
        return rootView;
    }
    private void drawArt() {
        WebView wv = rootView.findViewById(R.id.wvArt);
        String artText = db.getArticle(artID);
//        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadDataWithBaseURL("file:///android_asset/", artText, "text/html; charset=utf-8", "UTF-8", null);
    }
}
