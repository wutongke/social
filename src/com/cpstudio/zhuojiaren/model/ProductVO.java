package com.cpstudio.zhuojiaren.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductVO implements Parcelable {
	private String id;
	private String title;
	private String _desc;
	private String _value;
	private String addtime;
	private String username;
	private String uheader;
	private String userid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String get_desc() {
		return _desc;
	}

	public void set_desc(String _desc) {
		this._desc = _desc;
	}

	public String get_value() {
		return _value;
	}

	public void set_value(String _value) {
		this._value = _value;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUheader() {
		return uheader;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<ProductVO> CREATOR = new Creator<ProductVO>() {
		public ProductVO createFromParcel(Parcel source) {
			ProductVO productVO = new ProductVO();
			productVO.title = source.readString();
			productVO._desc = source.readString();
			productVO._value = source.readString();
			productVO.username = source.readString();
			productVO.uheader = source.readString();
			productVO.userid = source.readString();
			productVO.id = source.readString();
			productVO.addtime = source.readString();
			return productVO;
		}

		public ProductVO[] newArray(int size) {
			return new ProductVO[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(_desc);
		dest.writeString(_value);
		dest.writeString(username);
		dest.writeString(uheader);
		dest.writeString(userid);
		dest.writeString(id);
		dest.writeString(addtime);
	}

}
