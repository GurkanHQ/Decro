package me.GurkanHQ.Decro.command.commands;

import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.GurkanHQ.Decro.command.CommandContext;
import me.GurkanHQ.Decro.command.ICommand;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.size() < 2) {
            channel.sendMessage("Correct usage is: =minecraft uuid/names <username/uuid>");
            return;
        }

        final String item = args.get(0);
        final String id = args.get(1);

        if (item.equals("uuid")) {
            fetchUUID(id, (uuid) -> {
                if (uuid == null) {
                    channel.sendMessage("User with name " + id + " was not found").queue();
                    return;
                }
                channel.sendMessage(id + "'s uuid is " + uuid).queue();
            });
        } else if (item.equals("names")) {
           fetchNameHistory(id, (names) -> {
               if (names == null) {
                   channel.sendMessage("That uuid is not valid.").queue();
                   return;
               }

               final String namesJoined = String.join(", ", names);

               channel.sendMessageFormat("Name history for %s:\n%s", id, namesJoined).queue();
           });
        }  else {
            channel.sendMessageFormat("%s is not known, please either choose uuid or names.", item).queue();
        }

    }

    @Override
    public String getName() {
        return "minecraft";
    }

    @Override
    public String getHelp() {
        return "Get the UUID or name history of a Minecraft Player\n" +
                "Usage: =minecraft uuid/names <username/uuid>";
    }


    private void fetchUUID(String username, Consumer<String> callback) {

        WebUtils.ins.getJSONObject(
                "https://api.mojang.com/users/profiles/minecraft/" + username,
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> {
                    callback.accept(json.get("id").asText());
                },
                (error) -> {
                    callback.accept(null);
                }
        );

    }

    private void fetchNameHistory(String uuid, Consumer<List<String>> callback)  {

        WebUtils.ins.getJSONArray(
                "https://api.mojang.com/users/profiles/" + uuid + "/names",
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> {
                    List<String> names = new ArrayList<>();
                    json.forEach((item) -> names.add(item.get("name").asText()));

                    callback.accept(names);
                },
                (error) -> {
                    callback.accept(null);
                }
        );

    }

}
