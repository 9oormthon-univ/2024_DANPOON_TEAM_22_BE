package naeilmolae.domain.alarm.converter;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component

public class StringToAlarmCategoryConverter implements Converter<String, AlarmCategory> {
    @Override
    public AlarmCategory convert(String source) {
        return AlarmCategory.valueOf(source.toUpperCase());
    }
}
