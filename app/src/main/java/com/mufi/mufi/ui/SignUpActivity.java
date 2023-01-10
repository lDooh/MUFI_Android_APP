package com.mufi.mufi.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.Validation;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.model.SignUpModel;
import com.mufi.mufi.databinding.ActivitySignUpBinding;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private int checkedGender = -1;
    private boolean idAble = false, signUpSuccess = false;
    private String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 생일 입력 란 클릭 시 실행
        binding.inputBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(SignUpActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)).show();
            }
        });

        // 회원가입 취소 버튼 리스너
        binding.signUpCancelButton.setOnClickListener(new SignUpCancelListener());

        // 회원가입 확인 버튼 리스너
        binding.signUpOkButton.setOnClickListener(new SignUpOkListener());

        // 라디오 버튼 그룹 리스너
        binding.genderRadio.setOnCheckedChangeListener(new GenderRadioListener());
    }

    // DatePickerDialog 날짜 선택 시 실행될 리스너
    DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    binding.inputBirthday.setText(year + "-" + (month + 1) + "-" + day);
                }
            };

    // 회원가입 취소 Dialog 띄우기
    private void checkCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle(R.string.cancel);
        builder.setMessage(R.string.cancel_sign_up);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                finish();
                return;
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        builder.show();
    }

    // 뒤로 가기 버튼 눌렀을 시
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkCancel();

            return true;
        }

        return false;
    }

    // 취소 버튼 리스너
    class SignUpCancelListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            checkCancel();
            return;
        }
    }

    // 회원가입 버튼 리스너
    class SignUpOkListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String id = binding.inputID.getText().toString().trim();
            if (!Validation.validateID(id)) {
                Toast.makeText(getApplicationContext(), getString(R.string.id_validate), Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: ID 중복검사 -> 따로 버튼 만들기
            if (!isNotDuplicatedID(id)) {
                Toast.makeText(getApplicationContext(), getString(R.string.duplicate_id), Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.able_id), Toast.LENGTH_SHORT).show();
            }

            String pw = binding.inputPW.getText().toString().trim();
            if (!Validation.validatePW(pw)) {
                // 올바른 비밀번호를 입력해 주세요.
                Toast.makeText(getApplicationContext(), getString(R.string.pw_validate), Toast.LENGTH_SHORT).show();
                return;
            }

            String name = binding.inputName.getText().toString().trim();
            if (!Validation.validateName(name)) {
                Toast.makeText(getApplicationContext(), getString(R.string.validate_name), Toast.LENGTH_SHORT).show();
                return;
            }

            String callNumber = binding.inputCallNumber.getText().toString();
            if (!Validation.validateCallNumber(callNumber)) {
                Toast.makeText(getApplicationContext(), getString(R.string.validate_call_number), Toast.LENGTH_SHORT).show();
                return;
            }

            if (binding.inputBirthday.getText().toString() == null ||
                    binding.inputBirthday.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.validate_birth), Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkedGender == -1) {
                Toast.makeText(getApplicationContext(), getString(R.string.validate_gender), Toast.LENGTH_SHORT).show();
                return;
            }

            // 회원가입 요청
            boolean signUpCompleted;
            signUpCompleted = isSignUpSuccess(id, pw, name, callNumber, binding.inputBirthday.getText().toString(), checkedGender);

            if (signUpCompleted) {      // 회원가입에 성공했다면.
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle(R.string.sign_up);
                builder.setMessage(R.string.sign_up_success);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        finish();
                        return;
                    }
                });

                builder.show();
                return;
            } else {        // 회원가입 실패 시
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle(R.string.sign_up);
                builder.setMessage(R.string.sign_up_failed);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        return;
                    }
                });

                builder.show();
                return;
            }
        }
    }

    // ID 중복검사 메소드
    private boolean isNotDuplicatedID(String id) {
        MufiRest mufiRest = MufiRest.getInstance();
        SignUpModel signUpModel = new SignUpModel();

        resultString = mufiRest.send(id, MufiTag.REST_ID_DUPLICATED);
        idAble = signUpModel.isNotDuplicated(resultString);

        return idAble;
    }

    // 회원가입 성공 여부 반환
    private boolean isSignUpSuccess(String sid, String spassword, String sname, String scallNumber, String sbirthday, int sgender) {
        MufiRest mufiRest = MufiRest.getInstance();
        SignUpModel signUpModel = new SignUpModel();

        resultString = mufiRest.send(signUpModel.getSignUpString(sid, spassword, sname, scallNumber, sbirthday, sgender), MufiTag.REST_SIGN_UP);
        signUpSuccess = signUpModel.isSignUpSuccess(resultString);

        return signUpSuccess;
    }

    // 라디오버튼 그룹 리스너
    class GenderRadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            if (checkedId == R.id.male_radio) {
                checkedGender = 1;
                return;
            } else if (checkedId == R.id.female_radio) {
                checkedGender = 0;
                return;
            }
        }
    }
}