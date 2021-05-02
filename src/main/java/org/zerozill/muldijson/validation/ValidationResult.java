package org.zerozill.muldijson.validation;

public class ValidationResult {

    private boolean same;
    private String detail;

    public ValidationResult() {
        same = true;
        detail = null;
    }

    public ValidationResult(boolean same, String detail) {
        this.same = same;
        this.detail = detail;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public boolean isSame() {
        return same;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "ValidationResult {\n" +
                "The Json string is the same? : " + same + "\n" +
                "Detail:" + detail +
                '}';
    }
}
