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

import org.thinkit.bot.filater.command.FileDeleteCommand;
import org.thinkit.bot.filater.config.FileDeleteConfig;
import org.thinkit.bot.filater.result.FileDeleteResult;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The {@code Filater} is an object that manages various commands for file
 * deletion, and allows you to delete files according to the rules defined
 * through {@code Filater} .
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(staticName = "newInstance")
public final class Filater implements FileDeleter {

    @Override
    public FileDeleteResult executeFileDelete(@NonNull final FileDeleteConfig fileDeleteConfig) {
        return FileDeleteCommand.from(fileDeleteConfig).execute();
    }
}