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

package org.thinkit.bot.filater.batch.dto;

import java.io.Serializable;

import org.thinkit.bot.filater.batch.data.repository.ActionRecordRepository;
import org.thinkit.bot.filater.batch.data.repository.ErrorRepository;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteResultRepository;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteRuleRepository;
import org.thinkit.bot.filater.batch.data.repository.LastActionRepository;
import org.thinkit.bot.filater.batch.data.repository.VariableRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages collections of MongoDB.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoCollections implements Serializable {

    /**
     * The file delete rule repository
     */
    @Getter
    private FileDeleteRuleRepository fileDeleteRuleRepository;

    /**
     * The last action repository
     */
    @Getter
    private LastActionRepository lastActionRepository;

    /**
     * The action record repository
     */
    @Getter
    private ActionRecordRepository actionRecordRepository;

    /**
     * The error repository
     */
    @Getter
    private ErrorRepository errorRepository;

    /**
     * The variable repository
     */
    @Getter
    private VariableRepository variableRepository;

    /**
     * The file delete result repository
     */
    @Getter
    private FileDeleteResultRepository fileDeleteResultRepository;
}
