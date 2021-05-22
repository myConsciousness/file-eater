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

import java.util.List;

import com.mongodb.lang.NonNull;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.stereotype.Component;
import org.thinkit.bot.filater.FileDeleter;
import org.thinkit.bot.filater.batch.data.entity.FileDeleteResult;
import org.thinkit.bot.filater.batch.data.entity.FileDeleteRule;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteResultRepository;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteRuleRepository;
import org.thinkit.bot.filater.batch.dto.MongoCollections;
import org.thinkit.bot.filater.batch.result.BatchTaskResult;
import org.thinkit.bot.filater.catalog.DateFormat;
import org.thinkit.bot.filater.catalog.TaskType;
import org.thinkit.bot.filater.config.FileDeleteConfig;
import org.thinkit.bot.filater.result.FileDeleteCommandResult;
import org.thinkit.bot.filater.util.DateUtils;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The tasklet manages operations of delete file step.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
@Component
public final class ExecuteDeleteFileTasklet extends AbstractTasklet {

    /**
     * The constructor.
     */
    private ExecuteDeleteFileTasklet() {
        super(TaskType.DELETE_FILE);
    }

    /**
     * Returns the new instance of {@link ExecuteDeleteFileTasklet} .
     *
     * @return The new instance of {@link ExecuteDeleteFileTasklet}
     */
    public static Tasklet newInstance() {
        return new ExecuteDeleteFileTasklet();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        final FileDeleter fileDeleter = super.getFileDeleter();

        final MongoCollections mongoCollections = super.getMongoCollections();
        final FileDeleteRuleRepository fileDeleteRuleRepository = mongoCollections.getFileDeleteRuleRepository();
        final FileDeleteResultRepository fileDeleteResultRepository = mongoCollections.getFileDeleteResultRepository();

        final String executedAt = DateUtils.toString(DateFormat.YYYY_MM_DD, DateUtils.now());
        final List<FileDeleteRule> fileDeleteRules = fileDeleteRuleRepository.findAll();

        for (final FileDeleteRule fileDeleteRule : fileDeleteRules) {
            final FileDeleteCommandResult fileDeleteCommandResult = fileDeleter
                    .executeFileDelete(this.getFileDeleteConfig(fileDeleteRule));

            FileDeleteResult fileDeleteResult = new FileDeleteResult();
            fileDeleteResult.setTaskTypeCode(TaskType.DELETE_FILE.getCode());
            fileDeleteResult.setDirectoryPath(fileDeleteRule.getDirectoryPath());
            fileDeleteResult.setExtension(fileDeleteRule.getExtension());
            fileDeleteResult.setCount(fileDeleteCommandResult.getCount());
            fileDeleteResult.setSize(fileDeleteCommandResult.getSize());
            fileDeleteResult.setExecutedAt(executedAt);

            fileDeleteResult = fileDeleteResultRepository.insert(fileDeleteResult);
            log.debug("Inserted file delete result: {}", fileDeleteResult);
        }

        log.debug("END");
        return BatchTaskResult.builder().build();
    }

    /**
     * Returns the file delete config.
     *
     * @param fileDeleteRule The file delete rule
     * @return The file delete config
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private FileDeleteConfig getFileDeleteConfig(@NonNull final FileDeleteRule fileDeleteRule) {
        log.debug("START");

        final FileDeleteConfig.FileDeleteConfigBuilder fileDeleteConfigBuilder = FileDeleteConfig.builder();
        fileDeleteConfigBuilder.directoryPath(fileDeleteRule.getDirectoryPath());
        fileDeleteConfigBuilder.extension(fileDeleteRule.getExtension());
        fileDeleteConfigBuilder.periodDays(fileDeleteRule.getPeriodDays());

        log.debug("END");
        return fileDeleteConfigBuilder.build();
    }
}
