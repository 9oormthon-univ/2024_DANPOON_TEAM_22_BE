package naeilmolae.domain.voicefile.domain;

public enum AnalysisResultStatus {
    INCLUDE_INAPPROPRIATE_CONTENT("INCLUDE_INAPPROPRIATE_CONTENT"), // 부적절한 내용 포함
    NOT_READ_VOICE("NOT_READ_VOICE"), // 음성을 그대로 읽지 않음
    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    private final String description;

    AnalysisResultStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 입력된 문자열과 매칭되는 enum을 반환하는 메서드
    public static AnalysisResultStatus fromString(String description) {
        for (AnalysisResultStatus status : AnalysisResultStatus.values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + description);
    }

}
