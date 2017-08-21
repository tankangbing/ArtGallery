package com.artgallery.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

public class TestActivity extends BaseActivity{
	
	
	private VideoView videoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		videoView = (VideoView)findViewById(R.id.video_view); 
		String url = "http://192.168.20.160:8888/notify/video/http://192.168.20.160/cirmSystemFile/camInfo/video/a030f066-ce4c-4cb2-a1e4-17eff53474a1_cirm_videotestH264mp4.mp4";
		onvideo(videoView, url);
	}
	
	public void onvideo(VideoView videoView,String url){  
        
        Uri uri = Uri.parse(url);  
         
        videoView.setMediaController(new MediaController(this));  
        videoView.setVideoURI(uri);  
        videoView.start();  
        videoView.requestFocus();  
    }  

}
