package com.codepath.simpletodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity implements EditDateDialogFragment.EditDialogListener {
    private int priority;

    private static int[] PRIORITY_COLORS = new int[]{
            R.color.colorPriorityHigh,
            R.color.colorPriorityMid,
            R.color.colorPriorityLow,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        setTitle("Edit Item");

        // Title
        String title = getIntent().getStringExtra("title");
        EditText titleEditText = (EditText) findViewById(R.id.edit_text_edit_item_title);
        titleEditText.setText(title);

        // Body
        String body = getIntent().getStringExtra("body");
        EditText bodyEditText = (EditText) findViewById(R.id.edit_text_edit_item_body);
        bodyEditText.setText(body);

        // Priority
        priority = getIntent().getIntExtra("priority", 0);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_edit_priority);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(priority-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getApplicationContext(), PRIORITY_COLORS[position]));
//                ((TextView)parent.getChildAt(0)).setTextSize(getResources().getDimension(R.dimen.edit_activity_priority_text_size));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //  Date
        String date = getIntent().getStringExtra("date");
        TextView dateTextView = (TextView) findViewById(R.id.text_view_dialog_edit_date);
        dateTextView.setText(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.button_esc) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(String date) {
        TextView tvDate = (TextView) findViewById(R.id.text_view_dialog_edit_date);
        tvDate.setText(date);
    }

    public void onSave(View v) {
        EditText etTitle = (EditText) findViewById(R.id.edit_text_edit_item_title);
        EditText etBody = (EditText) findViewById(R.id.edit_text_edit_item_body);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_edit_priority);
        TextView tvDate = (TextView) findViewById(R.id.text_view_dialog_edit_date);
        if (!"".equals(etTitle.getText().toString())) {
            Intent data = new Intent();
            data.putExtra("itemTitle", etTitle.getText().toString());
            data.putExtra("itemBody", etBody.getText().toString());
            data.putExtra("itemPriority", spinner.getSelectedItemPosition()+1);
            data.putExtra("itemDate", tvDate.getText().toString());
            int pos = getIntent().getIntExtra("pos", 0);
            data.putExtra("pos", pos);
            setResult(RESULT_OK, data);
            finish();
        }
        else {
            Context context = getApplicationContext();
            String text =  getResources().getString(R.string.message_valid_name);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            // Reset title
            String title = getIntent().getStringExtra("title");
            etTitle.setText(title);

            // Reset body
            String body = getIntent().getStringExtra("body");
            etBody.setText(body);
        }
    }

    public void onDelete(View v) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.title_delete_confirm))
                .setMessage(getResources().getString(R.string.message_delete_confirm))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent data = new Intent();
                        int pos = getIntent().getIntExtra("pos", 0);
                        data.putExtra("pos", pos);
                        setResult(RESULT_OK, data);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onDatePick(View view) {
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TextView tvDate = (TextView) findViewById(R.id.text_view_dialog_edit_date);
        EditDateDialogFragment editItemDialog = EditDateDialogFragment.newInstance(tvDate.getText().toString());
        editItemDialog.show(fm, "fragment_edit_date_dialog");
    }
}
