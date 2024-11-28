package naeilmolae.domain.voicefile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static naeilmolae.domain.voicefile.domain.QProvidedFile.providedFile;
import static naeilmolae.domain.voicefile.domain.QVoiceFile.voiceFile;

@RequiredArgsConstructor
public class ProvidedFileRepositoryImpl implements ProvidedFileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, List<Long> alarmId, Pageable pageable) {
        // Fetch data with pagination
        List<ProvidedFile> content = queryFactory
                .selectFrom(providedFile)
                .join(providedFile.voiceFile, voiceFile).fetchJoin() // Join VoiceFile with fetch join
                .where(
                        voiceFile.memberId.eq(memberId), // memberId filtering
                        voiceFile.alarmId.in(alarmId) // alarmId filtering
                )
                .offset(pageable.getOffset()) // Apply pagination offset
                .limit(pageable.getPageSize()) // Apply pagination limit
                .fetch();

        // Count query for total elements
        Long total = queryFactory
                .select(providedFile.count())
                .from(providedFile)
                .join(providedFile.voiceFile, voiceFile)
                .where(
                        voiceFile.memberId.eq(memberId),
                        voiceFile.alarmId.in(alarmId)
                )
                .fetchOne();

        // Return Page object
        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    @Override
    public Page<ProvidedFile> findByMemberId(Long memberId, Pageable pageable) {
        List<ProvidedFile> content = queryFactory
                .selectFrom(providedFile)
                .join(providedFile.voiceFile, voiceFile).fetchJoin() // Join VoiceFile with fetch join
                .where(
                        voiceFile.memberId.eq(memberId) // memberId filtering
                )
                .offset(pageable.getOffset()) // Apply pagination offset
                .limit(pageable.getPageSize()) // Apply pagination limit
                .fetch();

        // Count query for total elements
        Long total = queryFactory
                .select(providedFile.count())
                .from(providedFile)
                .join(providedFile.voiceFile, voiceFile)
                .where(
                        voiceFile.memberId.eq(memberId)
                )
                .fetchOne();

        // Return Page object
        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}