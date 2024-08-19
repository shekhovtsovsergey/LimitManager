package ru.shekhovtsov.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shekhovtsov.converter.LimitConverter;
import ru.shekhovtsov.dao.LimitRepository;
import ru.shekhovtsov.dto.LimitDto;
import ru.shekhovtsov.model.Limit;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private static final BigDecimal DEFAULT_DAILY_LIMIT = BigDecimal.valueOf(10000.00);
    private static final Logger logger = LoggerFactory.getLogger(LimitServiceImpl.class);
    private final LimitRepository limitRepository;
    private final LimitConverter limitConverter;

    @Override
    public LimitDto getLimit(Long clientId) {
        logger.debug("Получение лимита для клиента с ID: {}", clientId);
        Optional<Limit> optionalLimit = limitRepository.findByClientId(clientId);
        if (optionalLimit.isPresent()) {
            logger.debug("Лимит найден: {}", optionalLimit.get());
            return limitConverter.entityToDto(optionalLimit.get());
        } else {
            logger.error("Лимит для клиента с ID {} не найден.", clientId);
            throw new IllegalArgumentException("Лимит не найден для клиента с ID: " + clientId);
        }
    }

    @Override
    @Transactional
    public LimitDto reduceLimit(Long clientId, BigDecimal amount) {
        logger.debug("Уменьшение лимита для клиента с ID: {} на сумму: {}", clientId, amount);
        Limit limit = limitRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Лимит не найден для клиента с ID: " + clientId));
        limit.setDailyLimit(limit.getDailyLimit().subtract(amount));
        Limit updatedLimit = limitRepository.save(limit);
        logger.debug("Лимит обновлен: {}", updatedLimit);
        return limitConverter.entityToDto(updatedLimit);
    }

    @Override
    @Transactional
    public LimitDto restoreLimit(Long clientId, BigDecimal amount) {
        logger.debug("Восстановление лимита для клиента с ID: {} на сумму: {}", clientId, amount);
        Limit limit = limitRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Лимит не найден для клиента с ID: " + clientId));
        limit.setDailyLimit(limit.getDailyLimit().add(amount));
        Limit updatedLimit = limitRepository.save(limit);
        logger.debug("Лимит обновлен: {}", updatedLimit);
        return limitConverter.entityToDto(updatedLimit);
    }

    @Override
    @Transactional
    //@Scheduled(cron = "0 * * * * ?")  //раз в минуту
    @Scheduled(cron = "0 0 0 * * ?") //ночью
    public void resetLimits() {
        logger.info("Сброс всех лимитов до значения: {}", DEFAULT_DAILY_LIMIT);
        limitRepository.resetAllDailyLimits(DEFAULT_DAILY_LIMIT);
        logger.info("Лимиты восстановлены");
    }

    @Override
    public LimitDto createLimit(LimitDto limitDto) {
        logger.debug("Создание нового лимита: {}", limitDto);
        Limit limit = limitConverter.dtoToEntity(limitDto);
        Limit savedLimit = limitRepository.save(limit);
        logger.debug("Лимит сохранен: {}", savedLimit);
        return limitConverter.entityToDto(savedLimit);
    }

    @Override
    public LimitDto updateLimit(Long clientId, LimitDto limitDto) {
        logger.debug("Обновление лимита для клиента с ID: {} на значение: {}", clientId, limitDto);
        Limit limit = limitRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Лимит не найден для клиента с ID: " + clientId));
        limit.setDailyLimit(limitDto.getDailyLimit());
        Limit updatedLimit = limitRepository.save(limit);
        logger.debug("Лимит обновлен: {}", updatedLimit);
        return limitConverter.entityToDto(updatedLimit);
    }

    @Override
    public void deleteLimit(Long clientId) {
        logger.debug("Удаление лимита для клиента с ID: {}", clientId);
        limitRepository.deleteByClientId(clientId);
        logger.info("Лимит для клиента с ID {} удален.", clientId);
    }
}
