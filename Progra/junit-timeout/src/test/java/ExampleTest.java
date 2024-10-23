import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest {
    @Test
    void test_reports_timeout_incorrectly() {
        assertThat(Example.answer()).isEqualTo(21);
    }
    
    @Test
    void test_correct_message() {
        assertThat(new int[]{Example.answer()}).contains(21);
    }
}
