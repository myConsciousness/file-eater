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

package org.thinkit.bot.filater.batch.data.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * The entity that manages file delete rule.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Data
@Document("file_delete_rule")
public final class FileDeleteRule implements Serializable {

    /**
     * The id
     */
    @Id
    @Indexed(unique = true)
    private String id;

    /**
     * The directory path
     */
    private String directoryPath;

    /**
     * The extension
     */
    private String extension;

    /**
     * The period days
     */
    private int periodDays;

    /**
     * The created datetime
     */
    private Date createdAt;

    /**
     * The updated datetime
     */
    private Date updatedAt;
}
