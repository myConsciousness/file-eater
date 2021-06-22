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

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thinkit.bot.filater.batch.catalog.BatchStep;
import org.thinkit.bot.filater.batch.dto.BatchStepCollections;

/**
 * The class that defines the configuration of a batch step process and
 * schedule.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
public class BatchStepConfiguration {

    /**
     * The step builder factory
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * The execute delete file tasklet
     */
    @Autowired
    private Tasklet executeDeleteFileTasklet;

    /**
     * The notify result report tasklet
     */
    @Autowired
    private Tasklet notifyResultReportTasklet;

    /**
     * The close session tasklet
     */
    @Autowired
    private Tasklet closeSessionTasklet;

    /**
     * Registers the instance of {@link BatchStepCollections} as bean.
     *
     * @return The instance of {@link BatchStepCollections}
     */
    @Bean
    public BatchStepCollections batchStepCollections() {
        final BatchStepCollections.BatchStepCollectionsBuilder batchStepCollectionsBuilder = BatchStepCollections
                .builder();
        batchStepCollectionsBuilder.executeDeleteFileStep(this.executeDeleteFileStep());
        batchStepCollectionsBuilder.notifyResultReportStep(this.notifyResultReportStep());
        batchStepCollectionsBuilder.closeSessionStep(this.closeSessionStep());

        return batchStepCollectionsBuilder.build();
    }

    /**
     * Returns the delete file step
     *
     * @return The delete file step
     */
    private Step executeDeleteFileStep() {
        return this.stepBuilderFactory.get(BatchStep.EXECUTE_DELETE_FILE.getTag())
                .tasklet(this.executeDeleteFileTasklet).build();
    }

    /**
     * Returns the noify result report step
     *
     * @return The notify result report step
     */
    private Step notifyResultReportStep() {
        return this.stepBuilderFactory.get(BatchStep.NOTIFY_RESULT_REPORT.getTag())
                .tasklet(this.notifyResultReportTasklet).build();
    }

    /**
     * Returns the close session step
     *
     * @return The close session step
     */
    private Step closeSessionStep() {
        return this.stepBuilderFactory.get(BatchStep.CLOSE_SESSION.getTag()).tasklet(this.closeSessionTasklet).build();
    }
}
