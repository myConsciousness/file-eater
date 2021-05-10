package org.thinkit.bot.filater.command;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class AbstractBotCommand<R> implements BotCommand<R>, Serializable {

    protected abstract R executeBotProcess();

    @Override
    public R execute() {
        return this.executeBotProcess();
    }
}
