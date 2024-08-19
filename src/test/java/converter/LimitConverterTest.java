package converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shekhovtsov.converter.LimitConverter;
import ru.shekhovtsov.dto.LimitDto;
import ru.shekhovtsov.model.Limit;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitConverterTest {

    private LimitConverter limitConverter;

    @BeforeEach
    public void setUp() {
        limitConverter = new LimitConverter();
    }

    @Test
    public void testEntityToDto() {
        Limit limit = new Limit(1L, 1L, BigDecimal.valueOf(10000.00));
        LimitDto limitDto = limitConverter.entityToDto(limit);
        assertEquals(limit.getClientId(), limitDto.getClientId());
        assertEquals(limit.getDailyLimit(), limitDto.getDailyLimit());
    }

    @Test
    public void testDtoToEntity() {
        LimitDto limitDto = new LimitDto(1L, BigDecimal.valueOf(10000.00));
        Limit limit = limitConverter.dtoToEntity(limitDto);
        assertEquals(limitDto.getClientId(), limit.getClientId());
        assertEquals(limitDto.getDailyLimit(), limit.getDailyLimit());
    }
}