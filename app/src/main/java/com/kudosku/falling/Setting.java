package com.kudosku.falling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class Setting extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        addPreferencesFromResource(R.xml.setting);

        Preference credits = (Preference)findPreference("credits");
        Preference donates = (Preference)findPreference("donates");

        credits.setOnPreferenceClickListener(this);
        donates.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("credits")) {
            Intent intent = new Intent(Setting.this, Credits.class);
            startActivity(intent);
        }
        if(preference.getKey().equals("donates")) {
            if(getResources().getConfiguration().locale.getLanguage() == "ko") {
                Intent intent = new Intent(Setting.this, Donates.class);
                startActivity(intent);
            } else {
                Toast.makeText(Setting.this,"Korean Only",Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
}
