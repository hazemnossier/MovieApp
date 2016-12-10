package com.example.muhammad.movieapp.Utilities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Muhammad on 27/11/2016.
 */

public class Settings extends PreferenceActivity  implements Preference.OnPreferenceChangeListener{
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {


            ListPreference mlistPreference = (ListPreference) preference;
            int mprefIndex = mlistPreference.findIndexOfValue(stringValue);
            if (mprefIndex >= 0) {
                preference.setSummary(mlistPreference.getEntries()[mprefIndex]);
            }
        } else {

            preference.setSummary(stringValue);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_type_list_key)));*/

    }

    private void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

}
