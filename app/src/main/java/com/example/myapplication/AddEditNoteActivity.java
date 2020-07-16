package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.myapplication.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.myapplication.EXTRA_TITLE";
    public static final String EXTRA_BODY =
            "com.example.myapplication.EXTRA_BODY";
    public static final String EXTRA_USER_ID =
            "com.example.myapplication.EXTRA_USER_ID";
    private EditText editTextTitle;
    private EditText editTextBody;
    private EditText editTextUserId;
    private EditText editTextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextBody = findViewById(R.id.edit_text_body);
        editTextUserId = findViewById(R.id.edit_text_user_id);
        editTextId = findViewById(R.id.edit_text_id);


        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextBody.setText(intent.getStringExtra(EXTRA_BODY));
            editTextUserId.setText(Integer.toString(intent.getIntExtra(EXTRA_USER_ID, 1)));
            editTextId.setText(Integer.toString(intent.getIntExtra(EXTRA_ID, 1)));
        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String body = editTextBody.getText().toString();
        String userId = editTextUserId.getText().toString();
        String id = editTextId.getText().toString();

        if (title.trim().isEmpty() || body.trim().isEmpty() || userId.trim().isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Please insert title, body, userId, id", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_BODY, body);
        data.putExtra(EXTRA_USER_ID, userId);
        data.putExtra(EXTRA_ID, id);


        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
