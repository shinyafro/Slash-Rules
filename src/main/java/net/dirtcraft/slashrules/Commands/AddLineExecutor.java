package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Data.RulesData;
import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class AddLineExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        @SuppressWarnings("OptionalGetWithoutIsPresent") String text = args.<String>getOne("text").get();
        RulesData rulesData = Slashrules.getRulesData();
        rulesData.addLine(text);
        return CommandResult.success();
    }
}
