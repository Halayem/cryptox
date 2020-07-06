package fr.enix.model.business.input;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class AddOrderInputTest {

    @Test
    public void testPriceShouldBeNullWhenNotBuilt() {
        assertNull(AddOrderInput.builder().build().getPrice());
    }
}
