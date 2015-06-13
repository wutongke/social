package com.cpstudio.zhuojiaren.model;

public class ImageRadioButton {

	int aImage;
	int bImage;
	int type;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aImage;
		result = prime * result + bImage;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageRadioButton other = (ImageRadioButton) obj;
		if (aImage != other.aImage)
			return false;
		if (bImage != other.bImage)
			return false;
		return true;
	}
	public ImageRadioButton(int aImage, int bImage) {
		super();
		this.aImage = aImage;
		this.bImage = bImage;
	}
	public int getaImage() {
		return aImage;
	}
	public void setaImage(int aImage) {
		this.aImage = aImage;
	}
	public int getbImage() {
		return bImage;
	}
	public void setbImage(int bImage) {
		this.bImage = bImage;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
