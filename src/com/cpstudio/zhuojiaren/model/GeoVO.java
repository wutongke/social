package com.cpstudio.zhuojiaren.model;

public class GeoVO {
	private String status;
	private ResultVO result = new ResultVO();
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ResultVO getResult() {
		return result;
	}

	public void setResult(ResultVO result) {
		this.result = result;
	}

	public class ResultVO{
		private LocationVO location;
		private String formatted_address;
		private String business;
		private AddressComponentVO addressComponent;
		private int cityCode;
		public LocationVO getLocation() {
			return location;
		}
		public void setLocation(LocationVO location) {
			this.location = location;
		}
		public String getFormatted_address() {
			return formatted_address;
		}
		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}
		public String getBusiness() {
			return business;
		}
		public void setBusiness(String business) {
			this.business = business;
		}
		public AddressComponentVO getAddressComponent() {
			return addressComponent;
		}
		public void setAddressComponent(AddressComponentVO addressComponent) {
			this.addressComponent = addressComponent;
		}
		public int getCityCode() {
			return cityCode;
		}
		public void setCityCode(int cityCode) {
			this.cityCode = cityCode;
		}
	}
    
	public class LocationVO{
    	private double lng;
    	private double lat;
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
    }
    
	public class AddressComponentVO{
    	private String city;
    	private String district;
    	private String province;
    	private String street;
    	private String street_number;
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getStreet_number() {
			return street_number;
		}
		public void setStreet_number(String street_number) {
			this.street_number = street_number;
		}
    }
}
