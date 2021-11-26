package pl.kj.bachelors.planning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import pl.kj.bachelors.planning.application.Application;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = { Application.class })
class ApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

}
