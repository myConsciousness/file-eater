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

package org.thinkit.bot.filater.result;

import java.io.Serializable;

import org.thinkit.common.base.precondition.Preconditions;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class that the result of file delete command.
 *
 * @author Kato Shinya
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileDeleteCommandResult implements Serializable {

    /**
     * The count
     */
    @Getter
    private int count;

    /**
     * The size
     */
    @Getter
    private long size;

    /**
     * Returns the new instance of builder object.
     *
     * @return The new instance of builder object
     */
    public static FileDeleteCommandResultBuilder builder() {
        return new FileDeleteCommandResultBuilder();
    }

    /**
     * The inner builder class for {@link FileDeleteCommandResult} .
     */
    public static class FileDeleteCommandResultBuilder {

        /**
         * The count
         */
        @Getter
        private int count;

        /**
         * The capacity
         */
        @Getter
        private long size;

        /**
         * Sets the count.
         *
         * @param count The count
         * @return This builder instance
         */
        public FileDeleteCommandResultBuilder count(final int count) {
            this.count = count;
            return this;
        }

        /**
         * Sets the size.
         *
         * @param size The size
         * @return This builder instance
         */
        public FileDeleteCommandResultBuilder size(final long size) {
            this.size = size;
            return this;
        }

        /**
         * Returns the new instance of {@link FileDeleteCommandResult} based on the set
         * values by {@link #count(int)} and {@link #size(long)} .
         *
         * @return The new instance of {@link FileDeleteCommandResult}
         */
        public FileDeleteCommandResult build() {
            Preconditions.requirePositive(count);
            Preconditions.requirePositive(size);

            final FileDeleteCommandResult fileDeleteCommandResult = new FileDeleteCommandResult();
            fileDeleteCommandResult.count = this.count;
            fileDeleteCommandResult.size = this.size;

            return fileDeleteCommandResult;
        }
    }
}
