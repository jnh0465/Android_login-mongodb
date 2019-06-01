package com.jiwoolee.android_smartlectureroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edit_login_id, edit_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = ((Retrofit) retrofitClient).create(IMyService.class);

        edit_login_id = (MaterialEditText)findViewById(R.id.edit_id);
        edit_login_password = (MaterialEditText)findViewById(R.id.edit_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(edit_login_id.getText().toString(),
                        edit_login_password.getText().toString());
            }
        });

        txt_create_account = (TextView)findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout, null);
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setTitle("REGISTERATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANSEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edit_register_id = (MaterialEditText)register_layout.findViewById(R.id.edit_id);
                                MaterialEditText edit_register_name = (MaterialEditText)register_layout.findViewById(R.id.edit_name);
                                MaterialEditText edit_register_password = (MaterialEditText)register_layout.findViewById(R.id.edit_password);

                                if(TextUtils.isEmpty(edit_register_id.getText().toString())){
                                    Toast.makeText(MainActivity.this, "email cannot be null", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                                if(TextUtils.isEmpty(edit_register_name.getText().toString())){
                                    Toast.makeText(MainActivity.this, "name cannot be null", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                                if(TextUtils.isEmpty(edit_register_password.getText().toString())){
                                    Toast.makeText(MainActivity.this, "password cannot be null", Toast.LENGTH_SHORT).show();
                                    return ;
                                }

                                registerUser(edit_register_id.getText().toString(),
                                        edit_register_name.getText().toString(),
                                        edit_register_password.getText().toString());
                            }
                        }).show();
            }
        });
    }

    private void registerUser(String student_id, String student_name, String student_password) {
        compositeDisposable.add(iMyService.registerUser(student_id, student_name, student_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private void loginUser(String student_id, String student_password) {
        if(TextUtils.isEmpty(student_id)){
            Toast.makeText(MainActivity.this, "email cannot be null", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(TextUtils.isEmpty(student_password)){
            Toast.makeText(MainActivity.this, "password cannot be null", Toast.LENGTH_SHORT).show();
            return ;
        }

        compositeDisposable.add(iMyService.loginUser(student_id, student_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show(); //node 서버에서 response.json으로 보낸 응답 받아서 toast
                    }
                })
        );
    }
}
