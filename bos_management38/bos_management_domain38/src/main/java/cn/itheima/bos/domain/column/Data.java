package cn.itheima.bos.domain.column;

import java.math.BigDecimal;

public class Data {
	private String name;
	private BigDecimal y;
	private String drilldown;
	
	public BigDecimal getY() {
		return y;
	}
	public void setY(BigDecimal bigDecimal) {
		this.y = bigDecimal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDrilldown() {
		return drilldown;
	}
	public void setDrilldown(String drilldown) {
		this.drilldown = drilldown;
	}
	
}
