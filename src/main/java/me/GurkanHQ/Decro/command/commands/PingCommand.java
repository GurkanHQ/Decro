package me.GurkanHQ.Decro.command.commands;

import me.GurkanHQ.Decro.command.CommandContext;
import me.GurkanHQ.Decro.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();
        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Rest ping: %sms\nWebSocket ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to discord servers";
    }
}
