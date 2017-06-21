package com.ananda.tailing.bike.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ananda.tailing.bike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @package com.ananda.tailing.bike.util
 * @description: 
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-3-5 下午5:45:47
 */
public class ImageLoadManager {
	
	private static DisplayImageOptions options;
	private static ImageLoader imageLoader;
	private static ImageLoadManager imageLoadManager;

	/**
	 * 采用单例模式初始化当前类
	 * 
	 * @return
	 */
	public static ImageLoadManager getInstance(Context context) {
		if (imageLoadManager == null) {
			imageLoadManager = new ImageLoadManager();
		}
		init(context);
		return imageLoadManager;
	}

	/**
	 * 初始化变量，同时配置缓存方式
	 */
	private static void init(Context context) {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
		}
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.frame_bg)
					.showImageForEmptyUri(R.drawable.frame_bg)
					.showImageOnFail(R.drawable.frame_bg).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(5))
					.build();
		}

	}

	/**
	 * 显示图片
	 * 
	 * @param uri
	 * @param imageView
	 */
	public void display(String uri, ImageView imageView) {
		imageLoader.displayImage(uri, imageView, options);
	}

	/**
	 * 显示图片
	 * 
	 * @param uri
	 * @param imageView
	 * @param listener
	 */
	public void display(String uri, ImageView imageView,
			ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, options, listener);
	}

	public void display(String uri, DisplayImageOptions opt,
			ImageView imageView, ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, opt, listener);
	}
}
