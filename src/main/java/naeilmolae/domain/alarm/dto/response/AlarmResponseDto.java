package naeilmolae.domain.alarm.dto.response;

public class AlarmResponseDto {
    public Long alarmId;
    public String title;

    public AlarmResponseDto(Long alarmId, String title) {
        this.alarmId = alarmId;
        this.title = title;
    }
}
