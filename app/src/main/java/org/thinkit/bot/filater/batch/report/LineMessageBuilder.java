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

package org.thinkit.bot.filater.batch.report;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.thinkit.bot.filater.batch.data.entity.FileDeleteResult;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteResultRepository;
import org.thinkit.bot.filater.batch.dto.MongoCollections;
import org.thinkit.bot.filater.catalog.DateFormat;
import org.thinkit.bot.filater.catalog.TaskType;
import org.thinkit.bot.filater.util.DateUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class LineMessageBuilder implements MessageBuilder {

    /**
     * The mongo collections
     */
    private MongoCollections mongoCollections;

    @Override
    public String build() {

        final FileDeleteResultRepository fileDeleteResultRepository = mongoCollections.getFileDeleteResultRepository();

        final String executedAt = DateUtils.toString(DateFormat.YYYY_MM_DD, DateUtils.now());
        final List<FileDeleteResult> fileDeleteResults = fileDeleteResultRepository
                .findByTaskTypeCodeAndExecutedAt(TaskType.DELETE_FILE.getCode(), executedAt);

        int sumCount = 0;
        long sumSize = 0;
        for (final FileDeleteResult fileDeleteResult : fileDeleteResults) {
            sumCount += fileDeleteResult.getCount();
            sumSize += fileDeleteResult.getSize();
        }

        return """
                File Delete Result (%s)
                ---------------------------
                Deleted File Count: %s
                Deleted File Size: %s
                """.formatted(executedAt, sumCount, FileUtils.byteCountToDisplaySize(sumSize));
    }
}
