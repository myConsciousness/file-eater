/*
 * Copyright 2021 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.bot.filater.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.thinkit.bot.filater.Filater;
import org.thinkit.bot.filater.FileDeleter;
import org.thinkit.bot.filater.batch.catalog.BatchJob;
import org.thinkit.bot.filater.batch.dto.BatchStepCollections;

/**
 * The class that defines the configuration of a batch job process and schedule.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
public class BatchJobConfiguration {

    /**
     * The schedule cron
     */
    private static final String SCHEDULE_CRON = "${spring.batch.schedule.cron}";

    /**
     * The timezone
     */
    private static final String TIME_ZONE = "${spring.batch.schedule.timezone}";

    /**
     * The job builder factory
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * The job launcher
     */
    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    /**
     * The batch step collections
     */
    @Autowired
    private BatchStepCollections batchStepCollections;

    /**
     * Registers the instance of {@link FileDeleter} as bean.
     *
     * @return The instance of {@link FileDeleter}
     */
    @Bean
    public FileDeleter fileDeleter() {
        return Filater.newInstance();
    }

    /**
     * Performs the process of scheduled file deletion.
     *
     * @throws Exception When an error occurs in the scheduled file deletion process
     */
    @Scheduled(cron = SCHEDULE_CRON, zone = TIME_ZONE)
    public void performScheduleFileDeletion() throws Exception {
        this.runJobLauncher();
    }

    /**
     * Runs the job launcher.
     *
     * @throws Exception When an error occurs in the scheduled file deletion process
     */
    private void runJobLauncher() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString(BatchJob.FILATER_BOT.getTag(), String.valueOf(System.currentTimeMillis())).toJobParameters();

        this.simpleJobLauncher.run(this.createFilaterBotJob(), param);
    }

    /**
     * Returns the batch job of Filater bot.
     *
     * @return The batch job of Filater bot
     */
    private Job createFilaterBotJob() {
        return this.createDeleteFileJobFlowBuilder().end().build();
    }

    /**
     * Returns the job flow object of delete file process.
     *
     * @return The job flow object of delete file process
     */
    private FlowBuilder<FlowJobBuilder> createDeleteFileJobFlowBuilder() {
        return this.getFilaterBotJobBuilder().flow(this.batchStepCollections.getExecuteDeleteFileStep())
                .next(this.batchStepCollections.getNotifyResultReportStep())
                .next(this.batchStepCollections.getCloseSessionStep());
    }

    /**
     * Returns the job builder of Filater bot.
     *
     * @return The job builder of Filater bot
     */
    private JobBuilder getFilaterBotJobBuilder() {
        return this.jobBuilderFactory.get(BatchJob.FILATER_BOT.getTag());
    }
}
