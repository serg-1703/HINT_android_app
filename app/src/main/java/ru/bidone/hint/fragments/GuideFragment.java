package ru.bidone.hint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.bidone.hint.R;
import ru.bidone.hint.utils.MSArray;
import ru.bidone.hint.utils.dbWrapper;
import ru.bidone.hint.utils.m;

public class GuideFragment extends Fragment {
    View rootView;
    int subID = 0;
    String subName;
    dbWrapper db;

    public GuideFragment(dbWrapper db) { this.db = db; }

    public GuideFragment(int subID, String subName, dbWrapper db) {
        this.subID = subID;
        this.db = db;
        this.subName = subName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_guide, container, false);
            if (subID == 0) {
                rootView.findViewById(R.id.ivBack).setVisibility(View.GONE);
                ((TextView) rootView.findViewById(R.id.tvTitle)).setText(R.string.tab_guide);
            } else {
                rootView.findViewById(R.id.ivBack).setOnClickListener(v -> getFragmentManager().popBackStack());
                ((TextView) rootView.findViewById(R.id.tvTitle)).setText(subName);
            }
            drawList();
        }
        return rootView;
    }

    private void drawList() {
        ListView listView = rootView.findViewById(R.id.lv);
        ArrayList<MSArray> objs = subID == 0 ? db.getSubjects() : db.getArticles(subID);

        String[] sjTitle = new String[objs.size()];
        for (int i = 0; i < objs.size(); i++)
            sjTitle[i] = objs.get(i).get(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, sjTitle);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int currentID = Integer.parseInt(objs.get(position).get(0));
            String currentName = objs.get(position).get(1);
            Fragment fragment;

            if (subID == 0)
                fragment = new GuideFragment(currentID, currentName, db);
            else
                fragment = new ArticleFragment(currentID, currentName, db);

            m.transaction(getFragmentManager(), R.id.flGuide, fragment, "guide");
        });
    }
}
