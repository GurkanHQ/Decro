package me.GurkanHQ.Decro;


import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final List<TextChannel> dontDoThis = event.getGuild().getTextChannelsByName("", true);

        if (dontDoThis.isEmpty()) {
            return;
        }

        final TextChannel pleaseDontDoThisAtAll = dontDoThis.get(0);

        final String useGuildSpecificSettingsInstead = String.format("Welcome %s to %n",
                event.getMember().getUser().getAsTag(), event.getGuild().getName());

        pleaseDontDoThisAtAll.sendMessageFormat(useGuildSpecificSettingsInstead).queue();

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        final List<TextChannel> dontDoThis = event.getGuild().getTextChannelsByName("", true);

        if (dontDoThis.isEmpty()) {
            return;
        }

        final TextChannel pleaseDontDoThisAtAll = dontDoThis.get(0);

        final String useGuildSpecificSettingsInstead = String.format("Goodbye %s",
                event.getMember().getUser().getAsTag());

        pleaseDontDoThisAtAll.sendMessageFormat(useGuildSpecificSettingsInstead).queue();

    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }
    }
}