package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.util.Set;

public class ReloadExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Set<CommandMapping> commands = Sponge.getCommandManager().getOwnedBy(Slashrules.getInstance());
        for (CommandMapping command : commands){
            Sponge.getCommandManager().removeMapping(command);
        }
        CommandRegistrar.registerCommands();
        Slashrules.setRulesData();
        return CommandResult.success();
    }
}
