package com.classicinvoice.app.models;

import com.classicinvoice.app.models.GoodsItem;

import java.util.ArrayList;

public class Invoice {

    String invoiceNumber, supplierAddress, supplierGst, recipientAddress,recipientGst;

    String consigneeAddress, ewayNumber, vehicleNumber, date;

    double cgst, sgst, igst, totalTaxableValue, freight;

    ArrayList<GoodsItem> item;

    public String getInvoiceNumber() {
        return invoiceNumber != null ? invoiceNumber : "";
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSupplierAddress() {
        return supplierAddress != null ? supplierAddress : "";
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierGst() {
        return supplierGst != null ? supplierGst : "9     9     9     9     9     9     9     9     9     9     9     9     9     9     9";
    }

    public void setSupplierGst(String supplierGst) {
        this.supplierGst = supplierGst;
    }

    public String getRecipientAddress() {
        return recipientAddress != null ? recipientAddress : "";
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientGst() {
        return recipientGst != null ? recipientGst : "9     9     9     9     9     9     9     9     9     9     9     9     9     9     9";
    }

    public void setRecipientGst(String recipientGst) {
        this.recipientGst = recipientGst;
    }

    public String getConsigneeAddress() {
        return consigneeAddress != null ? consigneeAddress : "";
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getEwayNumber() {
        return ewayNumber != null ? ewayNumber : "";
    }

    public void setEwayNumber(String ewayNumber) {
        this.ewayNumber = ewayNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber != null ? vehicleNumber : "";
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public ArrayList<GoodsItem> getItem() {
        return item;
    }

    public void setItem(ArrayList<GoodsItem> item) {
        this.item = item;
    }

    public double getTotalTaxableValue() {

        if (item != null) {

            for (int i = 0; i < item.size(); i++) {

                totalTaxableValue = totalTaxableValue + item.get(i).getTaxableValue();
            }
        }

        return totalTaxableValue;
    }
}
