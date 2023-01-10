package com.mufi.mufi.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mufi.mufi.DTO.AppUserDTO;
import com.mufi.mufi.DTO.ShopDto;
import com.mufi.mufi.DTO.ShopInfo;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.databinding.FragmentHomeBinding;
import com.mufi.mufi.model.MufiRest;
import com.mufi.mufi.model.QrModel;
import com.mufi.mufi.model.ShopModel;
import com.mufi.mufi.ui.QrScannerActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private FragmentHomeBinding binding;
    private MapView mapView;
    private static NaverMap naverMap;
    private InfoWindow infoWindow = new InfoWindow();
    private ArrayList<ShopInfo> shopInfoArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ShopModel shopModel = new ShopModel();
        MufiRest mufiRest = MufiRest.getInstance();

        String resultString = mufiRest.send("", MufiTag.REST_GET_SHOP_LOCATION);
        boolean isGetLocationSuccess = shopModel.setResultString(resultString);

        if (isGetLocationSuccess) {
            shopInfoArrayList = shopModel.getShopLocation();
        } else {
            Toast.makeText(getActivity(), "가게 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(getActivity()) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker _marker = infoWindow.getMarker();
                String shop_id = _marker.getTag().toString();
                View view = View.inflate(getContext(), R.layout.view_info_window, null);

                ShopModel _shopModel = new ShopModel();
                MufiRest _Mufi_rest = MufiRest.getInstance();

                String _resultString = _Mufi_rest.send(shop_id,
                        MufiTag.REST_GET_SHOP_INFO);

                boolean isGetShopSuccess = _shopModel.setResultString(_resultString);

                if (isGetShopSuccess) {
                    ShopDto shopDto = _shopModel.getShopDto();
                    DecimalFormat df = new DecimalFormat("###,###");
                    ((TextView)view.findViewById(R.id.shop_name_text_view)).
                            setText(shopDto.getShop_name());
                    ((TextView)view.findViewById(R.id.basic_price_text_view)).
                            setText(df.format(4000) + " 원");
                    ((TextView)view.findViewById(R.id.shop_hour_text_view)).
                            setText(shopDto.getOpen_time() + " ~ " + shopDto.getClose_time());
                    ((TextView)view.findViewById(R.id.usage_status_text_view)).
                            setText(shopDto.getNumber_booth() + " 대 중 " +
                                    shopDto.getNumber_using_booth() + " 대 이용 중");
                } else {

                }

                return view;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // QR 버튼
        binding.QRCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QrScannerActivity.class);
                mGetContent.launch(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setMaxZoom(18.0);
        naverMap.setMinZoom(10.0);

        // 최초 표시 위치
//        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.5666103, 126.9783882);
//        naverMap.moveCamera(cameraUpdate);
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.5666103, 126.9783882), 13);
        naverMap.setCameraPosition(cameraPosition);

        display_shop_location();
    }

    // 매장 위치 마커 표시
    private void display_shop_location() {
        for (ShopInfo shopInfo : shopInfoArrayList) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(shopInfo.getNorth_latitude(), shopInfo.getEast_longitude()));
            marker.setIcon(MarkerIcons.BLACK);
            marker.setIconTintColor(Color.GREEN);
            marker.setTag(shopInfo.getShopId());

            // 마커를 클릭하면:
            Overlay.OnClickListener listener = overlay -> {
                Marker _marker = (Marker)overlay;

                if (_marker.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(_marker);
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close();
                }

                return true;
            };
            marker.setOnClickListener(listener);

            marker.setMap(naverMap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        binding = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // QrScannerActivity에서 돌아왔을 때
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == MufiTag.RESULT_QR_SCAN) {
                        Intent intent = result.getData();

                        String qrURL = intent.getStringExtra("qrURL");
                        Log.d("READ_QR", "읽은 QR: " + qrURL);

                        // QR 코드 촬영 후 메인 메뉴로 돌아온 것이라면
                        if (qrURL != null) {
                            QrModel qrModel = new QrModel(qrURL);
                            MufiRest mufiRest = MufiRest.getInstance();
                            AppUserDTO appUserDTO = AppUserDTO.getInstance();

                            mufiRest.send(qrModel.getShopId()
                                    + "/" + qrModel.getKioskNumber()
                                    + "/" + appUserDTO.getId(), MufiTag.REST_QR);
                            Log.d("QR DEBUG TAG", "읽은 QR URL: " + qrURL);

                            Toast.makeText(getActivity(), qrURL, Toast.LENGTH_SHORT).show();
                        }

                        // QR 코드를 찍지 않고 뒤로가기를 통해 돌아왔다면 아무 동작 안함
                    }
                }
            });
}