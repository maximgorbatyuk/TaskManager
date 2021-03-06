package io.github.maximgorbatyuk.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.database.Database;
import io.github.maximgorbatyuk.taskmanager.database.IDatabaseExecute;
import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.Essential.TaskAdapter;
import io.github.maximgorbatyuk.taskmanager.helpers.NotificationHelper;

public class MainActivity extends AppCompatActivity implements IDatabaseExecute {

    private boolean SHOW_ALL = true;
    private ListView listView;
    private TextView textView;
    private int ITEM_POSITION_CHECKED = -1;
    private int TASK_ID_CHECKED = -1;
    private int DONE_TASK = 0;
    private int UPDATE_TASK = 1;
    private int REMOVE_TASK = 2;
    private TaskAdapter adapter;

    private Database database;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new Database(this);
        listView = (ListView) findViewById(R.id.listTasks);


        textView = (TextView) findViewById(R.id.textView);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ITEM_POSITION_CHECKED = position;
                TASK_ID_CHECKED = getTaskId(view);
                int taskId = getTaskId(view);
                openIntent("open", taskId);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ITEM_POSITION_CHECKED = position;
                TASK_ID_CHECKED = getTaskId(view);
                //openContextMenu(view);
                return false;
            }
        });
        registerForContextMenu(listView);
        // Берется список задач из базы по созданию активити
        getTasksList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openIntent("settings", -1);
            return true;
        }
        if (id == R.id.action_about){
            openIntent("about", -1);
            return true;
        }

        if (id == R.id.action_notification){
            NotificationHelper helper = new NotificationHelper(getApplicationContext(), this.getClass());
            helper.showNotification();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showNotification(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void showSnackbar(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }


    private void openIntent(String action, int id){
        Intent intent = null;
        if (action == "open"){
            intent = new Intent(this, OpenActivity.class);
            intent.putExtra("action", "open");
            intent.putExtra("id", "" + id);

        }
        if (action == "create"){
            intent = new Intent(this, EditActivity.class);
            intent.putExtra("action", "create");

        }
        if (action == "update"){
            intent = new Intent(this, EditActivity.class);
            intent.putExtra("action", "update");
            intent.putExtra("id", "" + id);
        }
        if (action == "settings"){
            intent = new Intent(this, SettingsActivity.class);

        }
        if (action == "about"){
            intent = new Intent(this, AboutActivity.class);

        }
        if (intent != null)
            startActivityForResult(intent, 0);
        TASK_ID_CHECKED = -1;
    }

    private void getTasksList(){
        database.getListOfProjects(this);
    }

    private void fillListByTasks(List<Project> source){
        if (source.size() > 0) {
            adapter = new TaskAdapter(this, source);
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);

        }
        else{
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

    }

    private int getTaskId(View view){
        return Integer.parseInt(((TextView) view.findViewById(R.id.listItemId)).getText().toString());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String action = data.getStringExtra("action");
            if (resultCode == RESULT_OK) {
                if (action == "create")
                    showNotification(getString(R.string.insert_success));

                if (action == "update")
                    showNotification(getString(R.string.update_success));

                getTasksList();

            }
            if (resultCode == RESULT_CANCELED) {
                showNotification(getString(R.string.error_not_created_updated));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (TASK_ID_CHECKED != -1) {
            if (id == UPDATE_TASK)
                openIntent("update", TASK_ID_CHECKED);
            if (id == REMOVE_TASK)
                removeTask(TASK_ID_CHECKED);

        }
        TASK_ID_CHECKED = -1;
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, UPDATE_TASK, 0, getString(R.string.context_update));
        menu.add(0, REMOVE_TASK, 0, getString(R.string.context_remove));
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void removeTask(int id){
        database.destroyProject(id, this);
    }

    public void onFabClick(View view) {
        openIntent("create", -1);
    }

    @Override
    public void onUpdatedSuccess(Boolean result) {
        if (result) {
            showNotification(getString(R.string.remove_success));
            getTasksList();
        }
        else
            showNotification(getString(R.string.error_smth_goes_wrong));
    }

    @Override
    public void onUpdatedSuccess(List<Project> list) {
        fillListByTasks(list);
    }
}
