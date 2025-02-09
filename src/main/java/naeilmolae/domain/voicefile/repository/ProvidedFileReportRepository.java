package naeilmolae.domain.voicefile.repository;

import naeilmolae.domain.voicefile.domain.ProvidedFileReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvidedFileReportRepository extends JpaRepository<ProvidedFileReport, Long> {
}
