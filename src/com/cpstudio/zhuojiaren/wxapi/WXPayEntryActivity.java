package com.cpstudio.zhuojiaren.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cpstudio.zhuojiaren.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        handleIntent(getIntent());
    }
    private void handleIntent(Intent intent) {
		SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		int result = 0;
		switch (resp.getType()) {
		case ConstantsAPI.COMMAND_PAY_BY_WX:
			if (resp.errCode==ErrCode.ERR_OK) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT);
				builder.setTitle("支付结果");
				builder.setMessage("支付成功");
				builder.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								WXPayEntryActivity.this.finish();
							}
						}).create().show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT);
				builder.setTitle("支付结果");
				builder.setMessage("支付失败");
				builder.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								WXPayEntryActivity.this.finish();
							}
						}).create().show();
			}
			break;
		default:
			result = R.string.errcode_unknown;
			finish();
			break;
		}
		System.out.println(result);
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();
		}
	}
}