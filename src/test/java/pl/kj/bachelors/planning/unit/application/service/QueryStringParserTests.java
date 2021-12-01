package pl.kj.bachelors.planning.unit.application.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.codehaus.jackson.annotate.JsonProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.application.deserializer.QueryStringValueToBooleanConverter;
import pl.kj.bachelors.planning.application.service.QueryStringParser;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryStringParserTests extends BaseUnitTest {
    @Autowired
    private QueryStringParser service;

    @Test
    public void testParse_AllKeyVals() {
        String value = "foo=bar&abc=123";

        Map<String, Object> result = this.service.parse(URI.create(String.format("localhost?%s", value)));

        assertThat(result).hasSize(2);
        assertThat(result).containsKey("foo");
        assertThat(result).containsKey("abc");
        assertThat(result.get("foo")).isEqualTo("bar");
        assertThat(result.get("abc")).isEqualTo("123");
    }

    @Test
    public void testParse_EmptyWithEqualMark() {
        String value = "foo=bar&abc=";

        Map<String, Object> result = this.service.parse(URI.create(String.format("localhost?%s", value)));

        assertThat(result).hasSize(2);
        assertThat(result).containsKey("foo");
        assertThat(result).containsKey("abc");
        assertThat(result.get("foo")).isEqualTo("bar");
        assertThat(result.get("abc")).isEqualTo("");
    }

    @Test
    public void testParse_KeyOnly() {
        String value = "foo=bar&abc";

        Map<String, Object> result = this.service.parse(URI.create(String.format("localhost?%s", value)));

        assertThat(result).hasSize(2);
        assertThat(result).containsKey("foo");
        assertThat(result).containsKey("abc");
        assertThat(result.get("foo")).isEqualTo("bar");
        assertThat(result.get("abc")).isEqualTo("");
    }

    @Test
    public void testParse_WithModel() {
        String value = "foo=bar&abc&page-size=3";
        URI uri = URI.create(String.format("localhost?%s", value));

        Model result = this.service.parse(uri, Model.class);

        assertThat(result.foo).isNotEmpty();
        assertThat(result.abc).isTrue();
        assertThat(result.pageSize).isEqualTo(3);
    }


    @JsonNaming(value = PropertyNamingStrategies.KebabCaseStrategy.class)
    public static class Model {
        private String foo;
        @JsonDeserialize(using = QueryStringValueToBooleanConverter.class)
        private boolean abc;
        @JsonProperty("page-size")
        private Integer pageSize;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public boolean isAbc() {
            return abc;
        }

        public void setAbc(boolean abc) {
            this.abc = abc;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }
    }
}
