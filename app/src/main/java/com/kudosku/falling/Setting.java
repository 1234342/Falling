package com.kudosku.falling;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Setting extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        addPreferencesFromResource(R.xml.setting);

        Preference credits = (Preference)findPreference("credits");

        credits.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("credits")) {
            Intent intent = new Intent(Setting.this, Credits.class);
            startActivity(intent);
        }
        return false;
    }

}
