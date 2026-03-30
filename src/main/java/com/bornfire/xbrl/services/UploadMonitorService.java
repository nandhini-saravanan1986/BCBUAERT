package com.bornfire.xbrl.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.xbrl.entities.UploadMonitorEntity;
import com.bornfire.xbrl.entities.UploadMonitorRepository;

@Service
public class UploadMonitorService {

    @Autowired
    private UploadMonitorRepository uploadMonitorRepository;

    @Transactional
    public UploadMonitorEntity startUpload(String reportType, Date reportDate, Date fromDate, MultipartFile file, String username,
            boolean forceUpload) {
        UploadMonitorEntity monitor = new UploadMonitorEntity();
        monitor.setUploadId(UUID.randomUUID().toString());
        monitor.setReportType(reportType);
        monitor.setReportDate(reportDate);
        monitor.setFromDate(fromDate);
        monitor.setFileName(file != null ? file.getOriginalFilename() : null);
        monitor.setFileFormat(file != null ? resolveFileFormat(file.getOriginalFilename()) : null);
        monitor.setFileSizeBytes(file != null ? file.getSize() : null);
        monitor.setUploadStatus("IN_PROGRESS");
        monitor.setStartedAt(new Date());
        monitor.setUploadedBy(username);
        monitor.setIsReupload(forceUpload ? "Y" : "N");
        return uploadMonitorRepository.save(monitor);
    }

    @Transactional
    public void completeSuccess(String uploadId, String message, Long total, Long loaded, Long failed) {
        Optional<UploadMonitorEntity> optional = uploadMonitorRepository.findById(uploadId);
        if (!optional.isPresent()) {
            return;
        }

        UploadMonitorEntity monitor = optional.get();
        Date ended = new Date();
        monitor.setEndedAt(ended);
        monitor.setDurationMs(calculateDuration(monitor.getStartedAt(), ended));
        monitor.setUploadStatus("SUCCESS");
        monitor.setErrorSummary(sanitize(message, 1000));
        monitor.setErrorDetails(sanitize(message, 4000));
        monitor.setRecordsTotal(total != null ? total : 0L);
        monitor.setRecordsLoaded(loaded != null ? loaded : 0L);
        monitor.setRecordsFailed(failed != null ? failed : 0L);
        uploadMonitorRepository.save(monitor);

        markPreviousAsReplaced(monitor);
    }

    @Transactional
    public void completeFailure(String uploadId, String errorMessage) {
        Optional<UploadMonitorEntity> optional = uploadMonitorRepository.findById(uploadId);
        if (!optional.isPresent()) {
            return;
        }

        UploadMonitorEntity monitor = optional.get();
        Date ended = new Date();
        monitor.setEndedAt(ended);
        monitor.setDurationMs(calculateDuration(monitor.getStartedAt(), ended));
        monitor.setUploadStatus("FAILED");
        monitor.setErrorSummary(sanitize(errorMessage, 1000));
        monitor.setErrorDetails(sanitize(errorMessage, 4000));
        monitor.setRecordsTotal(monitor.getRecordsTotal() == null ? 0L : monitor.getRecordsTotal());
        monitor.setRecordsLoaded(monitor.getRecordsLoaded() == null ? 0L : monitor.getRecordsLoaded());
        monitor.setRecordsFailed(monitor.getRecordsFailed() == null ? 0L : monitor.getRecordsFailed());
        uploadMonitorRepository.save(monitor);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSummary(String reportType, Date reportDate, Date fromDate, Date toDate) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        Optional<UploadMonitorEntity> latest;
        if (reportDate != null) {
            latest = uploadMonitorRepository.findTopByReportTypeAndReportDateOrderByStartedAtDesc(reportType, reportDate);
        } else {
            latest = uploadMonitorRepository.findTopByReportTypeOrderByStartedAtDesc(reportType);
        }

        result.put("reportType", reportType);
        result.put("latestUpload", latest.orElse(null));
        result.put("windowLoadedCount", 0L);

        if (fromDate != null && toDate != null) {
            Long totalLoaded = uploadMonitorRepository.sumLoadedByReportTypeAndStartedAtWindow(reportType, fromDate, toDate);
            result.put("windowLoadedCount", totalLoaded != null ? totalLoaded : 0L);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getHistory(String reportType, Date reportDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UploadMonitorEntity> historyPage;

        if (reportDate != null) {
            historyPage = uploadMonitorRepository.findByReportTypeAndReportDateOrderByStartedAtDesc(reportType, reportDate, pageable);
        } else {
            historyPage = uploadMonitorRepository.findByReportTypeOrderByStartedAtDesc(reportType, pageable);
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("content", historyPage.getContent());
        response.put("page", historyPage.getNumber());
        response.put("size", historyPage.getSize());
        response.put("totalElements", historyPage.getTotalElements());
        response.put("totalPages", historyPage.getTotalPages());
        return response;
    }

    @Transactional(readOnly = true)
    public UploadMonitorEntity getDetails(String uploadId) {
        return uploadMonitorRepository.findById(uploadId).orElse(null);
    }

    private void markPreviousAsReplaced(UploadMonitorEntity current) {
        if (current == null || current.getReportType() == null || current.getReportDate() == null) {
            return;
        }

        List<UploadMonitorEntity> previousRows = uploadMonitorRepository.findPreviousSuccessfulForSameReportDate(
                current.getReportType(),
                current.getReportDate(),
                current.getUploadId(),
                PageRequest.of(0, 1));

        if (previousRows == null || previousRows.isEmpty()) {
            return;
        }

        UploadMonitorEntity previous = previousRows.get(0);
        previous.setUploadStatus("REPLACED");
        previous.setReplacedUploadId(current.getUploadId());
        uploadMonitorRepository.save(previous);

        current.setIsReupload("Y");
        uploadMonitorRepository.save(current);
    }

    private String resolveFileFormat(String fileName) {
        if (fileName == null) {
            return null;
        }
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".xlsx") || lower.endsWith(".xls")) {
            return "EXCEL";
        }
        if (lower.endsWith(".txt") || lower.endsWith(".csv") || lower.endsWith(".dat")) {
            return "TEXT";
        }
        return "UNKNOWN";
    }

    private long calculateDuration(Date start, Date end) {
        if (start == null || end == null) {
            return 0L;
        }
        return Math.max(0L, end.getTime() - start.getTime());
    }

    private String sanitize(String raw, int maxLength) {
        if (raw == null) {
            return null;
        }
        String masked = raw.replaceAll("(?i)password\\s*[:=]\\s*\\S+", "password=***");
        if (masked.length() <= maxLength) {
            return masked;
        }
        return masked.substring(0, maxLength);
    }

    /**
     * Report dates with a successful upload in {@link UploadMonitorEntity} (e.g. GAM / EAB flows
     * without a dedicated staging table list).
     */
    @Transactional(readOnly = true)
    public List<String> getSuccessfulUploadDatesForReportType(String reportType) {
        if (reportType == null || reportType.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Date> dates = uploadMonitorRepository
                .findDistinctSuccessfulReportDatesByReportType(reportType.trim());
        if (dates == null || dates.isEmpty()) {
            return new ArrayList<>();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return dates.stream().filter(Objects::nonNull).map(sdf::format).collect(Collectors.toList());
    }
}
