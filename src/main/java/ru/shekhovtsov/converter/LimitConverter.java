package ru.shekhovtsov.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.shekhovtsov.dto.LimitDto;
import ru.shekhovtsov.model.Limit;

@Component
@RequiredArgsConstructor
public class LimitConverter {

    public LimitDto entityToDto(Limit limit) {
        return new LimitDto(limit.getClientId(), limit.getDailyLimit());
    }

    public Limit dtoToEntity(LimitDto limitDto) {
        Limit limit = new Limit();
        limit.setClientId(limitDto.getClientId());
        limit.setDailyLimit(limitDto.getDailyLimit());
        return limit;
    }
}