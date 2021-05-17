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

package org.thinkit.bot.filater;

import com.mongodb.lang.NonNull;

import org.thinkit.bot.filater.config.FileDeleteConfig;
import org.thinkit.bot.filater.result.FileDeleteResult;

/**
 * The interface that abstracts an object that deletes files.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
public interface FileDeleter {

    /**
     * Performs file deletion based on the rules set in the configuration object
     * passed as an argument. As a result, it returns an object that contains the
     * number of deleted files and the file size.
     *
     * @param fileDeleteConfig The file delete config
     * @return The file delete result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    public FileDeleteResult executeFileDelete(@NonNull final FileDeleteConfig fileDeleteConfig);
}
