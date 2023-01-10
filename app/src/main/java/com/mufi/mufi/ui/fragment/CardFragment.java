package com.mufi.mufi.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mufi.mufi.DTO.AppCardDTO;
import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.databinding.FragmentCardBinding;
import com.mufi.mufi.model.CardModel;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.ui.AddCardActivity;

import java.util.ArrayList;

public class CardFragment extends Fragment {
    private FragmentCardBinding binding;
    ArrayList<AppCardDTO> appCardArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        get_cards();

        binding.addCardButton.setOnClickListener(new AddCardListener());
    }

    @Override
    public void onResume() {
        super.onResume();

        get_cards();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 서버로부터 카드 정보를 받아오는 메소드
    private void get_cards() {
        CardModel cardModel = new CardModel();
        MufiRest mufiRest = MufiRest.getInstance();

        String resultString = mufiRest.send(AppUserDTO.getInstance().getId(),
                MufiTag.REST_GET_CARD);

        boolean cardGetSuccess = cardModel.setResultString(resultString);

        if (cardGetSuccess) {
            appCardArrayList = cardModel.getAppCardArrayList();
            display_card();
        }
        else {
            Toast.makeText(getActivity(), "등록된 카드가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void display_card() {
        // 카드 리스트를 보여주기 전 레이아웃 초기화
        binding.cardListLayout.removeAllViews();

        for (int i = 0; i < appCardArrayList.size(); i++) {
            LinearLayout cardLayout1 = new LinearLayout(getActivity());
            cardLayout1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params_mw =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            params_mw.setMargins(0, 0, 0, 30);
            cardLayout1.setLayoutParams(params_mw);
            cardLayout1.setBackgroundResource(R.drawable.card_num_border);
            cardLayout1.setPadding(10, 10, 10, 10);

            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.ic_card_img);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
            cardLayout1.addView(imageView);

            LinearLayout cardInfoLayout = new LinearLayout(getActivity());
            cardInfoLayout.setOrientation(LinearLayout.VERTICAL);
            cardInfoLayout.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            LinearLayout.LayoutParams params_ww =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            params_ww.setMargins(0, 0, 0, 2);

            TextView textView1 = new TextView(getActivity());
            textView1.setLayoutParams(params_ww);
            textView1.setText(appCardArrayList.get(i).getCardId());
            textView1.setTextColor(Color.BLACK);
            cardInfoLayout.addView(textView1);

            params_ww.setMargins(0, 2, 0, 0);
            TextView textView2 = new TextView(getActivity());
            textView2.setLayoutParams(params_ww);
            textView2.setText(cardNumberMasking(appCardArrayList.get(i).getCardNumber()));
            textView2.setTextColor(Color.BLACK);
            cardInfoLayout.addView(textView2);

            cardLayout1.addView(cardInfoLayout);

            Button button = new Button(getActivity());
            params_ww.setMargins(0, 0, 0, 0);
            button.setLayoutParams(params_ww);
            button.setGravity(Gravity.CENTER_VERTICAL);
            button.setText(R.string.delete);
            button.setTag(appCardArrayList.get(i).getCardId());
            button.setOnClickListener(new DeleteCardListener());
            cardLayout1.addView(button);

            binding.cardListLayout.addView(cardLayout1);
        }
    }

    class AddCardListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), AddCardActivity.class);
            mGetContent.launch(intent);
        }
    }

    class DeleteCardListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setTitle(R.string.delete);
            builder1.setMessage("카드를 삭제하시겠습니까?");

            builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CardModel cardModel = new CardModel();
                    MufiRest mufiRest = MufiRest.getInstance();

                    String resultString = mufiRest.send(view.getTag().toString(),
                            MufiTag.REST_DELETE_CARD);

                    boolean isDeleteCardSuccess = cardModel.setDeleteCardResultString(resultString);

                    if (isDeleteCardSuccess) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setTitle(R.string.delete);
                        builder2.setMessage("카드를 삭제하였습니다.");
                        builder2.setCancelable(false);

                        builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onResume();
                                return;
                            }
                        });
                        builder2.show();
                    } else {
                        Toast.makeText(getActivity(), "카드 삭제에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

            builder1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder1.show();
        }
    }

    private String cardNumberMasking(String cardNumber) {
        ArrayList<String> cardNumbers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cardNumbers.add(cardNumber.substring(i * 4, i * 4 + 4));
        }

        return cardNumbers.get(0) + "-****-****-" + cardNumbers.get(3);
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // 카드를 등록하고 돌아온 것이라면
                    if (result.getResultCode() == MufiTag.RESULT_ADD_CARD) {
                        Intent intent = result.getData();

                        display_card();
                    }
                }
            }
    );
}