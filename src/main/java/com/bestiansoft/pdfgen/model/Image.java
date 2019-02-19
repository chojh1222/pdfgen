package com.bestiansoft.pdfgen.model;

public class Image {
    int signId;
    String userId;
    String signtype;
    String filename;
    String encImg;

    public Image(int signId, String userId, String signtype, String filename, String encImg) {
        this.signId = signId;
        this.userId = userId;
        this.signtype = signtype;
        this.filename = filename;
        this.encImg = encImg;
    }

    /**
     * @return the signId
     */
    public int getSignId() {
        return signId;
    }

    /**
     * @param signId the signId to set
     */
    public void setSignId(int signId) {
        this.signId = signId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the signtype
     */
    public String getSigntype() {
        return signtype;
    }

    /**
     * @param signtype the signtype to set
     */
    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the encImg
     */
    public String getEncImg() {
        return encImg;
    }

    /**
     * @param encImg the encImg to set
     */
    public void setEncImg(String encImg) {
        this.encImg = encImg;
    }

    
}