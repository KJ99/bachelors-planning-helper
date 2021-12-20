package pl.kj.bachelors.planning.domain.model.config;

public class PdfColumnConfig {
    private String fieldName;
    private String displayName;
    private float relativeWidth;
    private int order;

    public PdfColumnConfig(String fieldName, String displayName, float relativeWidth, int order) {
        this.fieldName = fieldName;
        this.displayName = displayName;
        this.relativeWidth = relativeWidth;
        this.order = order;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public int getOrder() {
        return order;
    }
}
