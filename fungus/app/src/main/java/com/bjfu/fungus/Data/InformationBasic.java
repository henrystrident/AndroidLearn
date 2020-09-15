package com.bjfu.fungus.Data;

import org.litepal.crud.LitePalSupport;

public class InformationBasic extends LitePalSupport {
    private String environmentImages, wildFormImages, labImages, cultivateImages, collectNumber,
            date, collector, labelCode, province, city, country,
            address, latitude, longitude,
            altitude, chineseName, scientificName, host,
            growEnvironment, substrate, habit, spore,
            tissue, DNA, category, describe, saveToLocal="false", uploaded="false";

    public String getEnvironmentImages() {
        return environmentImages;
    }

    public void setEnvironmentImages(String environmentImages) {
        this.environmentImages = environmentImages;
    }

    public String getWildFormImages() {
        return wildFormImages;
    }

    public void setWildFormImages(String wildFormImages) {
        this.wildFormImages = wildFormImages;
    }

    public String getLabImages() {
        return labImages;
    }

    public void setLabImages(String labImages) {
        this.labImages = labImages;
    }

    public String getCultivateImages() {
        return cultivateImages;
    }

    public void setCultivateImages(String cultivateImages) {
        this.cultivateImages = cultivateImages;
    }

    public String getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(String collectNumber) {
        this.collectNumber = collectNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGrowEnvironment() {
        return growEnvironment;
    }

    public void setGrowEnvironment(String growEnvironment) {
        this.growEnvironment = growEnvironment;
    }

    public String getSubstrate() {
        return substrate;
    }

    public void setSubstrate(String substrate) {
        this.substrate = substrate;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getSpore() {
        return spore;
    }

    public void setSpore(String spore) {
        this.spore = spore;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getDNA() {
        return DNA;
    }

    public void setDNA(String DNA) {
        this.DNA = DNA;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSaveToLocal() {
        return saveToLocal;
    }

    public void setSaveToLocal(String saveToLocal) {
        this.saveToLocal = saveToLocal;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }
}
