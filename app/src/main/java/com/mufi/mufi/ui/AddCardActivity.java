package com.mufi.mufi.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.Validation;
import com.mufi.mufi.databinding.ActivityAddCardBinding;
import com.mufi.mufi.model.AddCardModel;
import com.mufi.mufi.model.MufiRest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCardActivity extends AppCompatActivity {
    private ActivityAddCardBinding binding;
    private String resultString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.card1.addTextChangedListener(new Card1Listener());
        binding.card2.addTextChangedListener(new Card2Listener());
        binding.card3.addTextChangedListener(new Card3Listener());
        binding.cardExMm.addTextChangedListener(new CardExListener());

        binding.requestAddCardButton.setOnClickListener(new RequestCardAddListener());
    }

    // 뒤로가기 버튼 클릭 시
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCardActivity.this);
            builder.setTitle(R.string.cancel);
            builder.setMessage(R.string.cancel_add_card);

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    Toast.makeText(getApplicationContext(), R.string.canceled_add_card, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    return;
                }
            });
            builder.show();

            return true;
        }
        return false;
    }

    // 카드 등록하기 버튼 리스너
    class RequestCardAddListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 유효성 검사 통과 시 카드 정보 서버에 전송, 등록
            if (validateCardAll()) {
                MufiRest mufiRest = MufiRest.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                Date date;
                try {
                    date = sdf.parse(binding.cardBirthday.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                sdf.applyPattern("yyyy-MM-dd");

                resultString = mufiRest.send(binding.card1.getText().toString()
                                            + binding.card2.getText().toString()
                                            + binding.card3.getText().toString()
                                            + binding.card4.getText().toString()
                                            + "/" + binding.cardExYy.getText().toString()
                                            + "/" + binding.cardExMm.getText().toString()
                                            + "/" + sdf.format(date)
                                            + "/" + AppUserDTO.getInstance().getId()
                                    , MufiTag.REST_ADD_CARD);

                AddCardModel addCardModel = new AddCardModel();
                boolean isAddCardSuccess = addCardModel.setResultString(resultString);

                showDialog(isAddCardSuccess);
            }
        }

        // 카드 등록 성공 여부에 따라 다른 Dialog를 띄우는 메소드
        void showDialog(boolean success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCardActivity.this);

            if (success) {      // 카드 등록에 성공했다면 카드 프래그먼트로 돌아감
               builder.setTitle(R.string.success);
                builder.setMessage(R.string.success_add_card);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Intent intent = new Intent();
                        setResult(MufiTag.RESULT_ADD_CARD);
                        finish();
                        return;
                    }
                });
            } else {        // 카드 등록 실패 시
                builder.setTitle(R.string.failed);
                builder.setTitle(R.string.failed_add_card);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        return;
                    }
                });
            }

            builder.show();
        }

        boolean validateCardAll() {
            // 카드 번호 유효성 검사
            if (!Validation.validateCardNumber(binding.card1.getText().toString()) ||
                    !Validation.validateCardNumber(binding.card2.getText().toString()) ||
                    !Validation.validateCardNumber(binding.card3.getText().toString()) ||
                    !Validation.validateCardNumber(binding.card4.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.validate_card_number, Toast.LENGTH_SHORT).show();
                binding.card1.requestFocus();
                return false;
            }

            // 카드 유효기간 유효성 검사
            if (!Validation.validateExMonth(binding.cardExMm.getText().toString()) ||
                    !Validation.validateExYear(binding.cardExYy.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.validate_card_ex, Toast.LENGTH_SHORT).show();
                binding.cardExMm.requestFocus();
                return false;
            }

            // 카드 생년월일
            if (!Validation.validateBirthdaySix(binding.cardBirthday.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.validate_birth_six, Toast.LENGTH_SHORT).show();
                binding.cardBirthday.requestFocus();
                return false;
            }

            return true;
        }
    }

    // 카드번호 4자리 입력하면 다음 입력란으로 이동하게 하는 리스너
    class Card1Listener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.card1.getText().toString().length() == 4) {
                binding.card2.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    class Card2Listener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.card2.getText().toString().length() == 4) {
                binding.card3.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    class Card3Listener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.card3.getText().toString().length() == 4) {
                binding.card4.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    // 카드 유효기간 자동으로 넘어가게 하는 리스너
    class CardExListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.cardExMm.getText().toString().length() == 2) {
                binding.cardExYy.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }
}