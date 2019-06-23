package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Config.Config;
import net.dirtcraft.slashrules.Config.Perms;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class HelpExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        List<Text> contents = new ArrayList<>();
        if (src.hasPermission(Perms.MAIN)){
            Text line = Text.builder("/rules")
                    .onClick(TextActions.runCommand("/rules"))
                    .build();
        }
        if (src.hasPermission(Perms.OTHER)){
            Text line = Text.builder("/rules other <player>")
                    .onClick(TextActions.suggestCommand("/rules other "))
                    .build();
        }
        if (src.hasPermission(Perms.ADD_LINE)){
            Text line = Text.builder("/rules addline <text>")
                    .onClick(TextActions.suggestCommand("/rules addline "))
                    .build();
        }
        if (src.hasPermission(Perms.SET_LINE)){
            Text line = Text.builder("/rules setline <line> <text>")
                    .onClick(TextActions.suggestCommand("/rules setline "))
                    .build();
        }
        if (src.hasPermission(Perms.REM_LINE)){
            Text line = Text.builder("/rules remove <line>")
                    .onClick(TextActions.suggestCommand("/rules remove "))
                    .build();
        }
        if (src.hasPermission(Perms.RELOAD)){
            Text line = Text.builder("/rules reload")
                    .onClick(TextActions.runCommand("/rules reload"))
                    .build();
        }

        PaginationList.builder()
                .title(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_HELP_HEADER))
                .padding(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_PADDING))
                .contents(contents)
                .build()
                .sendTo(src);
        return CommandResult.success();
    }
}
