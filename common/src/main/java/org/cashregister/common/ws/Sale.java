package org.cashregister.common.ws;



/**
 * Created by derkhumblet on 16/12/15.
 */
//@XmlRootElement
public class Sale {
    private String testString;

    public Sale() { }

    public Sale(String testString) {
        this.testString = testString;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "testString='" + testString + '\'' +
                '}';
    }
}
