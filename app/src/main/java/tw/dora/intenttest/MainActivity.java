package tw.dora.intenttest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Bitmap thumbnail;
    private UIHandler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiHandler = new UIHandler();
        img = findViewById(R.id.img);
    }

    public void test1(View view) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "OK")
                .putExtra(AlarmClock.EXTRA_HOUR, 12)
                .putExtra(AlarmClock.EXTRA_MINUTES, 06)
                .putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


    }

    public void test2(View view) {
           Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, "聚餐")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "台中市")
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis()+90000*1000);
           if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

    }

    public void test3(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            //Bundle bundle = data.getExtras();

            thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();
            Log.v("brad",fullPhotoUri.toString());
            if(thumbnail!=null){
                uiHandler.sendEmptyMessage(0);
            }else{
                Log.v("brad","null");
            }


            Log.v("brad",getPath(fullPhotoUri));
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return "no data";
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        Log.v("brad","xckjcoxvk");
        return s;
    }

    private class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(thumbnail);
        }
    }
}
