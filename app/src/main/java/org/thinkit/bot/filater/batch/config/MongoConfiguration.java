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
import org.thinkit.bot.filater.batch.data.repository.FileDeleteRuleRepository;
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

    @Override
    protected String getDatabaseName() {
        return MongoDatabase.FILATER.getTag();
    }

    /**
     * The bean that returns the mongo collections.
     *
     * @return The mongo collections.
     */
    @Bean
    public MongoCollections mongoCollections() {
        final MongoCollections.MongoCollectionsBuilder mongoCollectionsBuilder = MongoCollections.builder();
        mongoCollectionsBuilder.fileDeleteRuleRepository(this.fileDeleteRuleRepository);

        return mongoCollectionsBuilder.build();
    }
}
