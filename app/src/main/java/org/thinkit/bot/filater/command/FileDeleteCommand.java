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
import org.thinkit.bot.filater.catalog.DateFormat;
import org.thinkit.bot.filater.catalog.Delimiter;
import org.thinkit.bot.filater.config.FileDeleteConfig;
import org.thinkit.bot.filater.result.FileDeleteCommandResult;
import org.thinkit.bot.filater.util.DateUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that manages the file delete command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "from")
public final class FileDeleteCommand extends AbstractBotCommand<FileDeleteCommandResult> {

    /**
     * The file delete config
     */
    private FileDeleteConfig fileDeleteConfig;

    @Override
    public FileDeleteCommandResult executeBotProcess() {

        final FileDeleteCommandResult.FileDeleteCommandResultBuilder fileDeleteCommandResultBuilder = FileDeleteCommandResult
                .builder();
        this.deleteFileRecursively(new File(this.fileDeleteConfig.getDirectoryPath()), fileDeleteCommandResultBuilder);

        return fileDeleteCommandResultBuilder.build();
    }

    /**
     * Detects and recursively deletes files that have expired based on the period
     * days set in {@link FileDeleteConfig} . Recursive processing is performed if
     * the file object is a directory.
     *
     * @param file                           The file
     * @param fileDeleteCommandResultBuilder The builder of file delete command
     *                                       result
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private void deleteFileRecursively(@NonNull final File file,
            FileDeleteCommandResult.FileDeleteCommandResultBuilder fileDeleteCommandResultBuilder) {

        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (final File childFile : file.listFiles()) {
                this.deleteFileRecursively(childFile, fileDeleteCommandResultBuilder);
            }
        }

        if (this.isExpiredFile(file) && this.isTargetExtension(file)) {
            fileDeleteCommandResultBuilder.size(fileDeleteCommandResultBuilder.getSize() + file.length());

            if (file.delete()) {
                fileDeleteCommandResultBuilder.count(fileDeleteCommandResultBuilder.getCount() + 1);
            }
        }
    }

    /**
     * Checks if the file extension is the extension to be deleted.
     *
     * <p>
     * The file extension to be deleted will be checked based on the value set in
     * {@link FileDeleteConfig} . The file extension value to be deleted obtained
     * from {@link FileDeleteConfig} and the file extension to be checked are
     * normalized using the {@link #normalizeString(String)} method, so case and
     * spaces before and after the value do not interfere with the comparison.
     *
     * @param file The file
     * @return {@code true} if the file extension is the target extention to be
     *         deleted, otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private boolean isTargetExtension(@NonNull final File file) {

        final String targetExtension = this.normalizeString(this.fileDeleteConfig.getExtension());

        if (StringUtils.isEmpty(targetExtension)) {
            // If the target extension is empty, all extensions will be targeted.
            return true;
        }

        return targetExtension.equals(this.getFileExtension(file));
    }

    /**
     * Returns the extension from the file name.
     *
     * @param file The file name
     * @return The file extension
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private String getFileExtension(@NonNull final File file) {

        final String fileName = file.getName();
        final String dotDelimiter = Delimiter.DOT.getTag();

        if (!fileName.contains(dotDelimiter)) {
            return "";
        }

        final int firstIndex = fileName.indexOf(dotDelimiter);
        final int lastIndex = fileName.indexOf(dotDelimiter, firstIndex + 1);

        return this.normalizeString(
                fileName.substring(firstIndex + 1, lastIndex > firstIndex ? lastIndex : fileName.length()));
    }

    /**
     * Checks if the file object passed as an argument has expired.
     *
     * @param file The file
     * @return {@code true} if the file is expired, otherwise {@code false}
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private boolean isExpiredFile(@NonNull final File file) {
        return DateUtils.getDateAfter(DateFormat.YYYY_MM_DD_HH_MM_SS, file.lastModified(),
                this.fileDeleteConfig.getPeriodDays()).before(DateUtils.now());
    }

    /**
     * Normalizes the string passed as an argument by lowercasing it and trimming
     * spaces before and after it.
     *
     * @param string The string
     * @return The normalized string
     *
     * @exception NullPointerException If {@code null} is passed as an argument
     */
    private String normalizeString(@NonNull final String string) {
        return string.toLowerCase().trim();
    }
}
