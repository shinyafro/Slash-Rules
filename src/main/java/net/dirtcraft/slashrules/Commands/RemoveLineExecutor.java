package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Config.Lang;
import net.dirtcraft.slashrules.Data.RulesData;
import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class RemoveLineExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        @SuppressWarnings("OptionalGetWithoutIsPresent") Integer line = args.<Integer>getOne("line").get();
        RulesData rulesData = Slashrules.getRulesData();
        try {
            rulesData.removeLine(line-1); //Indexes start with 0, so we decrement it so it makes sense to normies.
        } catch (IllegalArgumentException e){
            throw new CommandException(Text.of(Lang.EXCEPTION_DOES_NOT_EXIST));
        }
        return CommandResult.success();
    }
}
