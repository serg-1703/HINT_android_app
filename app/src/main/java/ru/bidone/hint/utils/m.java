package ru.bidone.hint.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class m {
    public static void transaction(FragmentManager fm, int viewID, Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.commit();
    }

    public static void transaction(FragmentManager fm, int viewID, Fragment fragment, String stackName) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(viewID, fragment);
        transaction.addToBackStack(stackName);
        transaction.commit();
    }
}
