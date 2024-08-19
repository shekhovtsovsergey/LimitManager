package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shekhovtsov.converter.LimitConverter;
import ru.shekhovtsov.dao.LimitRepository;
import ru.shekhovtsov.dto.LimitDto;
import ru.shekhovtsov.model.Limit;
import ru.shekhovtsov.service.LimitServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitServiceImplTest {

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private LimitConverter limitConverter;

    @InjectMocks
    private LimitServiceImpl limitService;

    private Limit limit;
    private LimitDto limitDto;

    @BeforeEach
    void setUp() {
        limit = new Limit(1L, 1L, BigDecimal.valueOf(10000.00));
        limitDto = new LimitDto(1L, BigDecimal.valueOf(10000.00));
    }

    @Test
    @DisplayName("Получение лимита успешно")
    void testGetLimit_Success() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.of(limit));
        when(limitConverter.entityToDto(limit)).thenReturn(limitDto);

        LimitDto result = limitService.getLimit(1L);

        assertNotNull(result);
        assertEquals(limitDto, result);
        verify(limitRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Лимит не найден при получении")
    void testGetLimit_NotFound() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            limitService.getLimit(1L);
        });

        assertEquals("Лимит не найден для клиента с ID: 1", exception.getMessage());
        verify(limitRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Создание лимита успешно")
    void testCreateLimit() {
        when(limitConverter.dtoToEntity(limitDto)).thenReturn(limit);
        when(limitRepository.save(limit)).thenReturn(limit);
        when(limitConverter.entityToDto(limit)).thenReturn(limitDto);

        LimitDto result = limitService.createLimit(limitDto);

        assertNotNull(result);
        assertEquals(limitDto, result);
        verify(limitRepository).save(limit);
    }

    @Test
    @DisplayName("Обновление лимита успешно")
    void testUpdateLimit_Success() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.of(limit));
        when(limitConverter.entityToDto(limit)).thenReturn(limitDto);

        limitDto.setDailyLimit(BigDecimal.valueOf(15000.00));
        limit.setDailyLimit(limitDto.getDailyLimit());
        when(limitRepository.save(limit)).thenReturn(limit);

        LimitDto result = limitService.updateLimit(1L, limitDto);

        assertNotNull(result);
        assertEquals(limitDto, result);
        verify(limitRepository).save(limit);
    }

    @Test
    @DisplayName("Лимит не найден при обновлении")
    void testUpdateLimit_NotFound() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            limitService.updateLimit(1L, limitDto);
        });

        assertEquals("Лимит не найден для клиента с ID: 1", exception.getMessage());
        verify(limitRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Удаление лимита успешно")
    void testDeleteLimit() {
        doNothing().when(limitRepository).deleteByClientId(1L);

        limitService.deleteLimit(1L);

        verify(limitRepository).deleteByClientId(1L);
    }

    @Test
    @DisplayName("Лимит не найден при уменьшении")
    void testReduceLimit_NotFound() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            limitService.reduceLimit(1L, BigDecimal.valueOf(5000.00));
        });

        assertEquals("Лимит не найден для клиента с ID: 1", exception.getMessage());
        verify(limitRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Лимит не найден при восстановлении")
    void testRestoreLimit_NotFound() {
        when(limitRepository.findByClientId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            limitService.restoreLimit(1L, BigDecimal.valueOf(5000.00));
        });

        assertEquals("Лимит не найден для клиента с ID: 1", exception.getMessage());
        verify(limitRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Сброс лимитов успешно")
    void testResetLimits() {
        doNothing().when(limitRepository).resetAllDailyLimits(any(BigDecimal.class));

        limitService.resetLimits();

        verify(limitRepository).resetAllDailyLimits(BigDecimal.valueOf(10000.00));
    }
}