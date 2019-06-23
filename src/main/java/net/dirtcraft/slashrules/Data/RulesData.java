package net.dirtcraft.slashrules.Data;

import net.dirtcraft.slashrules.Slashrules;
import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RulesData {
    private final File fileLocation;
    private final Logger logger;
    private List<Text> rules;
    private boolean scheduledUpdate;

    public RulesData(){
        scheduledUpdate = false;
        logger = Slashrules.getInstance().getLogger();
        fileLocation = new File (Slashrules.getInstance().getConfigDir().getParent(), "rules.cfg");
        try {
            if (!fileLocation.exists()) {
                rules = new ArrayList<>();
                boolean created = fileLocation.createNewFile();
                if (!created) logger.error("SlashRules - did not create file");
            } else {
                Task.builder()
                        .execute(this::loadRules)
                        .submit(Slashrules.getInstance());
            }
        }catch (IOException e){
            logger.error("Slash-Rules: ", e);
        }
    }

    private void loadRules() {
        try(
                FileReader file = new FileReader(fileLocation);
                BufferedReader filestream = new BufferedReader(file)
        ){
            rules = new ArrayList<>();
            String line;
            while ((line = filestream.readLine())!= null){
                rules.add(TextSerializers.JSON.deserialize(line));
            }
        } catch (IOException e){
            logger.error("Slash-Rules: ", e);
        }
    }

    private void saveRules() {
        try(
                FileWriter file = new FileWriter(fileLocation);
                BufferedWriter filestream = new BufferedWriter(file)
        ){
            for (Text line : rules){
                filestream.write(TextSerializers.JSON.serialize(line));
                filestream.newLine();
            }
            scheduledUpdate = false;
        } catch (IOException e){
            logger.error("SlashRules - Error saving: ", e);
        }
    }

    public List<Text> getRules() {
        if (rules.size()==0){
            List<Text> empty = rules;
            empty.add(Text.of("There are no rules."));
            return empty;
        }
        return rules;
    }

    public void setLine(int line, String input) throws IllegalArgumentException {
        if (rules.size() <= line || line < 0) throw new IllegalArgumentException();
        rules.set(line, TextSerializers.FORMATTING_CODE.deserialize(input));
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Task.builder()
                    .delayTicks(100)
                    .execute(this::saveRules)
                    .submit(Slashrules.getInstance());
        }
    }

    public void addLine(String input){
        rules.add(TextSerializers.FORMATTING_CODE.deserialize(input));
        logger.error(input);
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Task.builder()
                    .delayTicks(100)
                    .execute(this::saveRules)
                    .submit(Slashrules.getInstance());
        }
    }

    public void removeLine(int line) throws IllegalArgumentException {
        if (rules.size() <= line || line < 0) throw new IllegalArgumentException();
        rules.remove(line);
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Task.builder()
                    .delayTicks(100)
                    .execute(this::saveRules)
                    .submit(Slashrules.getInstance());
        }
    }
}
