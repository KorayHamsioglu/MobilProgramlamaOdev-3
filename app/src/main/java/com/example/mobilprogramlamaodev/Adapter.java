package com.example.mobilprogramlamaodev;

import static androidx.core.app.ActivityCompat.startIntentSenderForResult;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public static final int DELETE_REQUEST_CODE=13;
    ArrayList<Song> songList;
    Context context;
    Activity activity;
    public Adapter(Activity activity,ArrayList<Song> songList, Context context) {
        this.activity=activity;
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         Song songData=songList.get(position);
         holder.itemTextView.setText(songData.getTitle());
         holder.itemTextViewDuration.setText(convertDuration(songData.getDuration()));

        String path=songData.getData();
        long id=getContentUri(songData);
        Uri uri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);

         holder.btnDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                // File root=android.os.Environment.getExternalStorageDirectory();
                 //System.out.println("FILE: "+root);

                 try {
                     delete(uri,path);

                 } catch (IntentSender.SendIntentException e) {
                     e.printStackTrace();
                 }


                 songList.remove(holder.getAdapterPosition());
                 notifyItemRemoved(holder.getAdapterPosition());
                 notifyItemRangeChanged(holder.getAdapterPosition(),songList.size());




             }
         });

         holder.btnSend.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String path=songData.getData();
                 Intent share=new Intent(Intent.ACTION_SEND);
                 share.setType("audio/mp3");
                 share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

                 share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                 activity.startActivity(Intent.createChooser(share,"Share mp3 File"));
             }
         });


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 //if (data==null){
           //System.out.println("OLMADI");
       //}
            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.currentIndex= holder.getAdapterPosition();
                 Intent intent=new Intent(context,SingleSongActivity.class);
                 intent.putExtra("LIST",songList);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);

             }
         });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    private void delete(Uri uri,String path) throws IntentSender.SendIntentException {
        ContentResolver contentResolver=context.getContentResolver();
        List<Uri> uris=new ArrayList<>();
        Collections.addAll(uris,uri);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            PendingIntent pendingIntent=MediaStore.createDeleteRequest(contentResolver,uris);
            activity.startIntentSenderForResult(pendingIntent.getIntentSender(),DELETE_REQUEST_CODE,null,0,0,0,null);
           //startIntentSenderForResult((Activity) context,pendingIntent.getIntentSender(),DELETE_REQUEST_CODE,null,0,0,0,null);
        }
        else{
            File file=new File(path);
            if (file.exists()){
                file.delete();

            }

        }

    }

    private long getContentUri(Song songData){
       long id=0;
        String[] projections={MediaStore.Audio.Media._ID};
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projections,MediaStore.Audio.Media.DATA+"=?",new String[]{songData.getData()},null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int index=cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id=Long.parseLong(cursor.getString(index));
            }
        }
        cursor.close();
        return id;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImageView;
        TextView itemTextView,itemTextViewDuration;
        Button btnSend,btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            itemImageView=itemView.findViewById(R.id.itemImageView);
            itemTextView=itemView.findViewById(R.id.itemTextView);
            itemTextViewDuration=itemView.findViewById(R.id.itemTextViewDuration);
            btnSend=itemView.findViewById(R.id.buttonSend);
            btnDelete=itemView.findViewById(R.id.buttonDelete);



        }
    }

    public static String convertDuration(String duration){
        Long sec=Long.parseLong(duration);
        return String.format("%02d"+":"+"%02d", TimeUnit.MILLISECONDS.toMinutes(sec)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(sec)%TimeUnit.MINUTES.toSeconds(1) );

    }
}
