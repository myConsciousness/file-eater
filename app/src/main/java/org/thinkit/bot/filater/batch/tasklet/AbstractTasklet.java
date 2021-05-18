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

import java.util.Date;
import java.util.List;

import com.mongodb.lang.NonNull;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.thinkit.bot.filater.batch.data.entity.ActionRecord;
import org.thinkit.bot.filater.batch.data.entity.Error;
import org.thinkit.bot.filater.batch.data.entity.LastAction;
import org.thinkit.bot.filater.batch.data.repository.ErrorRepository;
import org.thinkit.bot.filater.batch.data.repository.LastActionRepository;
import org.thinkit.bot.filater.batch.dto.MongoCollections;
import org.thinkit.bot.filater.batch.policy.BatchTask;
import org.thinkit.bot.filater.batch.result.BatchTaskResult;
import org.thinkit.bot.filater.catalog.TaskType;
import org.thinkit.bot.filater.result.ActionError;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Component
public abstract class AbstractTasklet implements Tasklet {

    /**
     * The batch task
     */
    private final BatchTask batchTask;

    /**
     * The configurable application context
     */
    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * The mongo collections
     */
    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private MongoCollections mongoCollections;

    /**
     * The constructor.
     *
     * @param taskType The task type
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    protected AbstractTasklet(@NonNull final TaskType taskType) {
        this.batchTask = BatchTask.from(taskType);
    }

    /**
     * Given the current context in the form of a step contribution, do whatever is
     * necessary to process this unit inside a transaction.
     *
     * <p>
     * Implementations return {@link RepeatStatus#FINISHED} if finished. If not they
     * return {@link RepeatStatus#CONTINUABLE}. On failure throws an exception.
     *
     * @param contribution The mutable state to be passed back to update the current
     *                     step execution
     * @param chunkContext The attributes shared between invocations but not between
     *                     restarts
     * @return The batch task result
     */
    protected abstract BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return this.executeTaskProcess(contribution, chunkContext);
    }

    private RepeatStatus executeTaskProcess(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        this.updateStartAction();

        final BatchTaskResult batchTaskResult = this.executeTask(contribution, chunkContext);
        log.debug("The batch task result: {}", batchTaskResult);

        final int actionCount = batchTaskResult.getActionCount();
        final List<ActionError> actionErrors = batchTaskResult.getActionErrors();

        this.saveActionRecord(actionCount);

        if (!actionErrors.isEmpty()) {
            this.saveActionError(actionErrors);
        }

        this.updateEndAction();

        log.debug("END");
        return batchTaskResult.getRepeatStatus();
    }

    private void saveActionRecord(final int actionCount) {
        log.debug("START");

        final ActionRecord actionRecord = new ActionRecord();
        actionRecord.setTaskTypeCode(this.batchTask.getTypeCode());
        actionRecord.setCount(actionCount);

        this.mongoCollections.getActionRecordRepository().insert(actionRecord);
        log.debug("Inserted action record: {}", actionRecord);

        log.debug("END");
    }

    private void saveActionError(@NonNull final List<ActionError> actionErrors) {
        log.debug("START");

        final ErrorRepository errorRepository = this.mongoCollections.getErrorRepository();

        for (final ActionError actionError : actionErrors) {
            Error error = new Error();
            error.setTaskTypeCode(actionError.getTaskType().getCode());
            error.setMessage(actionError.getMessage());
            error.setLocalizedMessage(actionError.getLocalizedMessage());
            error.setStackTrace(actionError.getStackTrace());

            error = errorRepository.insert(error);
            log.debug("Inserted error: {}", error);
        }

        log.debug("END");
    }

    private void updateStartAction() {
        log.debug("START");

        final LastActionRepository lastActionRepository = this.mongoCollections.getLastActionRepository();
        LastAction lastAction = lastActionRepository.findByTaskTypeCode(this.batchTask.getTypeCode());

        if (lastAction == null) {
            lastAction = new LastAction();
            lastAction.setTaskTypeCode(this.batchTask.getTypeCode());
        }

        lastAction.setStart(new Date());
        lastAction.setEnd(null);
        lastAction.setUpdatedAt(new Date());

        lastActionRepository.save(lastAction);
        log.debug("Updated last action: {}", lastAction);

        log.debug("END");
    }

    private void updateEndAction() {
        log.debug("START");

        final LastActionRepository lastActionRepository = this.mongoCollections.getLastActionRepository();
        final LastAction lastAction = lastActionRepository.findByTaskTypeCode(this.batchTask.getTypeCode());

        lastAction.setEnd(new Date());
        lastAction.setUpdatedAt(new Date());

        lastActionRepository.save(lastAction);
        log.debug("Updated last action: {}", lastAction);

        log.debug("END");
    }
}
