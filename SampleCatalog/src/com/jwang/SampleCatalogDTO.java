package com.jwang;
	

public class SampleCatalogDTO implements java.lang.Comparable<SampleCatalogDTO> {
	private long productId;
	private String productName;
	private String productType;
	private String unitOfMeasure;
	private Double unitPrice;
	private Double discountPercent;
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}
    
	/**
	 * Compare price after discount. 
	 * 
	 */
	public int compareTo(SampleCatalogDTO catalog) {
		if ((this.unitPrice.doubleValue() * (1 - this.discountPercent.doubleValue())) 
				> (catalog.getUnitPrice().doubleValue() * (1 - catalog.getDiscountPercent().doubleValue()))) {
			return 1;
		} else if ((this.unitPrice.doubleValue() * (1 - this.discountPercent.doubleValue())) 
				< (catalog.getUnitPrice().doubleValue() * (1 - catalog.getDiscountPercent().doubleValue()))) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public String toString() {
		return this.productId + " " + this.productName + " " + this.productType + " " + this.unitOfMeasure + " " 
	                                + this.unitPrice + " " + this.discountPercent;
	}
}
