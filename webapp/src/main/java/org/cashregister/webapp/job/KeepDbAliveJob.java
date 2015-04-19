package org.cashregister.webapp.job;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.persistence.api.TransactionRepository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by derkhumblet on 19/01/15.
 */
public class KeepDbAliveJob extends QuartzJobBean {
    private static final Logger LOG = LoggerFactory.getLogger(KeepDbAliveJob.class);
    @SpringBean private TransactionRepository transactionRepository;
    private static final DateFormat DF = new SimpleDateFormat("");

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Calendar cal = Calendar.getInstance();
        Date until = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        Date from = cal.getTime();
        long count = transactionRepository.countTransactions(from, until, -1);
        LOG.info("STATS Between {} and {} there have been {} transactions.", new String[] {DF.format(from), DF.format(until), String.valueOf(count)});
    }
}
