package fr.enix.model.business.input;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
 class AddOrderInputTest {

    @Test
    void testPriceShouldBeNullWhenNotBuilt() {
        assertNull(AddOrderInput.builder().build().getPrice());
    }
}
