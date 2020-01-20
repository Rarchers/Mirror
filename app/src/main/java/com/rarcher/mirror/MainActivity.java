package com.rarcher.mirror;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cleveroad.sy.cyclemenuwidget.CycleMenuItem;
import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.cleveroad.sy.cyclemenuwidget.OnMenuItemClickListener;
import com.rarcher.mirror.Info.Makeup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {


    Collection<CycleMenuItem> lip;
    Collection<CycleMenuItem> blush;
    Collection<CycleMenuItem> eyeshadow;
    Collection<CycleMenuItem> lashes;
    Collection<CycleMenuItem> foundation;











    CycleMenuWidget cycleMenuWidget;
    CycleMenuWidget cycleMenuWidgetleft;
    Makeup makeup = new Makeup();
   // C c = new C(getApplicationContext());
    HashMap<String,Integer> map = makeup.getMap();
    String[]ClipColor = {"Mac_Lipstick_RUBY WOO","Mac_Lipstick_DIVA","Mac_Lipstick_RUSSIANRED","Mac_Lipstick_SEE SHEER","Mac_Lipstick_LADYDANGER","Mac_Lipstick_CHILI",
            "Mac_Lipstick_COCKNEY","Mac_Lipstick_LADY BUG","Mac_Lipstick_MOCHA","Mac_Lipstick_DANGEROUS"};
    String[]Blush = {"Mac_Blush_LOVECLOUD","Mac_Blush_PEONY PETAL","Mac_Blush_Full OF JOY","Mac_Blush_PINK TEA","Mac_Blush_MODERN MANDARIN","Mac_Blush_IMMORTAL FLOWER"};
    String[]Choose = {"Clip","Blush","Lashes","EyeShadow","Foundation"};
    String[]Choose_Chinese={"口红","腮红","睫毛","眼影","粉底"};
    String[]Foundation = {"Armani_#2#","Armani_#3#","Armani_#3.5#","Armani_#4#","Armani_#5#"};
    String NowChoose = "Clip";

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


        init();


        cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidgetleft = findViewById(R.id.itemCycleMenuWidgetleft);
        cycleMenuWidgetleft.setMenuRes(R.menu.choose);
       // cycleMenuWidget.setMenuRes(R.menu.makeup_lipstick);
       cycleMenuWidget.setMenuItems(lip);
        cycleMenuWidget.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int itemPosition) {

                switch (NowChoose){
                    case "Clip":
                        Toast.makeText(getApplicationContext(),"当前点击："+ClipColor[itemPosition],Toast.LENGTH_SHORT).show();
                        FaceOverlapFragment.Clip_Color = map.get(ClipColor[itemPosition]);
                        break;
                    case "Blush":
                        Toast.makeText(getApplicationContext(),"当前点击："+Blush[itemPosition],Toast.LENGTH_SHORT).show();
                        FaceOverlapFragment.Blush_Color = map.get(Blush[itemPosition]);
                        break;
                    case"Foundation":
                        Toast.makeText(getApplicationContext(),"当前点击："+Foundation[itemPosition],Toast.LENGTH_SHORT).show();
                        FaceOverlapFragment.Foundation_Color = map.get(Foundation[itemPosition]);
                        break;
                }

            }

            @Override
            public void onMenuItemLongClick(View view, int itemPosition) {

            }
        });

        cycleMenuWidgetleft.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int itemPosition) {
                Toast.makeText(getApplicationContext(),"当前点击："+Choose_Chinese[itemPosition],Toast.LENGTH_SHORT).show();
                NowChoose = Choose[itemPosition];
                cycleMenuWidget.clearDisappearingChildren();
                switch (Choose[itemPosition]){
                    case "Clip":
                       //cycleMenuWidget.setMenuRes(R.menu.makeup_lipstick);
                        cycleMenuWidget.setMenuItems(lip);
                        break;
                    case "Blush":
                      // cycleMenuWidget.setMenuRes(R.menu.makeup_blush);
                        cycleMenuWidget.setMenuItems(blush);
                        break;
                    case "Lashes":
                        cycleMenuWidget.setMenuRes(R.menu.makeup_lashes);
                        break;
                    case "EyeShadow":
                        cycleMenuWidget.setMenuRes(R.menu.makeup_eyeshadow);
                        break;
                    case "Foundation":
                        cycleMenuWidget.setMenuItems(foundation);
                        break;

                }





            }

            @Override
            public void onMenuItemLongClick(View view, int itemPosition) {

            }
        });

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






    private void init(){
        lip = new ArrayList<CycleMenuItem>();
        Resources resources = this.getResources();


        Drawable drawable = resources.getDrawable(R.drawable.mac_rubywoo);
        CycleMenuItem li = new CycleMenuItem(1,drawable);
        lip.add(li);

        drawable = resources.getDrawable(R.drawable.mac_diva);
        li = new CycleMenuItem(2,drawable);
        lip.add(li);

        drawable = resources.getDrawable(R.drawable.mac_rubywoo);
        li = new CycleMenuItem(3,drawable);
        lip.add(li);

        drawable = resources.getDrawable(R.drawable.mac_seasheer);
        li = new CycleMenuItem(4,drawable);
        lip.add(li);

        drawable = resources.getDrawable(R.drawable.mac_ladydanger);
        li = new CycleMenuItem(5,drawable);
        lip.add(li);
        drawable = resources.getDrawable(R.drawable.mac_chili);
        li = new CycleMenuItem(6,drawable);
        lip.add(li);
        drawable = resources.getDrawable(R.drawable.mac_cockney);
        li = new CycleMenuItem(7,drawable);
        lip.add(li);
        drawable = resources.getDrawable(R.drawable.mac_ladybug);
        li = new CycleMenuItem(8,drawable);
        lip.add(li);
        drawable = resources.getDrawable(R.drawable.mac_mocha);
        li = new CycleMenuItem(9,drawable);
        lip.add(li);
        drawable = resources.getDrawable(R.drawable.mac_dangerous);
        li = new CycleMenuItem(10,drawable);
        lip.add(li);







        blush = new ArrayList<CycleMenuItem>();
        drawable = resources.getDrawable(R.drawable.lovecloud);
        li = new CycleMenuItem(1,drawable);
        blush.add(li);

        drawable = resources.getDrawable(R.drawable.peony_petal);
        li = new CycleMenuItem(2,drawable);
        blush.add(li);

        drawable = resources.getDrawable(R.drawable.full_of_joy);
        li = new CycleMenuItem(3,drawable);
        blush.add(li);

        drawable = resources.getDrawable(R.drawable.pink_tea);
        li = new CycleMenuItem(4,drawable);
        blush.add(li);

        drawable = resources.getDrawable(R.drawable.modern_mandarin);
        li = new CycleMenuItem(5,drawable);
        blush.add(li);

        drawable = resources.getDrawable(R.drawable.immortal_flower);
        li = new CycleMenuItem(6,drawable);
        blush.add(li);


        foundation = new ArrayList<>();

        drawable = resources.getDrawable(R.drawable.foundation2);
        li = new CycleMenuItem(1,drawable);
        foundation.add(li);

        drawable = resources.getDrawable(R.drawable.foundation3);
        li = new CycleMenuItem(2,drawable);
        foundation.add(li);

        drawable = resources.getDrawable(R.drawable.foundation3_5);
        li = new CycleMenuItem(3,drawable);
        foundation.add(li);

        drawable = resources.getDrawable(R.drawable.foundation4);
        li = new CycleMenuItem(4,drawable);
        foundation.add(li);

        drawable = resources.getDrawable(R.drawable.foundation5);
        li = new CycleMenuItem(5,drawable);
        foundation.add(li);




    }


    }
