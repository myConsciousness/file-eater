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

package org.thinkit.bot.filater.command;

import java.io.File;

import com.mongodb.lang.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.bot.filater.config.FileDeleteConfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class FileDeleteCommand extends AbstractBotCommand<String> {

    /**
     * The file delete config
     */
    private FileDeleteConfig fileDeleteConfig;

    @Override
    public String executeBotProcess() {
        this.deleteFileRecursively(new File(this.fileDeleteConfig.getDirectoryPath()));
        return "";
    }

    private void deleteFileRecursively(@NonNull final File file) {

        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (final File childFile : file.listFiles()) {
                this.deleteFileRecursively(childFile);
            }
        }

        if (this.isTargetExtension(file)) {
            file.delete();
        }
    }

    private boolean isTargetExtension(@NonNull final File file) {

        final String targetExtension = this.fileDeleteConfig.getExtension();

        if (StringUtils.isEmpty(targetExtension)) {
            return true;
        }

        return targetExtension.equals(this.getFileExtension(file));
    }

    private String getFileExtension(@NonNull final File file) {

        final String fileName = file.getName();

        if (!fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.indexOf(".") + 1);
    }
}
