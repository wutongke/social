package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class ProductNewVO {
	private String productid;

	private String comid;

	private String product;

	private String description;

	private String customer;

	private String value;

	private List<PicNewVO> productPic;

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getProductid() {
		return this.productid;
	}

	public void setComid(String comid) {
		this.comid = comid;
	}

	public String getComid() {
		return this.comid;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProduct() {
		return this.product;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setProductPic(List<PicNewVO> productPic) {
		this.productPic = productPic;
	}

	public List<PicNewVO> getProductPic() {
		return this.productPic;
	}

}
