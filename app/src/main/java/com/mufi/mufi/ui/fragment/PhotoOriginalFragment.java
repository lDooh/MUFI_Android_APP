package com.mufi.mufi.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mufi.mufi.DTO.PhotoInfo;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.databinding.FragmentPhotoOriginalBinding;
import com.mufi.mufi.model.PhotoModel;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.ui.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoOriginalFragment extends Fragment {
    private FragmentPhotoOriginalBinding binding;
    private PhotoInfo originalPhoto;
    private String shopName, photoDate;
    private double photoSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotoOriginalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.photoDeleteButton.setOnClickListener(new DeleteButtonListener());
        binding.photoDownloadButton.setOnClickListener(new PhotoDownloadButton());
    }

    @Override
    public void onResume() {
        super.onResume();
        get_original_photo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void get_original_photo() {
        PhotoModel photoModel = new PhotoModel();
        MufiRest mufiRest = MufiRest.getInstance();

        PhotoInfo photoInfo = ((MainActivity) getActivity()).getPhotoInfo();

        String resultString = mufiRest.send(photoInfo.getPayment_id() +
                        "/" + photoInfo.getPhoto_number(),
                MufiTag.REST_GET_PHOTO_ORIGINAL);

        boolean isGetPhotoSuccess = photoModel.setResultString(resultString);

        if (isGetPhotoSuccess) {
            originalPhoto = photoModel.getPhotoInfoArrayList().get(0);
            photoSize = photoModel.getPhotoSize();
            shopName = photoModel.getShopName();

            display_original_photo();
        } else {
            Toast.makeText(getActivity(), "사진을 가져오는 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void display_original_photo() {
        byte[] imageBytes = Base64.decode(originalPhoto.getImage_content().getBytes(), Base64.DEFAULT);
        binding.photoImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        binding.photoImageView.setBackgroundResource(R.drawable.btn_border);
        binding.photoImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        binding.photoDateTextView.setText(originalPhoto.getPayment_date());
        binding.photoSizeTextView.setText(String.format("%.3f", photoSize) + " MB");
        binding.photoShopTextView.setText(shopName);
    }

    class DeleteButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setTitle(R.string.delete);
            builder1.setMessage(R.string.sure_delete_photo);

            builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PhotoModel photoModel = new PhotoModel();
                    MufiRest mufiRest = MufiRest.getInstance();

                    String resultString = mufiRest.send(originalPhoto.getPayment_id() +
                            "/" + originalPhoto.getPhoto_number(),
                            MufiTag.REST_DELETE_PHOTO);

                    boolean isDeletePhotoSuccess = photoModel.setDeletePhotoResultString(resultString);

                    if (isDeletePhotoSuccess) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setTitle(R.string.delete);
                        builder2.setMessage("사진을 삭제하였습니다.");
                        builder2.setCancelable(false);

                        builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 사진 10장 모음으로 돌아감
                                ((MainActivity)getActivity()).getSupportFragmentManager().
                                        beginTransaction().remove(PhotoOriginalFragment.this).
                                        commit();
                                ((MainActivity)getActivity()).getSupportFragmentManager().
                                        popBackStack();
                                return;
                            }
                        });
                        builder2.show();
                    } else {
                        Toast.makeText(getActivity(), "사진 삭제에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

            builder1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 사진 10장 모음으로 돌아감
                    ((MainActivity)getActivity()).getSupportFragmentManager().
                            beginTransaction().remove(PhotoOriginalFragment.this).
                            commit();
                    ((MainActivity)getActivity()).getSupportFragmentManager().
                            popBackStack();
                    return;
                }
            });
            builder1.show();
        }
    }

    class PhotoDownloadButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd_HH:mm:ss");
            Date date = new Date();

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d(MufiTag.DEBUG_FILE_DOWNLOAD, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                byte[] imageBytes = Base64.decode(originalPhoto.getImage_content().getBytes(), Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Log.d("File Download Tag", "File Download Success");
                Toast.makeText(getContext(), "사진 다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.d("File Download Tag", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("File Download Tag", "Error accessing file: " + e.getMessage());
            }
        }

        private  File getOutputMediaFile() {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                            getAbsolutePath());

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            File mediaFile;
            String mImageName = "MUFI_"+ timeStamp +".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            return mediaFile;
        }
    }
}
