package com.pdf.basic;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PdfRendererBasicFragment(), FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.intro_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
//                test();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void test() {
        // com.amigo.contacts.activities.ContactSelectionActivity
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
//        intent.setComponent(new ComponentName("com.android.contacts",
//                "com.android.contacts.activities.ContactSelectionActivity"));

        intent.setComponent(new ComponentName("com.android.contacts",
                "com.amigo.contacts.activities.ContactSelectionActivity"));

        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "124454545");
        intent.setType(Contacts.People.CONTENT_ITEM_TYPE);
        startActivity(intent);
    }

}
