package app.aptitude.quiz.admin.craftystudio.aptitudequizadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import utils.FireBaseHandler;
import utils.Questions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText questionName;
    EditText optionA;
    EditText optionB;
    EditText optionC;
    EditText optionD;

    EditText questionExplaination;
    EditText correctAnswer;
    TextView topicName;
    TextView testName;
    EditText previousYears;


    ProgressDialog dialog;
    Questions questions = new Questions();


    FireBaseHandler fireBaseHandler;

    String name = null;
    // AlertDialog alertDialog;

    ArrayList<String> topiclist;
    ArrayList<String> testList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fireBaseHandler = new FireBaseHandler();

        //declaration
        questionName = (EditText) findViewById(R.id.admin_question_name_edittext);
        optionA = (EditText) findViewById(R.id.admin_question_optiona_edittext);
        optionB = (EditText) findViewById(R.id.admin_question_optionb_edittext);
        optionC = (EditText) findViewById(R.id.admin_question_optionc_edittext);
        optionD = (EditText) findViewById(R.id.admin_question_optiond_edittext);

        correctAnswer = (EditText) findViewById(R.id.admin_question_correctanswer_edittext);
        questionExplaination = (EditText) findViewById(R.id.admin_question_explaination_edittext);
        topicName = (TextView) findViewById(R.id.admin_question_topic_edittext);
        testName = (TextView) findViewById(R.id.admin_question_test_edittext);
        previousYears = (EditText) findViewById(R.id.admin_question_previousYears_edittext);

        topicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTopicListDialog();
            }
        });
        testName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openTestListDialog();
            }
        });

        downloadTopicList();
        downloadTestList();
    }

    public boolean createQuestion() {
        //creting story class object
        if (isEmpty(questionName)) {
            questionName.setError("Title cannot be null");
            return false;
        } else {
            questions.setQuestionName(questionName.getText().toString());
        }

        if (isEmpty(optionA)) {
            optionA.setError("Story cannot be null");
            return false;

        } else {
            questions.setOptionA(optionA.getText().toString());
        }

        if (isEmpty(optionB)) {
            optionB.setError("Story cannot be null");
            return false;

        } else {
            questions.setOptionB(optionB.getText().toString());
        }

        if (isEmpty(optionC)) {
            optionC.setError("Story cannot be null");
            return false;

        } else {
            questions.setOptionC(optionC.getText().toString());
        }

        if (isEmpty(optionD)) {
            optionD.setError("Story cannot be null");
            return false;

        } else {
            questions.setOptionD(optionD.getText().toString());
        }

        if (isEmpty(correctAnswer)) {
            correctAnswer.setError("Story cannot be null");
            return false;

        } else {
            questions.setCorrectAnswer(correctAnswer.getText().toString());
        }
        if (isEmpty(questionExplaination)) {
            questionExplaination.setError("Story cannot be null");
            return false;

        } else {
            questions.setQuestionExplaination(questionExplaination.getText().toString());
        }

        if (isEmpty(previousYears)) {

        } else {
            questions.setPreviousYearsName((previousYears.getText().toString()));
        }

        questions.setPushNotificaton(false);


        //random no generate
        final int min = 1;
        final int max = 1000;
        Random random = new Random();
        final int r = random.nextInt((max - min) + 1) + min;

        questions.setRandomNumber(r);


        questions.setNotificationText("edit me");
        return true;
    }

    private void clearData() {

        questionName.setText("");
        optionB.setText("");
        optionA.setText("");
        optionC.setText("");
        optionD.setText("");

        questionExplaination.setText("");
        correctAnswer.setText("");
        topicName.setText("");
        testName.setText("");
        previousYears.setText("");

    }

    private void showDialog() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Uploading");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        dialog.cancel();
    }

    public boolean isEmpty(EditText edittext) {

        if (TextUtils.isEmpty(edittext.getText().toString())) {
            return true;
        } else {
            return false;
        }

    }

    public void uploadQuestions(View view) {

        if (createQuestion()) {

            showDialog();

            fireBaseHandler = new FireBaseHandler();

            fireBaseHandler.uploadQuestion(questions, new FireBaseHandler.OnQuestionlistener() {
                @Override
                public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

                }

                @Override
                public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                }

                @Override
                public void onQuestionUpload(boolean isSuccessful) {

                    if (isSuccessful) {
                        Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Uploaded not Succesfully", Toast.LENGTH_SHORT).show();

                    }
                    hideDialog();
                }
            });
        }
    }

    public void getNameFromDialog(final int topicortest) {
        // get prompts.xml view


        //Custom layouts are discouraged due to the intended use of Snackbars,but this will do your task!
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linearlayout);

        final Snackbar snackbar = Snackbar.make(linearLayout, "Hey Whats Up", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Inflate your custom view with an Edit Text
        LayoutInflater objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = objLayoutInflater.inflate(R.layout.dialog_layout, null); // custom_snac_layout is your custom xml

        final EditText editext = (EditText) snackView.findViewById(R.id.username);
        Button button = (Button) snackView.findViewById(R.id.username_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, editext.getText().toString(), Toast.LENGTH_SHORT).show();
                snackbar.dismiss();

                if (topicortest == 1) {
                    uploadTopicName(editext.getText().toString());
                } else if (topicortest == 2) {
                    uploadTestName(editext.getText().toString());
                }
            }
        });
        layout.addView(snackView, 0);
        snackbar.show();

    }

    public void openTopicListDialog() {

        //  showDialog();

        String[] stockArr = new String[topiclist.size()];
        stockArr = topiclist.toArray(stockArr);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Topic List")
                .setItems(stockArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        questions.setQuestionTopicName(topiclist.get(which));
                        topicName.setText(topiclist.get(which));
                        Toast.makeText(MainActivity.this, topiclist.get(which), Toast.LENGTH_SHORT).show();
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();

        // hideDialog();
    }


    public void downloadTopicList() {
        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadTopicList(30, new FireBaseHandler.OnTopiclistener() {
            @Override
            public void onTopicDownLoad(String topic, boolean isSuccessful) {

            }

            @Override
            public void onTopicListDownLoad(final ArrayList<String> topicList, boolean isSuccessful) {

                if (isSuccessful) {

                    MainActivity.this.topiclist = topicList;
                }
            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

            }
        });

    }

    public void downloadTestList() {

        //  showDialog();
        fireBaseHandler = new FireBaseHandler();

        fireBaseHandler.downloadTestList(30, new FireBaseHandler.OnTestSerieslistener() {
            @Override
            public void onTestDownLoad(String test, boolean isSuccessful) {

            }

            @Override
            public void onTestListDownLoad(final ArrayList<String> testList, boolean isSuccessful) {
                if (isSuccessful) {

                    MainActivity.this.testList = testList;

                }
                //hideDialog();
            }

            @Override
            public void onTestUpload(boolean isSuccessful) {

            }
        });
    }

    public void openTestListDialog() {
        String[] stockArr = new String[testList.size()];
        stockArr = testList.toArray(stockArr);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Test List")
                .setItems(stockArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        questions.setQuestionTestName(testList.get(which));
                        testName.setText(testList.get(which));
                        Toast.makeText(MainActivity.this, testList.get(which), Toast.LENGTH_SHORT).show();
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

    public void uploadTopicName(String s) {

        fireBaseHandler.uploadTopicName(s, new FireBaseHandler.OnTopiclistener() {
            @Override
            public void onTopicDownLoad(String topic, boolean isSuccessful) {

            }

            @Override
            public void onTopicListDownLoad(ArrayList<String> topicList, boolean isSuccessful) {

            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

                if (isSuccessful) {
                    Toast.makeText(MainActivity.this, "Topic Name Uploaded", Toast.LENGTH_SHORT).show();
                }
                // alertDialog.hide();
            }
        });

    }

    public void uploadTestName(String s) {
        fireBaseHandler.uploadTestName(s, new FireBaseHandler.OnTestSerieslistener() {
            @Override
            public void onTestDownLoad(String test, boolean isSuccessful) {

            }

            @Override
            public void onTestListDownLoad(ArrayList<String> testList, boolean isSuccessful) {

            }

            @Override
            public void onTestUpload(boolean isSuccessful) {

                if (isSuccessful)
                    Toast.makeText(MainActivity.this, "Test Name Uploaded", Toast.LENGTH_SHORT).show();


                //  alertDialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            clearData();
            return true;
        } else if (id == R.id.action_topic) {

            getNameFromDialog(1);

        } else if (id == R.id.action_test) {
            getNameFromDialog(2);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
