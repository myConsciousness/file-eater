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

package org.thinkit.bot.filater.batch.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thinkit.bot.filater.batch.data.entity.FileDeleteResult;

/**
 * The ineterface that manages file delete result repository.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Repository
public interface FileDeleteResultRepository extends MongoRepository<FileDeleteResult, String> {

    /**
     * Returns the list of file delete result based on the task type code and
     * execute datetime passed as arguments.
     *
     * @param taskTypeCode The task type code
     * @param executedAt   The executed datetime
     * @return The list of file delete result
     */
    public List<FileDeleteResult> findByTaskTypeCodeAndExecutedAt(int taskTypeCode, String executedAt);
}
