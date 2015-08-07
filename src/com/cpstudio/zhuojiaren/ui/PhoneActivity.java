package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.util.Util;

public class PhoneActivity extends BaseActivity {
	@InjectView(R.id.ap_phone_bang_image)
	ImageView phoneBang;
	@InjectView(R.id.ap_phone_number)
	TextView phoneNumber;
	@InjectView(R.id.ap_phone_box)
	Button phoneBox;
	@InjectView(R.id.ap_update_phone)
	Button updatePhone;
	@InjectView(R.id.ap_phone_des)
	TextView des;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.bang_phone);
		TextView goBack = (TextView)findViewById(R.id.activity_back);
		goBack.setText(R.string.account_and_security);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		phoneNumber.setVisibility(View.VISIBLE);
		ResHelper resHelper = ResHelper.getInstance(mContext);
		phoneNumber.setText(resHelper.getUserid());
		updatePhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,TextEditActivity.class);
				intent.putExtra("edtiText", "");
				startActivityForResult(intent, 1);
			}
		});
		phoneBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,  
                        ContactsContract.Contacts.CONTENT_URI);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1&&requestCode==RESULT_OK){
			 Uri contactData = data.getData();  
             Cursor cursor = managedQuery(contactData, null, null, null,  
                     null);  
             cursor.moveToFirst();  
             String num = getContactPhone(cursor);  
			if(Util.isMobileNum(num))
			phoneNumber.setText(getResources().getString(R.string.your_phone)+num);
			else{
				Util.toastMessage((Activity)mContext, getResources().getString(R.string.invalid_phone));
			}
		}
	}
	private String getContactPhone(Cursor cursor) {  
        // TODO Auto-generated method stub  
        int phoneColumn = cursor  
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);  
        int phoneNum = cursor.getInt(phoneColumn);  
        String result = "";  
        if (phoneNum > 0) {  
            // 获得联系人的ID号  
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);  
            String contactId = cursor.getString(idColumn);  
            // 获得联系人电话的cursor  
            Cursor phone = getContentResolver().query(  
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                    null,  
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="  
                            + contactId, null, null);  
            if (phone.moveToFirst()) {  
                for (; !phone.isAfterLast(); phone.moveToNext()) {  
                    int index = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);  
                    int typeindex = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);  
                    int phone_type = phone.getInt(typeindex);  
                    String phoneNumber = phone.getString(index);  
                    result = phoneNumber;  
//                  switch (phone_type) {//此处请看下方注释  
//                  case 2:  
//                      result = phoneNumber;  
//                      break;  
//  
//                  default:  
//                      break;  
//                  }  
                }  
                if (!phone.isClosed()) {  
                    phone.close();  
                }  
            }  
        }  
        return result;  
    }  
}
