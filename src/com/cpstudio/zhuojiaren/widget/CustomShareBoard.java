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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
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

	public CustomShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
		initView(activity);
		// ������Ҫ��������ƽ̨
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
		// ���÷��������
		setShareContent();
		int id = v.getId();
		if (id == R.id.wechat) {
			performShare(SHARE_MEDIA.WEIXIN);
		} else if (id == R.id.wechat_circle) {
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
		} else if (id == R.id.qq) {
			performShare(SHARE_MEDIA.QQ);
		} else if (id == R.id.qzone) {
			performShare(SHARE_MEDIA.QZONE);
		} else if (id == R.id.xinlang) {
			performShare(SHARE_MEDIA.SINA);
		} else if (id == R.id.message) {
			performShare(SHARE_MEDIA.SMS);
		} else if (id == R.id.copy) {
			ClipboardManager cmb = (ClipboardManager) mActivity.getSystemService(Activity.CLIPBOARD_SERVICE);
			if(targetUrl!=null)
			cmb.setText(targetUrl);
			Toast.makeText(mActivity, "�Ѿ��������ӵ�������", Toast.LENGTH_SHORT).show();
		}else if (id == R.id.hide) {
			CustomShareBoard.this.dismiss();
		}  
		else {
			Toast.makeText(mActivity, "پ��", Toast.LENGTH_SHORT).show();
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
					showText += "����ɹ�";
				} else {
					showText += "����ʧ��";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}

	/**
	 * ���÷���ƽ̨����</br>
	 */
	private void configPlatforms() {
		// �������SSO��Ȩ
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		addSMS();
		// ���QQ��QZoneƽ̨
		addQQQZonePlatform();
		// ���΢�š�΢������Ȧƽ̨
		addWXPlatform();
	}

	/**
	 * ���ݲ�ͬ��ƽ̨���ò�ͬ�ķ�������</br>
	 */
	private void setShareContent() {

		// ����SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		// ΢������
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// ��������Ȧ���������
		CircleShareContent circleMedia = new CircleShareContent();
		// ����QQ�ռ��������
		QZoneShareContent qzone = new QZoneShareContent();
		// QQ����
		QQShareContent qqShareContent = new QQShareContent();
		TencentWbShareContent tencent = new TencentWbShareContent();
		// �����ʼ��������ݣ� �����Ҫ����ͼƬ��ֻ֧�ֱ���ͼƬ
		MailShareContent mail = new MailShareContent();
		// ���ö��ŷ�������
		SmsShareContent sms = new SmsShareContent();
		// ����
		SinaShareContent sinaContent = new SinaShareContent();
		// ����title
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
	 * ������е�ƽ̨</br>
	 */
	private void addCustomPlatforms() {
		// ���΢��ƽ̨
		addWXPlatform();
		addSMS();
		// ���emailƽ̨
		addEmail();
		mController.registerListener(new SnsPostListener() {

			@Override
			public void onStart() {
				Toast.makeText(mActivity, "share start...", 0).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				Toast.makeText(mActivity, "code : " + eCode, 0).show();
			}
		});

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS);
		mController.openShare(mActivity, false);
	}

	/**
	 * ��Ӷ���ƽ̨</br>
	 */
	private void addSMS() {
		// ��Ӷ���
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	/**
	 * ���Emailƽ̨</br>
	 */
	private void addEmail() {
		// ���email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	/**
	 * @�������� : ���΢��ƽ̨����
	 * @return
	 */
	private void addWXPlatform() {
		// ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = "wx967daebe835fbeac";
		String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @�������� : ���QQƽ̨֧�� QQ��������ݣ� �����������ͣ� �����������֡�ͼƬ�����֡���Ƶ. ����˵�� : title, summary,
	 *       image url�б�����������һ��, targetUrl��������,��ҳ��ַ������"http://"��ͷ . title :
	 *       Ҫ������� summary : Ҫ��������ָ��� image url : ͼƬ��ַ [������������������дһ��] targetUrl
	 *       : �û�����÷���ʱ��ת����Ŀ���ַ [����] ( ������д��Ĭ������Ϊ������ҳ )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// ���QQ֧��, ��������QQ�������ݵ�target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId,
				appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// ���QZoneƽ̨
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

}
