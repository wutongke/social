/**
 * ShoppingCartActivity.java
 *
 * Created by xuanzhui on 2015/7/29.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package com.cpstudio.zhuojiaren.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCPayPalSyncObserver;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCBillOrder;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;

import com.cpstudio.zhuojiaren.R;


public class ShoppingCartActivity extends Activity {
    private static final String TAG = "ShoppingCartActivity";

    Button btnQueryOrders;

    private ProgressDialog loadingDialog;
    private ListView payMethod;

    int money;
    String number;
    
    //֧������������
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {
            final BCPayResult bcPayResult = (BCPayResult)bcResult;
            //�˴��ر�loading����
            loadingDialog.dismiss();

            //�������Լ���������֧�����
            //��Ҫע����ǣ��˴�����漰��UI�ĸ��£�����UI�����̻���Handler����
            ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String result = bcPayResult.getResult();

                    /*
                      ע�⣡
                      ����֧�����������Է���˵�״̬���Ϊ׼���˴����ص�RESULT_SUCCESS���������ֻ���֧���ɹ�
                    */
                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                        Toast.makeText(ShoppingCartActivity.this, "�û�֧���ɹ�", Toast.LENGTH_LONG).show();

                    } else if (result.equals(BCPayResult.RESULT_CANCEL))
                        Toast.makeText(ShoppingCartActivity.this, "�û�ȡ��֧��", Toast.LENGTH_LONG).show();
                    else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        String toastMsg = "֧��ʧ��, ԭ��: " + bcPayResult.getErrCode() +
                                " # " + bcPayResult.getErrMsg() +
                                " # " + bcPayResult.getDetailInfo();

                        /**
                         * �㷢������Ŀ�в�Ӧ�ó������´��󣬴˴�����֧��������ԭ��
                         * �����ṩ֧����֧���Ĳ��Թ��ܣ����Ը�����ʾ˵��
                         */
                        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
                                bcPayResult.getDetailInfo().startsWith("֧��������")) {
                            toastMsg = "֧��ʧ�ܣ�����֧��������ԭ�򣬹ʲ����ṩ֧����֧���Ĳ��Թ��ܣ����������Ĳ��㣬�����½�";
                        }

                        /**
                         * �������������̣��밴�账��ʧ����Ϣ
                         */

                        Toast.makeText(ShoppingCartActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, toastMsg);

                        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED) ||
                                bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NEED_UPGRADE)) {
                        }

                    } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                        //���ܳ�����֧����8000����״̬
                        Toast.makeText(ShoppingCartActivity.this, "����״̬δ֪", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ShoppingCartActivity.this, "invalid return", Toast.LENGTH_LONG).show();
                    }

                    if (bcPayResult.getId() != null) {
                        //����԰����id�浽��Ķ����У��´�ֱ��ͨ�����id��ѯ����
                        Log.w(TAG, "bill id retrieved : " + bcPayResult.getId());

                        //����ID��ѯ
                        getBillInfoByID(bcPayResult.getId());
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        
        money = getIntent().getIntExtra("money", 0);
        number = getIntent().getStringExtra("number");
        float moneyShow = money/100f;
        ((TextView)findViewById(R.id.money)).setText(""+moneyShow);
        // �Ƽ�����Activity���onCreate�����г�ʼ��BeeCloud.
        BeeCloud.setAppIdAndSecret("dcf459df-8e65-4d3a-ba9f-2423e33e780e",
                "ff3946f0-235f-42b7-8a84-95405fc75f0e");

        // ����õ�΢��֧�������õ�΢��֧����Activity��onCreate������������º���.
        // �ڶ���������Ҫ�������Լ���΢��AppID.
        String initInfo = BCPay.initWechatPay(ShoppingCartActivity.this, "wx95c80614da3d5a6a");
        if (initInfo != null) {
            Toast.makeText(this, "΢�ų�ʼ��ʧ�ܣ�" + initInfo, Toast.LENGTH_LONG).show();
        }

        // ���ʹ��PayPal��Ҫ��֧��֮ǰ����client id��Ӧ��secret
        // BCPay.PAYPAL_PAY_TYPE.SANDBOX���ڲ��ԣ�BCPay.PAYPAL_PAY_TYPE.LIVE������������
        //���һ��������ʾ�Ƿ���paypal֧��ҳ����ʾ�ջ���ַ�������ַ���Ϸ��п�������޷�֧��
        BCPay.initPayPal("AVT1Ch18aTIlUJIeeCxvC7ZKQYHczGwiWm8jOwhrREc4a5FnbdwlqEB4evlHPXXUA67RAAZqZM0H8TCR",
                "EL-fkjkEUyxrwZAmrfn46awFXlX-h2nRkyCVhhpeVdlSRuhPJKXx3ZvUTTJqPQuAeomXA8PZ2MkX24vF",
                BCPay.PAYPAL_PAY_TYPE.SANDBOX, Boolean.FALSE);

        payMethod = (ListView) this.findViewById(R.id.payMethod);
        Integer[] payIcons = new Integer[]{R.drawable.wechat, R.drawable.alipay
                };
        final String[] payNames = new String[]{"΢��֧��", "֧����֧��"};
        String[] payDescs = new String[]{"ʹ��΢��֧�����������CNY�Ʒ�", "ʹ��֧����֧�����������CNY�Ʒ�"};
        PayMethodListItem adapter = new PayMethodListItem(this, payIcons, payNames, payDescs);
        payMethod.setAdapter(adapter);

        // �������֧��̫��, ���������￪������, ��progressdialogΪ��
        loadingDialog = new ProgressDialog(ShoppingCartActivity.this);
        loadingDialog.setMessage("����������֧�������Ժ�...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);

        payMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0: //΢��
                        loadingDialog.show();
                        //����΢��֧��, �ֻ��ڴ�̫С����OutOfResourcesException��ɵĿ���, �����޷����֧��
                        //�����΢��������ڵ�����
                        Map<String, String> mapOptional = new HashMap<String, String>();
                        mapOptional.put("�ͻ���", "��׿");

                        if (BCPay.isWXAppInstalledAndSupported() &&
                                BCPay.isWXPaySupported()) {

                            BCPay.getInstance(ShoppingCartActivity.this).reqWXPaymentAsync(
                                    "پ��֧��",               //��������
                                    money,                           //�������(��)
                                    number,  //������ˮ��
                                    mapOptional,            //��չ����(����null)
                                    bcCallback);            //֧����ɺ�ص����

                        } else {
                            Toast.makeText(ShoppingCartActivity.this,
                                    "����δ��װ΢�Ż��߰�װ��΢�Ű汾��֧��", Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();
                        }
                        break;

                    case 1: //֧����֧��
                        loadingDialog.show();

                        mapOptional = new HashMap<String, String>();
                        mapOptional.put("�ͻ���", "��׿");
                        mapOptional.put("money", money+"");

                        BCPay.getInstance(ShoppingCartActivity.this).reqAliPaymentAsync(
                                "پ��֧��",
                                money,                           //�������(��)
                                number,
                                mapOptional,
                                bcCallback);

                        break;

                    case 2: //����֧��
                        loadingDialog.show();

                        /*  �����ͨ�����·�������֧��������PayParams�ķ�ʽ
                        BCPay.getInstance(ShoppingCartActivity.this).reqUnionPaymentAsync("����֧������",
                                1,
                                BillUtils.genBillNum(),
                                null,
                                bcCallback);*/


                        BCPay.PayParams payParam = new BCPay.PayParams();

                        payParam.channelType = BCReqParams.BCChannelTypes.UN_APP;

                        //��Ʒ����, 32���ֽ���, ������2���ֽڼ�
                        payParam.billTitle = "��׿����֧������";

                        //֧�����Է�Ϊ��λ��������������
                        payParam.billTotalFee = 1;

                        //�̻��Զ��嶩����
                        payParam.billNum = BillUtils.genBillNum();

                        BCPay.getInstance(ShoppingCartActivity.this).reqPaymentAsync(payParam,
                                bcCallback);


                        break;
                    case 3: //ͨ���ٶ�Ǯ��֧��
                        loadingDialog.show();

                        mapOptional = new HashMap<String, String>();
                        mapOptional.put("goods desc", "��Ʒ��ϸ����");

                        Map<String, String> analysis;

                        //ͨ������PayParam�ķ�ʽ����֧��
                        //��Ҳ����ͨ��reqBaiduPaymentAsync�ķ�ʽ֧��
                        //BCPay.PayParam
                        payParam = new BCPay.PayParams();
                        /*
                        *  ֧���������˴��԰ٶ�Ǯ��Ϊ����ʵ��֧������
                        *  BCReqParams.BCChannelTypes.WX_APP��
                        *  BCReqParams.BCChannelTypes.ALI_APP��
                        *  BCReqParams.BCChannelTypes.UN_APP��
                        *  BCReqParams.BCChannelTypes.BD_APP��
                        *  BCReqParams.BCChannelTypes.PAYPAL_SANDBOX��
                        *  BCReqParams.BCChannelTypes.PAYPAL_LIVE
                        */
                        payParam.channelType = BCReqParams.BCChannelTypes.BD_APP;

                        //��Ʒ����, 32���ֽ���, ������2���ֽڼ�
                        payParam.billTitle = "��׿BaiduǮ��֧������";

                        //֧�����Է�Ϊ��λ��������������
                        payParam.billTotalFee = 1;

                        //�̻��Զ��嶩����
                        payParam.billNum = BillUtils.genBillNum();

                        //������ʱʱ�䣬����Ϊ��λ�����鲻С��360�����Բ�����
                        payParam.billTimeout = 360;

                        //��չ���������Դ�������������key/value���������ҵ���߼������󣬿��Բ�����
                        payParam.optional = mapOptional;

                        //��չ���������ں��ڷ�����Ŀǰֻ֧��keyΪcategory�ķ�����������Բ�����
                        analysis = new HashMap<String, String>();
                        analysis.put("category", "BD");
                        payParam.analysis = analysis;

                        BCPay.getInstance(ShoppingCartActivity.this).reqPaymentAsync(payParam,
                                bcCallback);
                        break;
                    case 4: //ͨ��PayPal֧��
                        /*
                         ����PayPal��ÿһ��֧����sdk���Զ�����������ͬ����
                         ���������ͬ��ʧ�ܣ���¼�ᱻ�Զ����棬��ʱ����Ե���batchSyncPayPalPayment�����ֶ�ͬ��
                         ��Ȼ��������Ƚ��٣����ǽ���ο�PayPalUnSyncedListActivity����ͬ����������������޷����ĵ�����
                         */
                        loadingDialog.show();

                        HashMap<String, String> hashMapOptional = new HashMap<String, String>();
                        hashMapOptional.put("PayPal key1", "PayPal value1");
                        hashMapOptional.put("PayPal key2", "PayPal value2");

                        BCPay bcPay = BCPay.getInstance(ShoppingCartActivity.this);

                        //this is only required if you want to get the bill id
                        //or you want to know the sync result
                        bcPay.addPayPalSyncObserver(new BCPayPalSyncObserver(){

                            @Override
                            public void onSyncSucceed(String id) {
                                Log.w(TAG, "paypal bill id retrieved: " + id);
                                getBillInfoByID(id);
                            }

                            @Override
                            public boolean onSyncFailed(String billInfo, String failInfo) {
                                Log.w(TAG, "billInfo: " + billInfo +
                                    "#failInfo: "+failInfo);
                                //return true if you have successfully dealt with the billInfo
                                //else sdk will continue to store the un-synced billInfo into cache for later sync
                                return false;
                            }
                        });

                        BCPay.getInstance(ShoppingCartActivity.this).reqPayPalPaymentAsync(
                                "PayPal payment test",  //bill title
                                1,                      //bill amount(use cents)
                                "USD",                  //bill currency
                                hashMapOptional,        //optional info
                                bcCallback);
                        break;
                    default:
                }
            }
        });

        btnQueryOrders = (Button) findViewById(R.id.btnQueryOrders);
        btnQueryOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    void getBillInfoByID(String id) {

        BCQuery.getInstance().queryBillByIDAsync(id,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        BCQueryBillResult billResult = (BCQueryBillResult) result;

                        Log.d(TAG, "------ response info ------");
                        Log.d(TAG, "------getResultCode------" + billResult.getResultCode());
                        Log.d(TAG, "------getResultMsg------" + billResult.getResultMsg());
                        Log.d(TAG, "------getErrDetail------" + billResult.getErrDetail());

                        Log.d(TAG, "------- bill info ------");
                        BCBillOrder billOrder = billResult.getBill();
                        Log.d(TAG, "������:" + billOrder.getBillNum());
                        Log.d(TAG, "�������, ��λΪ��:" + billOrder.getTotalFee());
                        Log.d(TAG, "��������:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getChannel()));
                        Log.d(TAG, "����������:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getSubChannel()));

                        Log.d(TAG, "�����Ƿ�ɹ�:" + billOrder.getPayResult());

                        if (billOrder.getPayResult())
                            Log.d(TAG, "�������صĽ��׺ţ�δ֧���ɹ�ʱ���ǲ����ò�����:" + billOrder.getTradeNum());
                        else
                            Log.d(TAG, "�����Ƿ񱻳������ò����������²�Ʒ�������ά���ɨ��֧������Ч:"
                                    + billOrder.getRevertResult());

                        Log.d(TAG, "��������ʱ��:" + new Date(billOrder.getCreatedTime()));
                        Log.d(TAG, "��չ����:" + billOrder.getOptional());
                        Log.w(TAG, "�����Ƿ��Ѿ��˿�ɹ�(���ں��ڲ�ѯ): " + billOrder.getRefundResult());
                        Log.w(TAG, "�������ص���ϸ��Ϣ�����账��: " + billOrder.getMessageDetail());

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //����ǰ��activity����
        BCPay.clear();

        //ʹ��΢�ŵģ���initWechatPay��activity����ʱdetach
        BCPay.detachWechat();

        //ʹ�ðٶ�֧���ģ���activity����ʱdetach
        BCPay.detachBaiduPay();
    }
}
