package ru.bidone.hint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.bidone.hint.fragments.CalcFragment;
import ru.bidone.hint.fragments.GuideFragment;
import ru.bidone.hint.utils.dbWrapper;
import ru.bidone.hint.utils.m;

public class MainActivity extends AppCompatActivity {

    public dbWrapper db;
    private Fragment guideFragment, calcFragment;
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dbWrapper(this);

        db.copyDB();
        db.openDB();

        View.OnClickListener onTabClickListener = v -> onChangeTab((int) v.getTag());

        View tabGuide = findViewById(R.id.tabGuide);
        tabGuide.setTag(0);
        tabGuide.setOnClickListener(onTabClickListener);

        View tabCalc = findViewById(R.id.tabCalc);
        tabCalc.setTag(1);
        tabCalc.setOnClickListener(onTabClickListener);

        tabGuide.performClick();
    }

    private void onChangeTab(int tabID) {
        Fragment fr;
        currentTab = tabID;
        switch (tabID) {
            case 1:
                if (calcFragment == null)
                    calcFragment = new CalcFragment();
                fr = calcFragment;
                break;
            default:
                if (guideFragment == null)
                    guideFragment = new GuideFragment(db);
                fr = guideFragment;
                break;
        }

        m.transaction(getSupportFragmentManager(), R.id.flMain, fr);

        ArrayList<tabItem> tabItems = new ArrayList<>();
        tabItems.add(new tabItem(findViewById(R.id.ivTabGuide), findViewById(R.id.tvTabGuide)));
        tabItems.add(new tabItem(findViewById(R.id.ivTabCalc), findViewById(R.id.tvTabCalc)));

        for (tabItem item : tabItems) {
            item.tv.setTextColor(getColor(R.color.tab_main_item));
            item.iv.setColorFilter(getColor(R.color.tab_main_item));
        }

        tabItem item = tabItems.get(tabID);
        item.tv.setTextColor(getColor(R.color.colorPrimary));
        item.iv.setColorFilter(getColor(R.color.colorPrimary));
    }

    private static class tabItem {
        public ImageView iv;
        public TextView tv;

        tabItem(ImageView iv, TextView tv) {
            this.iv = iv;
            this.tv = tv;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onChangeTab(currentTab);
    }
}