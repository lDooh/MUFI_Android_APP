/*
 * QR 코드 스캐너
 * 깃허브 구글 GMS API: https://github.com/googlesamples/android-vision
 * */

package com.mufi.mufi.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;

import java.io.IOException;

public class QrScannerActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        Intent intent = getIntent();
        if (intent.getStringExtra("id") != null) {
            id = intent.getStringExtra("id");
        }

        surfaceView = findViewById(R.id.surface_view);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource
                .Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(29.8f)     // 프레임 설정
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)      // 자동으로 초점 맞추기
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0) {
                    String qrCodeContents = qrcode.valueAt(0).displayValue;

                    Intent intent = new Intent();
                    intent.putExtra("qrURL", qrCodeContents);
                    setResult(MufiTag.RESULT_QR_SCAN, intent);
                    finish();       // QR 코드를 인식하면 이 인텐트 종료
                }
            }
        });
    }
}