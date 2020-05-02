package com.rarcher.mirror;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rarcher.mirror.Info.Makeup;
import com.zeusee.zmobileapi.AuthCallback;
import com.zeusee.zmobileapi.STUtils;

import java.util.List;

import zeusees.tracking.Face;
import zeusees.tracking.FaceInfo;
import zeusees.tracking.FaceTracking;


public class FaceOverlapFragment extends CameraOverlapFragment {

    static FaceInfo faceInfo;
    public static int Alpha = 100;
    public static float blur_radius = 8.5f;
    public static int Clip_Color = 0x8feb3452;
    public static boolean clip_close = true;
    public static int Blush_Color = 0xc7dc909a;
    public static boolean blush_close = true;
    public static int Foundation_Color = Color.WHITE;

    public static boolean foundation_close = true;

    public static void setClip_close(boolean closed){clip_close = closed;}
    public static void setBlush_close(boolean closed){blush_close = closed;}
    public static void setFoundation_close(boolean closed){foundation_close = closed;}

    public static void setClip_Color(int clip_Color) {
        Clip_Color = clip_Color;
    }

    private static final int MESSAGE_DRAW_POINTS = 100;
    private FaceTracking mMultiTrack106 = null;
    private TrackCallBack mListener;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private byte mNv21Data[];
    private byte[] mTmpBuffer;

    private int frameIndex = 0;

    private Paint mPaint;
    private Object lockObj = new Object();
    private boolean mIsPaused = false;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);





        mNv21Data = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mTmpBuffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        frameIndex= 0 ;
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(57, 138, 243));
        int strokeWidth = Math.max(PREVIEW_HEIGHT / 240, 2);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Style.FILL);
        mHandlerThread = new HandlerThread("DrawFacePointsThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DRAW_POINTS) {
                    synchronized (lockObj)
                    {
                        if(!mIsPaused) {
                            handleDrawPoints();
                        }
                    }

                }
            }
        };
        this.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                synchronized (mNv21Data) {
                    System.arraycopy(data, 0, mNv21Data, 0, data.length);
                }

                mHandler.removeMessages(MESSAGE_DRAW_POINTS);
                mHandler.sendEmptyMessage(MESSAGE_DRAW_POINTS);
            }
        });
        return view;
    }

    public void setAlpha(int alpha){
        Alpha = alpha;
    }

    private void handleDrawPoints() {

        synchronized (mNv21Data) {
            System.arraycopy(mNv21Data, 0, mTmpBuffer, 0, mNv21Data.length);
        }

        boolean frontCamera = (CameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT);




        if(frameIndex == 0 )
        {
            mMultiTrack106.FaceTrackingInit(mTmpBuffer,  PREVIEW_HEIGHT,PREVIEW_WIDTH);

        }
        else {
            mMultiTrack106.Update(mTmpBuffer, PREVIEW_HEIGHT,PREVIEW_WIDTH);
        }
        frameIndex+=1;

        List<Face> faceActions = mMultiTrack106.getTrackingInfo();



        if (faceActions != null) {

            if (!mOverlap.getHolder().getSurface().isValid()) {
                return;
            }

            Canvas canvas = mOverlap.getHolder().lockCanvas();
            if (canvas == null)
                return;

            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.setMatrix(getMatrix());
            boolean rotate270 = mCameraInfo.orientation == 270;
            for (Face r : faceActions) {

                Rect rect=new Rect(PREVIEW_HEIGHT - r.left,r.top,PREVIEW_HEIGHT - r.right,r.bottom);

                PointF[] points = new PointF[106];
                for(int i = 0 ; i < 106 ; i++)
                {
                    points[i]  = new PointF(r.landmarks[i*2],r.landmarks[i*2+1]);


                }

                float[] visibles =  new float[106];


                for (int i = 0; i < points.length; i++) {
                    visibles[i] = 1.0f;


                    if (rotate270) {
                        points[i].x = PREVIEW_HEIGHT-points[i].x;

                    }

                }

               // STUtils.drawFaceRect(canvas,rect, PREVIEW_HEIGHT, PREVIEW_WIDTH, frontCamera);
                drawPoints(canvas, mPaint, points,visibles, PREVIEW_HEIGHT, PREVIEW_WIDTH, frontCamera);

            }
            mOverlap.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public static void drawPoints(Canvas canvas, Paint paint, PointF[] points, float[] visibles, int width, int height, boolean frontCamera) {

        //这个地方详细进行分类，调用FaceInfo类来获取详细的区域信息
        faceInfo = new FaceInfo(points,width);
        if (canvas != null) {
            //int strokeWidth = Math.max(width / 240, 2);
            /*for(int i = 0; i <points.length; ++i) {
                PointF p = points[i];
                if (frontCamera) {
                    p.x = (float)width - p.x;
                }

                if ((double)visibles[i] < 0.5D) {
                    paint.setColor(Color.rgb(255, 20, 20));
                } else {
                    paint.setColor(Color.rgb(57, 168, 243));
                }

              //  canvas.drawCircle(p.x, p.y, (float)strokeWidth, paint);
                canvas.drawText(""+i,p.x,p.y,paint);
            }
*/
/*
  Paint text = new Paint();
            text.setTextSize(100);
            text.setColor(0xffffffff);
            canvas.drawText("当前颜色："+Clip_Color,50,50,text);
*/

            // paint.setColor(0XD81B60);


            if (!foundation_close){
                Path face = faceInfo.getFoundation();
                Paint facepaint = new Paint();
                facepaint.setColor(Foundation_Color);
                facepaint.setAlpha(50);
                facepaint.setStyle(Style.FILL);
                facepaint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
                canvas.drawPath(face,facepaint);
            }



            if (!blush_close){
                float[] blush = faceInfo.getBlush();
                Paint blushpatin = new Paint();
                blushpatin.setColor(Blush_Color);
                blushpatin.setAlpha(45);
                blushpatin.setStyle(Style.FILL);
                blushpatin.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));

                Paint blushpatin2 = new Paint();
                blushpatin2.setColor(Blush_Color);
                blushpatin2.setAlpha(47);
                blushpatin2.setStyle(Style.FILL);
                blushpatin2.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));

                canvas.drawCircle(blush[0],blush[1],100,blushpatin);
                canvas.drawCircle(blush[2],blush[3],100,blushpatin);
                canvas.drawCircle(blush[0],blush[1],60,blushpatin2);
                canvas.drawCircle(blush[2],blush[3],60,blushpatin2);
            }


            if (!clip_close){
                Path clip_up = faceInfo.getClip_up();
                Path clip_down = faceInfo.getClip_down();
                paint.setColor(Clip_Color);
                paint.setAlpha(Alpha);
                paint.setStyle(Style.FILL);
                paint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
                canvas.drawPath(clip_up,paint);
                canvas.drawPath(clip_down,paint);
            }





        }
    }







    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.mIsPaused = false;

        if (mMultiTrack106 == null) {

            AuthCallback authCallback = new AuthCallback() {
                @Override
                public void onAuthResult(boolean succeed, String errMessage) {
                    if (!TextUtils.isEmpty(errMessage)) {
                        Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
                    }

                }
            };

            if (authCallback != null) {
                mMultiTrack106 = new FaceTracking("/sdcard/FaceTracking/models");
            }

        }
    }

    @Override
    public void onPause() {
        mHandler.removeMessages(MESSAGE_DRAW_POINTS);
        mIsPaused = true;
        synchronized (lockObj)
        {
            if (mMultiTrack106 != null) {
                mMultiTrack106= null;

            }
        }

        super.onPause();
    }

    public void registTrackCallback(TrackCallBack callback) {
        mListener = callback;
    }

    public interface TrackCallBack {
        void onTrackdetected(int value, float pitch, float roll, float yaw, float eye_dist,
                             int id, int eyeBlink, int mouthAh, int headYaw, int headPitch, int browJump);
    }





}
