package com.mufi.mufi.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.Validation;
import com.mufi.mufi.model.LoginModel;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    static final int PERMISSIONS_REQUEST = 0x0000001;
    private String resultString;
    private boolean isLoginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(new loginClickListener());
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        onCheckPermission();
    }

    private Context getContext() {
        return getApplicationContext();
    }

    // 로그인 버튼 리스너
    class loginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String id = binding.inputID.getText().toString();
            if (!Validation.validateID(id)) {
                Toast.makeText(getApplicationContext(), getString(R.string.input_id), Toast.LENGTH_SHORT).show();
                return;
            }

            String pw = binding.inputPW.getText().toString();
            if (!Validation.validatePW(pw)) {
                Toast.makeText(getApplicationContext(), getString(R.string.input_pw), Toast.LENGTH_SHORT).show();
                return;
            }

            // ID와 PW가 올바르게 입력되었을 시
            LoginModel loginModel = new LoginModel();
            MufiRest mufiRest = MufiRest.getInstance();

            resultString = mufiRest.send(binding.inputID.getText() + "/" + binding.inputPW.getText(), MufiTag.REST_LOGIN);
            isLoginSuccess = loginModel.setResultString(resultString);

            if (isLoginSuccess) {     // 로그인 성공 시
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                AppUserDTO appUserDTO = loginModel.getAppUserDTO(id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getContext().startActivity(intent);
                finish();
            }
            else {      // 로그인 실패 시
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("");
                builder.setMessage(R.string.login_error);
                // builder.setIcon();       // TODO: 무피 아이콘으로 설정
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        return;
                    }
                });
                builder.show();
            }

        }
    }
    
    // 필요 권한
    String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // 여러 권한 요청 메소드
    public void onCheckPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    Toast.makeText(this, "앱 실행을 위해서는 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
                }
            }
        }
    }

    // 권한 요청 코드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        // 하나의 권한이라도 허용이 되지 않았다면
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "권한 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    Toast.makeText(this, "권한 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
