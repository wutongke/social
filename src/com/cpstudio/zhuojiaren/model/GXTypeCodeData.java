package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.List;

/**
 * 基本编码数据(除了)
 * 
 * @author lz
 * 
 */
public class GXTypeCodeData implements Serializable {
	private List<GXTypeItemVO> supply;
	private List<GXTypeItemVO> demand;
	public List<GXTypeItemVO> getSupply() {
		return supply;
	}
	public void setSupply(List<GXTypeItemVO> supply) {
		this.supply = supply;
	}
	public List<GXTypeItemVO> getDemand() {
		return demand;
	}
	public void setDemand(List<GXTypeItemVO> demand) {
		this.demand = demand;
	}
	
}