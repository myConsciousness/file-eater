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

@Configuration
public class BatchStepConfiguration {

    /**
     * The step builder factory
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * The delete file tasklet
     */
    @Autowired
    private Tasklet deleteFileTasklet;

    /**
     * The notify result report tasklet
     */
    @Autowired
    private Tasklet notifyResultReportTasklet;

    @Bean
    public BatchStepCollections batchStepCollections() {
        final BatchStepCollections.BatchStepCollectionsBuilder batchStepCollectionsBuilder = BatchStepCollections
                .builder();
        batchStepCollectionsBuilder.deleteFileStep(this.deleteFileStep());
        batchStepCollectionsBuilder.deleteFileStep(this.notifyResultReportStep());

        return batchStepCollectionsBuilder.build();
    }

    private Step deleteFileStep() {
        return this.stepBuilderFactory.get(BatchStep.DELETE_FILE.getTag()).tasklet(this.deleteFileTasklet).build();
    }

    private Step notifyResultReportStep() {
        return this.stepBuilderFactory.get(BatchStep.NOTIFY_RESULT_REPORT.getTag())
                .tasklet(this.notifyResultReportTasklet).build();
    }
}
