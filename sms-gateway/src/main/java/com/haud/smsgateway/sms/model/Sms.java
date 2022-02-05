package com.haud.smsgateway.sms.model;

public class Sms {
	private String senderPhone;
	private String receiverPhone;
	private String text;

	public Sms() {
	}

	public Sms(String senderPhone, String receiverPhone, String text) {
		this.senderPhone = senderPhone;
		this.receiverPhone = receiverPhone;
		this.text = text;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override public String toString() {
		return "Sms{" +
				"senderPhone='" + senderPhone + '\'' +
				", receiverPhone='" + receiverPhone + '\'' +
				", text='" + text + '\'' +
				'}';
	}
}
