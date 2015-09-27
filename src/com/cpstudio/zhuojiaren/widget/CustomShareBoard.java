/**
 * 
 */

package com.cpstudio.zhuojiaren.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.ZhuoShareContent;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomShareBoard extends PopupWindow implements OnClickListener {

	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Activity mActivity;
	private String title;
	private UMImage image;
	private String content;
	private String targetUrl;
	private ZhuoShareContent zhuoShareContent;

	public CustomShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
		initView(activity);
		// 配置需要分享的相关平台
		configPlatforms();
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.custom_board, null);
		rootView.findViewById(R.id.wechat).setOnClickListener(this);
		rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
		rootView.findViewById(R.id.qq).setOnClickListener(this);
		rootView.findViewById(R.id.qzone).setOnClickListener(this);
		rootView.findViewById(R.id.message).setOnClickListener(this);
		rootView.findViewById(R.id.copy).setOnClickListener(this);
		rootView.findViewById(R.id.zhuo_share).setOnClickListener(this);
		rootView.findViewById(R.id.xinlang).setOnClickListener(this);
		rootView.findViewById(R.id.hide).setOnClickListener(this);
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// 设置分享的内容
		setShareContent();
		int id = v.getId();
		if (id == R.id.wechat) {
			performShare(SHARE_MEDIA.WEIXIN);
		} else if (id == R.id.wechat_circle) {
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
		} else if (id == R.id.qq) {
//			performShare(SHARE_MEDIA.QQ);
		} else if (id == R.id.qzone) {
//			performShare(SHARE_MEDIA.QZONE);
		} else if (id == R.id.xinlang) {
//			performShare(SHARE_MEDIA.SINA);
		} else if (id == R.id.message) {
			performShare(SHARE_MEDIA.SMS);
		} else if (id == R.id.copy) {
			ClipboardManager cmb = (ClipboardManager) mActivity.getSystemService(Activity.CLIPBOARD_SERVICE);
			if(targetUrl!=null)
			cmb.setText(targetUrl);
			Toast.makeText(mActivity, "已经复制链接到剪贴板", Toast.LENGTH_SHORT).show();
		}else if (id == R.id.hide) {
			CustomShareBoard.this.dismiss();
		}  
		else {
			if(zhuoShareContent==null){
				Toast.makeText(mActivity, "没有分享内容", Toast.LENGTH_SHORT).show();	
				return;
			}
			AppClientLef.getInstance(mActivity).shareToZhuo(mActivity, new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (JsonHandler.checkResult((String) msg.obj,
							mActivity)) {
						Toast.makeText(mActivity, "分享到倬脉", Toast.LENGTH_SHORT).show();	
					} else {
						CommonUtil.displayToast(mActivity, R.string.data_error);
						return;
					}
				}
			}, 1, zhuoShareContent);
			
		}
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "分享成功";
				} else {
					showText += "分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		addSMS();
		// 添加QQ、QZone平台
		addQQQZonePlatform();
		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		// 微信设置
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		// QQ好友
		QQShareContent qqShareContent = new QQShareContent();
		TencentWbShareContent tencent = new TencentWbShareContent();
		// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
		MailShareContent mail = new MailShareContent();
		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		// 新浪
		SinaShareContent sinaContent = new SinaShareContent();
		// 设置title
		if (title != null) {
			weixinContent.setTitle(title);
			circleMedia.setTitle(title);
			qqShareContent.setTitle(title);
			qzone.setTitle(title);
			sinaContent.setTitle(title);
			mail.setTitle(title);
		}
		if (content != null) {
			weixinContent.setShareContent(content);
			qzone.setShareContent("share test");
			circleMedia.setShareContent(content);
			qqShareContent.setShareContent(content);
			tencent.setShareContent(content);
			mail.setShareContent(content);
			sms.setShareContent(content);
			sinaContent.setShareContent(content);
		}
		if (image != null) {
			weixinContent.setShareMedia(image);
			circleMedia.setShareMedia(image);
			qzone.setShareMedia(image);
			qqShareContent.setShareMedia(image);
			sinaContent.setShareImage(image);
		}
		if (targetUrl != null) {
			weixinContent.setTargetUrl(targetUrl);
			circleMedia.setTargetUrl(targetUrl);
			qzone.setTargetUrl(targetUrl);
			qqShareContent.setTargetUrl(targetUrl);
			sinaContent.setTargetUrl(targetUrl);
		}
		mController.setShareMedia(weixinContent);
		mController.setShareMedia(circleMedia);
		mController.setShareMedia(qzone);
		mController.setShareMedia(qqShareContent);
		mController.setShareMedia(tencent);
		mController.setShareMedia(mail);
		mController.setShareMedia(sms);
		mController.setShareMedia(sinaContent);
	}


	/**
	 * 添加短信平台</br>
	 */
	private void addSMS() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	/**
	 * 添加Email平台</br>
	 */
	private void addEmail() {
		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx95c80614da3d5a6a";
		String appSecret = "427537e09cb92d0a51c45cf10091b080";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId,
				appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	public UMSocialService getmController() {
		return mController;
	}

	public Activity getmActivity() {
		return mActivity;
	}

	public String getTitle() {
		return title;
	}

	public UMImage getImage() {
		return image;
	}

	public String getContent() {
		return content;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setmController(UMSocialService mController) {
		this.mController = mController;
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(UMImage image) {
		this.image = image;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public ZhuoShareContent getZhuoShareContent() {
		return zhuoShareContent;
	}

	public void setZhuoShareContent(ZhuoShareContent zhuoShareContent) {
		this.zhuoShareContent = zhuoShareContent;
	}

}
