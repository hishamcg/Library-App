package com.strata.justbooksclc.model;

public class Book {
	private String title;
	private String author;
	private String publisher;
	private String price;
	private String isbn;
	private String category;
	private String title_id;
	private String rental_id;
	private String times_rented;
	private String avg_reading;
	private String image_url;
	private String summary;
	private String pickup_order;
	private String delivery_order_id;
	
	private Boolean allow_cancel;
	private String disallow_cancel_reason;
	private String order_type;
	private String status;
	
	
	public String getDelivery_order_id() {
		return delivery_order_id;
	}
	public void setDelivery_order_id(String delivery_order_id) {
		this.delivery_order_id = delivery_order_id;
	}
	public Boolean getAllow_cancel() {
		return allow_cancel;
	}
	public void setAllow_cancel(Boolean allow_cancel) {
		this.allow_cancel = allow_cancel;
	}
	public String getDisallow_cancel_reason() {
		return disallow_cancel_reason;
	}
	public void setDisallow_cancel_reason(String disallow_cancel_reason) {
		this.disallow_cancel_reason = disallow_cancel_reason;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPickup_order() {
		return pickup_order;
	}
	public void setPickup_order(String pickup_order) {
		this.pickup_order = pickup_order;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTimes_rented() {
		return times_rented;
	}
	public void setTimes_rented(String times_rented) {
		this.times_rented = times_rented;
	}
	public String getAvg_reading() {
		return avg_reading;
	}
	public void setAvg_reading(String avg_reading) {
		this.avg_reading = avg_reading;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getId() {
		return title_id;
	}
	public void setId(String title_id) {
		this.title_id = title_id;
	}
	public String getRental_id() {
		return rental_id;
	}
	public void setRental_id(String rental_id) {
		this.rental_id = rental_id;
	}
	

}
