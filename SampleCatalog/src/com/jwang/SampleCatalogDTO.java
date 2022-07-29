package com.jwang;
	

public class SampleCatalogDTO implements java.lang.Comparable<SampleCatalogDTO> {
	private long productId;
	private String description;
	private String category;
	private String unitOfMeasure;
	private Double unitPrice;
	private Double discountPercent;
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
		return this.productId + " " + this.description + " " + this.category + " " + this.unitOfMeasure + " " 
	                                + this.unitPrice + " " + this.discountPercent;
	}
}
