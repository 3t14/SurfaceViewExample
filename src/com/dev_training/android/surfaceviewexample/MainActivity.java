package com.dev_training.android.surfaceviewexample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			Callback {

		private static final String TAG = "PlaceholderFragment";
		private SurfaceView mSurfaceView;
		private float mRadius;
		private int mRadiusDelta;


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			// SurfaceViewを取得しコールバックを設定
			mSurfaceView = (SurfaceView) rootView
					.findViewById(R.id.surfaceView1);
			SurfaceHolder holder = mSurfaceView.getHolder();
			
			holder.addCallback(this);
			
			Log.d(TAG, "AddCallBack");
			
			ScheduledExecutorService execService = 
					Executors.newSingleThreadScheduledExecutor();
			execService.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					draw(mSurfaceView.getHolder());
				}
				
			}, 1000, 10, TimeUnit.MILLISECONDS);
			
			return rootView;
		}

		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			draw(holder);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "surfaceCreated");
			// 生成時の処理
			mRadiusDelta = 10;
			mRadius = 100;
			
			draw(holder);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// 終了時の処理
			Log.d(TAG, "surfaceDestroyed");
		}

		//
		public void draw(SurfaceHolder holder) {
			int width = holder.getSurfaceFrame().width();
			int height = holder.getSurfaceFrame().height();
			
			// 描画
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(Color.WHITE);
			Paint paint = new Paint();
			// 半径と幅、高さによって明るさを変動
			paint.setColor(Color.rgb(
					(int) (255.0f * (float)(mRadius) / (float)Math.max(width/2, height/2)),
					0, 0));
			paint.setStyle(Style.FILL);
			canvas.drawCircle(width / 2, height / 2, mRadius, paint);

			mRadius += mRadiusDelta;
			if (mRadiusDelta > 0) {
				if (mRadius > Math.max(width / 2, height / 2)) {
					mRadiusDelta *= -1;
					mRadius = Math.max(width / 2, height / 2);
				}
			} else if (mRadius < 0) {
				// 反転
				mRadiusDelta *= -1;
				mRadius = 0;
			}

			holder.unlockCanvasAndPost(canvas);
			
		}
	}

}
