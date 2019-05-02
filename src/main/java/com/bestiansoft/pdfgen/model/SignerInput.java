package com.bestiansoft.pdfgen.model;

// import javax.persistence.Id;
// import javax.persistence.ManyToOne;

// @Entity
public class SignerInput {
    // @Id 
    private String id;

    // @ManyToOne
    private Element elem;

    private String textInput;

    private byte[] signInput;



    /**
     * @return the textInput
     */
    public String getTextInput() {
        return textInput;
    }

    /**
     * @param textInput the textInput to set
     */
    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }

    /**
     * @return the signInput
     */
    public byte[] getSignInput() {
        return signInput;
    }

    /**
     * @param signInput the signInput to set
     */
    public void setSignInput(byte[] signInput) {
        this.signInput = signInput;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the elem
     */
    public Element getElem() {
        return elem;
    }

    /**
     * @param elem the elem to set
     */
    public void setElem(Element elem) {
        this.elem = elem;
    }



}
