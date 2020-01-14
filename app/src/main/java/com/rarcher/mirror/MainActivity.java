package com.rarcher.mirror;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener;
import com.rarcher.mirror.Info.Makeup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import static com.rarcher.mirror.FaceOverlapFragment.faceInfo;

public class MainActivity extends AppCompatActivity  {

    CycleMenuWidget cycleMenuWidget;
    CycleMenuWidget cycleMenuWidgetleft;
    Makeup makeup = new Makeup();
    HashMap<String,Integer> map = makeup.getMap();
    String[]ClipColor = {"Mac_Lipstick_RUBY WOO","Mac_Lipstick_DIVA","Mac_Lipstick_RUSSIANRED","Mac_Lipstick_SEE SHEER","Mac_Lipstick_LADYDANGER","Mac_Lipstick_CHILI",
            "Mac_Lipstick_COCKNEY","Mac_Lipstick_LADY BUG","Mac_Lipstick_MOCHA","Mac_Lipstick_DANGEROUS"};

    private final static int CAMERA_REQUEST_CODE = 0x111;
    public void copyFilesFromAssets(Context context, String oldPath, String newPath) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                // directory
                File file = new File(newPath);
                if (!file.mkdir())
                {
                    Log.d("mkdir","can't make folder");

                }

                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, oldPath + "/" + fileName,
                            newPath + "/" + fileName);
                }
            } else {
                // file
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void InitModelFiles()
    {

        String assetPath = "FaceTracking";
        String sdcardPath = Environment.getExternalStorageDirectory()
                + File.separator + assetPath;
        copyFilesFromAssets(this, assetPath, sdcardPath);

    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidgetleft = findViewById(R.id.itemCycleMenuWidgetleft);
        cycleMenuWidgetleft.setMenuRes(R.menu.choose);
        cycleMenuWidget.setMenuRes(R.menu.makeup_lipstick);







        cycleMenuWidget.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int itemPosition) {
                Toast.makeText(getApplicationContext(),"当前点击："+ClipColor[itemPosition],Toast.LENGTH_SHORT).show();
                FaceOverlapFragment.Clip_Color = map.get(ClipColor[itemPosition]);
            }

            @Override
            public void onMenuItemLongClick(View view, int itemPosition) {

            }
        });

        cycleMenuWidgetleft.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int itemPosition) {

            }

            @Override
            public void onMenuItemLongClick(View view, int itemPosition) {

            }
        });


      /*  SeekBar seekBar1 = findViewById(R.id.seekBar2);

        final TextView textView1 = findViewById(R.id.textView2);*/

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Please grant camera permission first", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST_CODE);
                }
            }
        }

      /*  seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float v = progress;
                v/=10;
                textView1.setText("当前高斯模糊数值:"+  v  );
                FaceOverlapFragment.blur_radius = v;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/



        verifyStoragePermissions(this);

        InitModelFiles();





    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作

                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final FaceOverlapFragment fragment = (FaceOverlapFragment) getFragmentManager()
                .findFragmentById(R.id.overlapFragment);
        fragment.registTrackCallback(new FaceOverlapFragment.TrackCallBack() {

            @Override
            public void onTrackdetected(final int value, final float pitch, final float roll, final float yaw, final float eye_dist,
                                        final int id, final int eyeBlink, final int mouthAh, final int headYaw, final int headPitch, final int browJump) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    }
