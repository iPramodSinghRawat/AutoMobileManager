package com.alpha.ghosty.automobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AppLock extends SuperActivity {
    EditText passwordEdtTxt;
    int wrnAtmpt=0;
    TextView pCntTmr;
    Button sbmtBtn;

    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        dbHandler= new DBhandler(this, null, null, 1);
        passwordEdtTxt = (EditText)findViewById(R.id.passwordEdtTxt);
        pCntTmr = (TextView)findViewById(R.id.pCntTmr);
        sbmtBtn = (Button)findViewById(R.id.sbmtBtn);

        passwordEdtTxt.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            //addCourseFromTextBox();
                            checkUserPassword(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    public void checkUserPassword(View view){

        /* implent number of wrong atempt */

        if (passwordEdtTxt.getText().toString().trim().equals("")) {
            passwordEdtTxt.setError("Password is required!");
            passwordEdtTxt.setHint("Please enter a Password ");
        }else{

            String upaswrd=passwordEdtTxt.getText().toString();

            if(upaswrd.length()<6){
                upaswrd="";
                passwordEdtTxt.setError("Error: Invalid Password ");
                passwordEdtTxt.setHint("Error: Minimum Length Must be 6");
                //passwordEdtTxt.setText("Error in Password");
            }else {
                if(wrnAtmpt<2){

                    int rtn = dbHandler.log_in(upaswrd);
                    if (rtn > 0) {
                        Intent intent = new Intent(getBaseContext(), MenuPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        passwordEdtTxt.setText("");
                        passwordEdtTxt.setError("Error: Incorrect Password ");
                        passwordEdtTxt.setHint("Error: Check Your Password");
                        wrnAtmpt++;
                    }
                }else{

                    Toast.makeText(this, " Try Again After 30 Second ", Toast.LENGTH_SHORT).show();

                    passwordEdtTxt.setText("");

                    sbmtBtn.setEnabled(false);
                    new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            pCntTmr.setText("Wait for seconds : " + millisUntilFinished / 1000 + " ");
                        }

                        public void onFinish() {
                            sbmtBtn.setEnabled(true);
                            wrnAtmpt=0;//set <- zero after 40 Second
                            pCntTmr.setText(" Try Now ! ");
                        }

                    }.start();

                }
            }
        }
    }

    public void redirect2ForgetPassword(View view){
        startActivity(new Intent(getBaseContext(), ForgetPassword.class));
        finish();
    }
}
