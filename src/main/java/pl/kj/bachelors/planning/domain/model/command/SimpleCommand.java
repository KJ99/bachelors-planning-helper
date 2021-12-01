package pl.kj.bachelors.planning.domain.model.command;

import pl.kj.bachelors.planning.domain.model.extension.CommandType;

public class SimpleCommand extends Command<Object> {
    public SimpleCommand() {
        this.payload = null;
    }
}
