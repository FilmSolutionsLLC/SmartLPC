package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.service.ReportService;
import com.fps.service.util.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by macbookpro on 2/15/17.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService{

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);


    public List<Report> getReportQueries() {
        log.info("Getting Report Queries");
        final String sql = "select * from reports";
        List<Report> reports = new ArrayList<>();

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
            Report report = new Report();
            report.setId((Long) row.get("id"));
            report.setName((String) row.get("name"));
            report.setQuery((String) row.get("query"));
            report.setReport_type((Integer) row.get("report_type"));
            reports.add(report);
            log.info(report.toString());
        }
        return reports;

    }
}
