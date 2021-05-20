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
 * The entity that manages file delete result.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@Data
@Document("file_delete_result")
public final class FileDeleteResult implements Serializable {

    /**
     * The id
     */
    @Id
    @Indexed(unique = true)
    private String id;

    /**
     * The task type code
     */
    @Indexed
    private int taskTypeCode;

    /**
     * The count
     */
    private int count;

    /**
     * The size
     */
    private int size;

    /**
     * The executed date
     */
    private String executedAt;

    /**
     * The created datetime
     */
    private Date createdAt;

    /**
     * The updated datetime
     */
    private Date updatedAt;
}
