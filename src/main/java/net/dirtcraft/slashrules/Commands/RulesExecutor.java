package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Config.Config;
import net.dirtcraft.slashrules.Config.Lang;
import net.dirtcraft.slashrules.Data.RulesData;
import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.Optional;

public class RulesExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> line = args.getOne("line");
        RulesData rulesData = Slashrules.getRulesData();

        if (!line.isPresent()){
            PaginationList.builder()
                    .title(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_RULES_HEADER))
                    .padding(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_PADDING))
                    .contents(rulesData.getRules())
                    .build()
                    .sendTo(src);
        } else {
            int rule = line.get()-1;
            List<Text> rules = rulesData.getRules();
            if (rules.size() > rule && rule >= 0){
                src.sendMessage(rules.get(rule));
            } else {
                throw new CommandException(Text.of(Lang.EXCEPTION_DOES_NOT_EXIST));
            }
        }

        return CommandResult.success();
    }
}
