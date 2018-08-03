package com.example.asus.bookingreal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bookingreal.Model.CheckUserResponse;
import com.example.asus.bookingreal.Model.User;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1001;
    Button but;
    IBookingAPI mService;
    TextView text_name_main,text_name_main2;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
            break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        text_name_main = (TextView)findViewById(R.id.text_name_main);
        text_name_main2 = (TextView)findViewById(R.id.text_name_main2);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fronts/HATTEN.TTF");
        text_name_main.setTypeface(typeface);
        text_name_main2.setTypeface(typeface);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);


        mService = Common.getAPI();
        but = (Button) findViewById(R.id.button1);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

        if (AccountKit.getCurrentAccessToken() != null) {
            final AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
            alertDialog.show();
            alertDialog.setMessage("กรุณารอสักครู่ ...");

            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString())
                            .enqueue(new Callback<CheckUserResponse>() {
                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                    CheckUserResponse userResponse = response.body();
                                    if (userResponse.isExists()) {
                                        mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                alertDialog.dismiss();
                                                Common.cUser = response.body();
                                                updateTokenToFirebase();
                                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        alertDialog.dismiss();
                                        showRegisterDialog(account.getPhoneNumber().toString());
                                    }

                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                }
                            });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("ERROR", accountKitError.getErrorType().getMessage());
                }
            });
        }


    }

    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "ยกเลิก", Toast.LENGTH_SHORT).show();

            } else {
                if (result.getAccessToken() != null) {
                    final AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("กรุณารอสักครู่ ...");

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse = response.body();
                                            if (userResponse.isExists()) {
                                                mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        alertDialog.dismiss();
                                                        Common.cUser = response.body();
                                                        updateTokenToFirebase();
                                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                alertDialog.dismiss();
                                                showRegisterDialog(account.getPhoneNumber().toString());
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR", accountKitError.getErrorType().getMessage());
                        }
                    });
                }
            }
        }
    }

    private void showRegisterDialog(final String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("สมัครสมาชิก");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final MaterialEditText editText_Cname = (MaterialEditText) register_layout.findViewById(R.id.edit1);
        final MaterialEditText editText_Bname = (MaterialEditText) register_layout.findViewById(R.id.edit2);


        Button btn_register = (Button) register_layout.findViewById(R.id.button2);
        builder.setView(register_layout);
        final AlertDialog dialog = builder.create();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (TextUtils.isEmpty(editText_Cname.getText().toString())) {
                    Toast.makeText(MainActivity.this, "กรุณาใส่ชื่อบริษัทของท่าน ..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editText_Bname.getText().toString())) {
                    Toast.makeText(MainActivity.this, "กรุณาใส่ชื่อของท่าน ..", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog waitingD = new SpotsDialog(MainActivity.this);
                waitingD.show();
                waitingD.setMessage("โปรดรอสักครู่ ...");
                mService.registerNewUser(phone, editText_Cname.getText().toString(), editText_Bname.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        waitingD.dismiss();
                        User user = response.body();
                        if (TextUtils.isEmpty(user.getError_msg())) {
                            Toast.makeText(MainActivity.this, "สมัครเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            Common.cUser = response.body();
                            updateTokenToFirebase();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        waitingD.dismiss();
                    }
                });
            }
        });
        dialog.show();

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.asus.bookingreal", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {

        if (isBackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked = true;
        Toast.makeText(this, "กรุณากดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        isBackButtonClicked = false;
        super.onResume();
    }

    private void updateTokenToFirebase() {
        IBookingAPI mService = Common.getAPI();
        mService.updateToken(Common.cUser.getPhone(), FirebaseInstanceId.getInstance().getToken(),"0").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("DEBUG", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("DEBUG", t.getMessage());
            }
        });
    }

}

