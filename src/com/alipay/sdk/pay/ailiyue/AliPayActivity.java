package com.alipay.sdk.pay.ailiyue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alipay.sdk.app.PayTask;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.CartActivity;

public class AliPayActivity extends FragmentActivity {

	//商户PID
	public static final String PARTNER = "2088021294618045";
	//商户收款账号
	public static final String SELLER = "zhuomai@hopechina.cc";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALo+I+536G2AjfaOaZDLIVLqb+Z4p2K73JstBoco95Ax66K8OUqGZDTW918QjAVjuIPrYSei/D0yL6De6WkJlRYPSRyE87tvj7WRHVVuBw/uMcXisxanvx0xghEC26FjABOrk6j7OKDnpXum1+imuetIRo+R9tFKsqeG20ByvFHnAgMBAAECgYAPwIe7eKrQ8CmRq/Pjyjp0T93NCXFm2WrMOR+vONjPellqty7FTd6V1YSwpq6hGhEWH0dlWdSpvF/o5FGrj6LrSLuSLfOVJKcpSXNDbuCWruT7R0HBimWvSuUNItB6xODk85PAxqV87TstlA2ctUmj3AjqLVrc+tw3bVeTJCGnOQJBAOa9FEL3rrkV/R4L7jWIUsm2CsAhkStsGCslETYRLvW75Lzz8jykw9+cDqSruyPfdEqTwJ+oBoScVHHdmcb2/3UCQQDOoflQ6cGbKpwEkc0Yz8Q7i3jNoj/g8xvn0cxPgqZGfKy3cnun+zKT7KloL2AiRAkWUz49CEDJomNzn13OO9xrAkACLs++QnGYtqiKXDqIrx15Ywt+/dJNwtwHrHYZONhk4r4rO3OTN0vTFbrdu/ItSLPk8LjlycUmBASYfywxQazxAkBw9AfmnDGSUaOBt/Xml0SMzDwYJJZIsHxTp9LIsAGWzHZZvYItHFrUk+0zH5qsgIZ7JrwB/u3ziwpUXQyZPQ3LAkAkM2MX3AfHYh3ovaTquDI9Hjes6a3jaxiuITrplBBcwQPLROTo1KkfHaDfP72ernwuqSZBRPKJhQKVgNSnP00Z";
//	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANE82aJEv8ocDgln/HDB++VewSpQbpTjRV71tWU+uZoqGoDVpBKZKfuhQ+aXVdnHD1H9LtN8YMJpInDooiotVGz2fJcS+WuGi6gFoOB5mAh9QGxKiQiaEv8XKYVRf+hUOoVoxh0Te/m5r7KKH4tF10SLKSwKrMa4wQOJgQdYQVetAgMBAAECgYBM5TUCOovVZgwOjuWqNtAxEmqg92A8XW3AIUKhy7SAIJsO3/TlVN6l+DUxCErntw09T3OnC12v9N1Qa141a+VLX/Ij3Bwy3V4HGlczX6OhfJcvsPrOfEO7uMhWiN6odd0c+OWen8iCNJzR9UAuqlzkU24A/wm8KnkVEhui4oiYDQJBAO8wLfVdGcV3R2gi9e9RkzID3pi66NcCgcCztqyqWg2zLI7SH4dEi+qFXB33UBLjouRNcDcVuJMOgj5htfLw6p8CQQDf8cH7dX3msmTiV+ui+h6EJdYWSWJHExVOVscYVnYGcLLIjsZ8L8FBo8HpvNCzeM5Hlj9L5GaGfj87Z7ZZMSYzAkEAgJpyC3vsUFzKuvYZL3y33OtxssLEUF6bryWxcXMCil0s9vmJVCyJ4iWkGsSPfCiCu6fbTvLR09e4NZxJvP7F1wJBALo3bfaLXIXZEcqlEWwUUAvS6pLdsrsnQzBBOI0kWEBCtmfPUX0yuKC8ayLRvzl92wnsBfY1lot/r1TWiOrh08ECQDDfVu3c2eA2bjTQb5DcYA+YJ7pAYBp547ImVVsOaFP99Mlsi29ko5NC9hCNI2RtA+j7CzM9pMhjjmb1+exSL/0=";
	//支付宝公钥,是默认的值，不是从网上拿来的
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private String fee;
	//服务器生成的订单号
	private String tradeId ;
	@InjectView(R.id.product_price)
	TextView price;
	@InjectView(R.id.product_subject)
	TextView name;
	@InjectView(R.id.product_des)
	TextView des;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(AliPayActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					Intent it = new Intent(AliPayActivity.this,CartActivity.class);
					it.putExtra("from", "money");
					startActivity(it);
					AliPayActivity.this.finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(AliPayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(AliPayActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(AliPayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		ButterKnife.inject(this);
		Intent intent = getIntent();
		fee = intent.getStringExtra("money");
		tradeId = intent.getStringExtra("tradeNum");
		price.setText(fee+"￥");
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		// 订单
//		String orderInfo = getOrderInfo("鲜花","鲜花，送女神男神", String.valueOf(fee));
		/**
		 * 测试使用
		 */
		String orderInfo = getOrderInfo("倬脉","倬脉", fee);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(AliPayActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(AliPayActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + tradeId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "115.29.205.20:8899/pay/alipay_notify.do"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
