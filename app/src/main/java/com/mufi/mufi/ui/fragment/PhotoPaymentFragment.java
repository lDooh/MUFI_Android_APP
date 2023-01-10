package com.mufi.mufi.ui.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mufi.mufi.DTO.PhotoInfo;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.databinding.FragmentPhotoViewBinding;
import com.mufi.mufi.model.PhotoModel;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.ui.MainActivity;

import java.util.ArrayList;

public class PhotoPaymentFragment extends Fragment {
    private FragmentPhotoViewBinding binding;
    ArrayList<PhotoInfo> photoInfoArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        get_photo_payment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void get_photo_payment() {
        PhotoModel getPhotoConnect = new PhotoModel();
        MufiRest mufiRest = MufiRest.getInstance();

        PhotoInfo photoInfo = ((MainActivity) getActivity()).getPhotoInfo();

        String resultString = mufiRest.send(photoInfo.getPayment_id(),
                MufiTag.REST_GET_PHOTO_PAYMENT);

        boolean isGetPaymentSuccess = getPhotoConnect.setResultString(resultString);

        if (isGetPaymentSuccess) {
            photoInfoArrayList = getPhotoConnect.getPhotoInfoArrayList();
            // 사진을 가져오는 데 성공했다면 UI 적용
            display_photo_payment();
        }
        else {
            Toast.makeText(getActivity(), "사진을 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void display_photo_payment() {
        binding.photoViewLayout.removeAllViews();

        for (PhotoInfo photoInfo : photoInfoArrayList) {
            ImageButton imageButton = new ImageButton(getActivity());
            imageButton.setLayoutParams(new GridLayout.LayoutParams());
            imageButton.getLayoutParams().width = 0;
            imageButton.getLayoutParams().height = 500;

            ((GridLayout.LayoutParams) imageButton.getLayoutParams()).columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f);

            ((GridLayout.LayoutParams) imageButton.getLayoutParams()).setMargins(30, 30, 30, 30);
            imageButton.setPadding(5, 5, 5, 5);

            imageButton.setTag(photoInfo.getPhoto_number());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (PhotoInfo pi : photoInfoArrayList) {
                        System.out.println("View: " + view.getTag().toString() + ", pi: " + Integer.toString(pi.getPhoto_number()));
                        if (view.getTag().equals(pi.getPhoto_number())) {
                            ((MainActivity) getActivity()).setPhotoInfo(pi);
                            break;
                        }
                    }
                    ((MainActivity) getActivity()).createPhotoOriginalFragment();
                }
            });

            byte[] imageBytes = Base64.decode(photoInfo.getImage_content().getBytes(), Base64.DEFAULT);
            imageButton.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            imageButton.setBackgroundResource(R.drawable.btn_border);       // 버튼 테두리 + item state 설정
            imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);    // 이미지의 크기를 View 크기에 맞게 자동으로 조정
//            imageButton.setClipToOutline(true);

            binding.photoViewLayout.addView(imageButton);
        }
    }
}