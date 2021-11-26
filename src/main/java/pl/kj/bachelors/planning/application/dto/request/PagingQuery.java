package pl.kj.bachelors.planning.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonNaming(value = PropertyNamingStrategies.KebabCaseStrategy.class)
public class PagingQuery {
    private int page = 0;
    @JsonProperty("page-size")
    private int pageSize = 100;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
