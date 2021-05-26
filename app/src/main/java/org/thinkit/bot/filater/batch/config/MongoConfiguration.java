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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.thinkit.bot.filater.batch.catalog.MongoDatabase;
import org.thinkit.bot.filater.batch.data.repository.ActionRecordRepository;
import org.thinkit.bot.filater.batch.data.repository.ErrorRepository;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteResultRepository;
import org.thinkit.bot.filater.batch.data.repository.FileDeleteRuleRepository;
import org.thinkit.bot.filater.batch.data.repository.LastActionRepository;
import org.thinkit.bot.filater.batch.data.repository.VariableRepository;
import org.thinkit.bot.filater.batch.dto.MongoCollections;

/**
 * The configuration class for MongoDB.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    /**
     * The file delete rule repository
     */
    @Autowired
    private FileDeleteRuleRepository fileDeleteRuleRepository;

    /**
     * The last action repository
     */
    @Autowired
    private LastActionRepository lastActionRepository;

    /**
     * The action record repository
     */
    @Autowired
    private ActionRecordRepository actionRecordRepository;

    /**
     * The error repository
     */
    @Autowired
    private ErrorRepository errorRepository;

    /**
     * The variable repository
     */
    @Autowired
    private VariableRepository variableRepository;

    /**
     * The file delete result repository
     */
    @Autowired
    private FileDeleteResultRepository fileDeleteResultRepository;

    @Override
    protected String getDatabaseName() {
        return MongoDatabase.FILATER.getTag();
    }

    /**
     * Registers the instance of {@link MongoCollections} as bean
     *
     * @return The instance of {@link MongoCollections}
     */
    @Bean
    public MongoCollections mongoCollections() {
        final MongoCollections.MongoCollectionsBuilder mongoCollectionsBuilder = MongoCollections.builder();
        mongoCollectionsBuilder.fileDeleteRuleRepository(this.fileDeleteRuleRepository);
        mongoCollectionsBuilder.lastActionRepository(this.lastActionRepository);
        mongoCollectionsBuilder.actionRecordRepository(this.actionRecordRepository);
        mongoCollectionsBuilder.errorRepository(this.errorRepository);
        mongoCollectionsBuilder.variableRepository(this.variableRepository);
        mongoCollectionsBuilder.fileDeleteResultRepository(this.fileDeleteResultRepository);

        return mongoCollectionsBuilder.build();
    }
}
