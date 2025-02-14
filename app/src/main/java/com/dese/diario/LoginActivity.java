package com.dese.diario;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dese.diario.POJOS.DatosUsr;
import com.dese.diario.POJOS.VariablesLogin;
import com.dese.diario.Utils.FirebaseService.FirebaseConection;
import com.dese.diario.Utils.Session;
import com.dese.diario.Utils.ShowProgressDialog;
import com.dese.diario.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    final String correo= "correo";
    final String password1= "password";
    final String idusuario= "idusuario";
    final String cuenta= "cuenta";
    final String telefono= "telefono";
    final String foto= "foto";
    final String Success="Success";
    final String GET="GET";
    final static String url= Urls.login;

    //Resource
    String datos = "";
    // private EditText mEmailView;
    public EditText mPasswordView;

   public EditText mEmailView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView failedLoginMessage, mForgetPassword;
    View focusView = null;
    private String email;
    private String password;

    DatosUsr du;
    ShowProgressDialog spd;

    String tokennew;

    private ProgressDialog mProgress;
    private Session conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         spd= new ShowProgressDialog();

        getToken();

        inicializarButton();

        //Hide softKeyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }//Fin oNCreate

    private void inicializarButton() {
        conf = new Session(this);
        /*if(conf.getUserEmail() != null)
            userLogin2();*/


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Procesando...");
        mProgress.setMessage("Porfavor espere...");
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setIndeterminate(true);

        du= new DatosUsr();
        mEmailView= (EditText) findViewById(R.id.tvEmail_Login);
        //       populateAutoComplete();
        failedLoginMessage = (TextView)findViewById(R.id.failed_login);
        mPasswordView=(EditText)findViewById(R.id.tvPassword_Login);
        //    buttonLogin = (Button) findViewById(R.id.email_sign_in_button);

        Bundle i=(getIntent().getExtras());
            if (i == null)
                mEmailView.setText("");
         else mEmailView.setText( i.getString("mail"));

            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        //*****spd.DialogProgress (LoginActivity.this, false);
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failedLoginMessage.setText("");

                        attemptLogin();


            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fp = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(fp);
            }
        });


    }



    /**---LOGIN---**/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void attemptLogin(){
        boolean mCancel = this.loginValidation();
        if (mCancel) {
            focusView.requestFocus();
        } else {
            if(isNetworkConnected()){
                //spd.DialogProgress (LoginActivity.this, true);
              /*  if(conf.getUserEmail() != null)
                    userLogin2();
                else*/
                    userLogin();


            }else{
               //***** spd.DialogProgress (LoginActivity.this, false);
                showAlertDialog(getResources().getString(R.string.title_conection_fail),
                        getResources().getString(R.string.message_need_conection));
            }


        }
    }
    // Check if the device is connected to the Internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {

            return false;
        } else
            return true;
    }

    private void showAlertDialog(String title, String message) {
        final AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(Dialog.BUTTON_POSITIVE,("Ajustes"),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                startActivity(intent);
            }
        });
        alert.setButton(Dialog.BUTTON_NEGATIVE,"Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.show();
    }

    private boolean loginValidation() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
       conf.setUserEmail(email);
       conf.setUserPass(password);
        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        return cancel;
    }

  /*  private void populateAutoComplete(){
        String[] countries = getResources().getStringArray(R.array.autocomplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        mEmailView.setAdapter(adapter);
    }*/

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }



    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private void userLogin() {

        try {
            if (isOnline()) {
               // System.out.println(R.string.user_Login_entra);
               //********** spd.DialogProgress (LoginActivity.this, true);
                mProgress.show();
                AsyncTask task = new ObtenerDatos();
                String[][] parametros = {
                        {url.toString()},
                        {correo, password1, "token"},
                        {mEmailView.getText().toString(), mPasswordView.getText().toString(), tokennew}};
                task.execute(parametros);
              //Toast.makeText(LoginActivity.this,mEmailView.getText().toString()+mPasswordView.getText().toString()+ tokennew, Toast.LENGTH_LONG).show();

            }else{
                mProgress.dismiss();
               //***** spd.DialogProgress (LoginActivity.this, false);
         }
        }catch (Exception ex){
                Log.e("Exception->", ex.getMessage().toString());
            //System.out.println(ex);
        }
    }//Fin UserLogin
    private void userLogin2() {

        try {
            if (isOnline()) {
                // System.out.println(R.string.user_Login_entra);
                //********** spd.DialogProgress (LoginActivity.this, true);
                mProgress.show();
                AsyncTask task = new ObtenerDatos();
                String[][] parametros = {
                        {url.toString()},
                        {conf.getUserEmail(), conf.getUserPass(), "token"},
                        {mEmailView.getText().toString(), mPasswordView.getText().toString(), tokennew}};
                task.execute(parametros);
                //Toast.makeText(LoginActivity.this,mEmailView.getText().toString()+mPasswordView.getText().toString()+ tokennew, Toast.LENGTH_LONG).show();

            }else{
                mProgress.dismiss();
                //***** spd.DialogProgress (LoginActivity.this, false);
            }
        }catch (Exception ex){
            Log.e("Exception->", ex.getMessage().toString());
            //System.out.println(ex);
        }
    }//Fin UserLogin

    private class ObtenerDatos extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... parametros) {
            try {
                return ConexionServer(parametros[0][0], parametros[1], parametros[2]);
            } catch (IOException e) {
                Log.e("Obtener datos---->", e.getMessage().toString());
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String resultado){

            try {

                VariablesLogin var_Login;
                String datoslogin;
                JSONObject jsonObject = new JSONObject(resultado.toString());
                //   txtToken.setText(jsonObject.getString("success"));
                var_Login = new VariablesLogin();
                datoslogin = jsonObject.getString(Success);
                if(datoslogin.equals("[]")){
                    Toast.makeText(LoginActivity.this,"Hubo un problema. Intente más tarde",Toast.LENGTH_LONG).show();

                }else{
                    if (datoslogin.length()>=10){
                      //  spd.DialogProgress (LoginActivity.this, true);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString(Success));
                        for (int x=0; x<jsonArray.length(); x++) {
                            JSONObject json = new JSONObject(jsonArray.get(x).toString());

                            var_Login.setIdusuario(json.getString(idusuario));
                            getToken();
                            var_Login.setCuenta(json.getString(cuenta));
                            var_Login.setCorreo(json.getString(correo));
                            var_Login.setTelefono(json.getString(telefono));
                            var_Login.setFoto(json.getString(foto));
                            var_Login.setFportada(json.getString("fportada"));
                            var_Login.setToken(tokennew);

                            du.setFoto(json.getString(foto));
                            du.setFportada(json.getString("fportada"));
                            du.setToken(tokennew);

                            FirebaseConection fc= new FirebaseConection();
                            fc.setDatabaseUser(var_Login);
                            //spd.dismiss();
                        }
                        finishLogin();

                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Verifique su Correo y Contraseña",Toast.LENGTH_LONG).show();
                       //*****spd.DialogProgress (LoginActivity.this, false);


                    }
                }
            } catch (Exception e) {
                mProgress.dismiss();

                Drawable drawable = getResources().getDrawable(R.drawable.image_cloud_sad);
               new MaterialDialog.Builder(LoginActivity.this)
                         .title("Uja! Hubo un error")

                        .content("Lo lamentamos, intente más tarde. Plis!"+ "\n"+e.getMessage().toString())
                        .negativeText(R.string.disagree)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                mProgress.dismiss();
                            }
                        })
                        .show();
              //****  spd.DialogProgress (LoginActivity.this, false);
                Log.e("Login.ObtenerDatos", e.getMessage()+ ">--<"+e.getLocalizedMessage());

            }
           //*** spd.DialogProgress (LoginActivity.this, false);
        }

    }
    public void getToken(){
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);

        tokennew= prefs.getString( "tokenNew", "tokentmps" ); // (key, default)
        Log.e("Cuenta", tokennew);



    }
    private String ConexionServer(String dir_url, String[] variables, String[] valores) throws IOException {
        URL url = new URL(dir_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(60000);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod(GET);
        int total = variables.length;
        for (int i = 0; i < total; i++) {
            conn.setRequestProperty(variables[i], valores[i]);
        }
        conn.setDoInput(true);
        conn.connect();
        datos = readStream(conn.getInputStream());
        conn.disconnect();
        System.out.println("datosserver: "+datos);
        return datos;

    }

    /*private void openProfile(){
        showProgress(true);
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);


    }*/

    private void finishLogin() {

        SharedPreferences preferences =
                getSharedPreferences(getString(R.string.my_preferences2), MODE_PRIVATE);

        preferences.edit()
                .putBoolean(getString(R.string.login_complete),true).apply();

        Intent main = new Intent(this, MainActivity.class);
        main.putExtra("Type", "Manual");
        startActivity(main);


        finish();
    }




}
