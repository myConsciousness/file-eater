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
import org.thinkit.bot.filater.Filater;
import org.thinkit.bot.filater.FileDeleter;
import org.thinkit.bot.filater.batch.data.entity.FileDeleteRule;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteRuleRepository;
import org.thinkit.bot.filater.batch.result.BatchTaskResult;
import org.thinkit.bot.filater.catalog.TaskType;
import org.thinkit.bot.filater.config.FileDeleteConfig;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The tasklet manages operations related to the delete file command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Slf4j
@ToString
@EqualsAndHashCode(callSuper = false)
public final class ExecuteFileDeleteTasklet extends AbstractTasklet {

    /**
     * The constructor.
     */
    private ExecuteFileDeleteTasklet() {
        super(TaskType.DELETE_FILE);
    }

    /**
     * Returns the new instance of {@link ExecuteFileDeleteTasklet} .
     *
     * @return The new instance of {@link ExecuteFileDeleteTasklet}
     */
    public static Tasklet newInstance() {
        return new ExecuteFileDeleteTasklet();
    }

    @Override
    protected BatchTaskResult executeTask(StepContribution contribution, ChunkContext chunkContext) {
        log.debug("START");

        final FileDeleter fileDeleter = Filater.newInstance();

        final FileDeleteRuleRepository fileDeleteRuleRepository = super.getMongoCollections()
                .getFileDeleteRuleRepository();
        final List<FileDeleteRule> fileDeleteRules = fileDeleteRuleRepository.findAll();

        for (final FileDeleteRule fileDeleteRule : fileDeleteRules) {
            fileDeleter.executeFileDelete(this.getFileDeleteConfig(fileDeleteRule));
        }

        log.debug("END");
        return null;
    }

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
