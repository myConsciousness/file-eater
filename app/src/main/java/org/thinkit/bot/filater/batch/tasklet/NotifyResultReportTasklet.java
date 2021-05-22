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

package org.thinkit.bot.filater.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.api.line.factory.LineApiJFactory;
import org.thinkit.bot.filater.batch.catalog.VariableName;
import org.thinkit.bot.filater.batch.report.LineMessageBuilder;
import org.thinkit.bot.filater.batch.result.BatchTaskResult;
import org.thinkit.bot.filater.catalog.TaskType;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The tasklet manages operations of notify result report step.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class NotifyResultReportTasklet extends AbstractTasklet {

    /**
     * The constructor.
     */
    private NotifyResultReportTasklet() {
        super(TaskType.NOTIFY_RESULT_REPORT);
    }

    /**
     * Returns the new instance of {@link NotifyResultReportTasklet} .
     *
     * @return The new instance of {@link NotifyResultReportTasklet} .
     */
    public static Tasklet newInstance() {
        return new NotifyResultReportTasklet();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        LineApiJFactory.getInstance().createLineNotify(this.getLineNotifyToken())
                .sendMessage(LineMessageBuilder.from(super.getMongoCollections()).build());

        BatchTaskResult.BatchTaskResultBuilder batchTaskResultBuilder = BatchTaskResult.builder();
        batchTaskResultBuilder.actionCount(1);
        batchTaskResultBuilder.resultCount(1);

        log.debug("END");
        return batchTaskResultBuilder.build();
    }

    /**
     * Returns the line notify token.
     *
     * @return The Line Notify token
     */
    private String getLineNotifyToken() {
        return super.getVariableValue(VariableName.LINE_NOTIFY_TOKEN);
    }
}
