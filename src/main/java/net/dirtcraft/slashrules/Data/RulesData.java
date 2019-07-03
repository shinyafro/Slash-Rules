package net.dirtcraft.slashrules.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dirtcraft.slashrules.Config.Config;
import net.dirtcraft.slashrules.Config.Lang;
import net.dirtcraft.slashrules.Slashrules;
import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RulesData {
    private final File fileLocation;
    private final Logger logger;
    private List<String> immutableRules;
    private List<String> mutableRules;
    private boolean scheduledUpdate;

    public RulesData(){
        scheduledUpdate = false;
        logger = Slashrules.getInstance().getLogger();
        fileLocation = new File (Slashrules.getInstance().getConfigDir().getParent(), "Rules.json");
        try{
            loadImmutableRules();
        } catch (Throwable e){
            logger.error(e.getMessage());
            immutableRules = new ArrayList<>();
        }
        try {
            if (!fileLocation.exists()) {
                mutableRules = new ArrayList<>();
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

    private void loadImmutableRules() {
        HttpURLConnection con = null;
        URL url;
        try {
            url = new URL(Config.IMMUTABLE_RULES_URL);
            con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        } catch (IOException e) {
            logger.error("slash-rules: "+e);
            if (con != null) con.disconnect();
            return;
        }

        try (
                InputStream in = con.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in)
                ){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            immutableRules = gson.fromJson(inputStreamReader, ArrayList.class);
        } catch (IOException e){
            logger.error("slash-rules: "+e);
        } finally {
            con.disconnect();
        }
    }

    private void loadRules() {
        try(
                FileReader file = new FileReader(fileLocation);
                BufferedReader filestream = new BufferedReader(file);
        ){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            mutableRules = gson.fromJson(filestream, ArrayList.class);
            if (mutableRules == null) mutableRules = new ArrayList<>();
        } catch (IOException e){
            logger.error("Slash-Rules: ", e);
        }
    }

    private void saveRules() {
        try(
                FileWriter file = new FileWriter(fileLocation);
                BufferedWriter filestream = new BufferedWriter(file)
        ){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(mutableRules, ArrayList.class, filestream);
            scheduledUpdate = false;
        } catch (IOException e){
            logger.error("SlashRules - Error saving: ", e);
        }
    }

    public List<Text> getRules() {
        List<Text> rules = new ArrayList<>();
        immutableRules.forEach(rule->rules.add(TextSerializers.FORMATTING_CODE.deserialize(rule)));
        mutableRules.forEach(rule->rules.add(TextSerializers.FORMATTING_CODE.deserialize(rule)));
        return rules;
    }

    public void setLine(int line, String input) throws IllegalArgumentException {
        line = getMutableLine(line);
        mutableRules.set(line, input);
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Task.builder()
                    .delayTicks(100)
                    .execute(this::saveRules)
                    .submit(Slashrules.getInstance());
        }
    }

    public void addLine(String input){
        mutableRules.add(input);
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
        line = getMutableLine(line);
        mutableRules.remove(line);
        if (!scheduledUpdate) {
            scheduledUpdate = true;
            Task.builder()
                    .delayTicks(100)
                    .execute(this::saveRules)
                    .submit(Slashrules.getInstance());
        }
    }

    private int getMutableLine(int line){
        if (line < 0) throw new IllegalArgumentException(Lang.EXCEPTION_LINE_VALUE_SMALL);
        if (line >= mutableRules.size()+immutableRules.size()) throw new IllegalArgumentException(Lang.EXCEPTION_LINE_VALUE_BIG);
        if (line < immutableRules.size()) throw new IllegalArgumentException(Lang.EXCEPTION_LINE_VALUE_PROTECTED);
        line = line - immutableRules.size();
        return line;
    }
}
