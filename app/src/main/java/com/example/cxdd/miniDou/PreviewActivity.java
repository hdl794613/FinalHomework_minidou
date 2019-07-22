package com.example.cxdd.miniDou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.cxdd.miniDou.R;
import com.example.cxdd.miniDou.bean.PostVideoResponse;
import com.example.cxdd.miniDou.newtork.IMiniDouyinService;
import com.example.cxdd.miniDou.newtork.RetrofitManager;
import com.example.cxdd.miniDou.utils.ResourceUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewActivity extends AppCompatActivity {
    private ImageView imageView;
    private VideoView videoView;
    private Intent intent;
    private Button buttonUpLoad;
    private String imagePath, videoPath;
    private Uri imageUri, videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = findViewById(R.id.img_pic);
        videoView = findViewById(R.id.img_show);
//
        intent = getIntent();
        imagePath = intent.getStringExtra("image_path");
        videoPath = intent.getStringExtra("video_path");
        Log.e("1233", imagePath);
        Log.e("1233", videoPath);

        imageUri = Uri.fromFile(new File(imagePath));
        videoUri = Uri.fromFile(new File(videoPath));

        setPic();
        setVideo();
        upLoad();
    }

    private void upLoad() {
        buttonUpLoad = findViewById(R.id.btn_picture_show);
        buttonUpLoad.setOnClickListener(view -> {
            //发布后添加动画
            postMessage();
            finish();
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(PreviewActivity.this, MainActivity.class));
                }
            }, 200);

            //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);//anmi是动画切换的资源
           // finish();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    private void postMessage() {
        RetrofitManager.get(IMiniDouyinService.HOST).create(IMiniDouyinService.class).createVideo("CXDD_002", "CXDD_002", getMultipartFromUri("cover_image", imageUri), getMultipartFromUri("video", videoUri)).enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                String toast;
                if (response.isSuccessful()) {
                    toast = "Post Success!";
                } else {
                    toast = "Post Failure... Please try again!";
                }
                Toast.makeText(PreviewActivity.this, toast, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                Toast.makeText(PreviewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void setVideo() {
        videoView.setVideoPath(videoPath);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

            }
        });
    }

    private void setPic() {
        //todo 根据imageView裁剪
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap = rotaingImageView(90,bitmap);
        imageView.setImageBitmap(bitmap);
    }
    private Bitmap rotaingImageView(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }
    private String getVideoPath(int bytedance) {
        return "android.resource://" + this.getPackageName() + "/" + bytedance;
    }
}
