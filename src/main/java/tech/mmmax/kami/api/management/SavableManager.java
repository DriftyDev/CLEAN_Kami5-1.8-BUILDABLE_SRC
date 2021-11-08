/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import tech.mmmax.kami.api.config.ISavable;
import tech.mmmax.kami.impl.KamiMod;

public class SavableManager {
    public static final File MAIN_FOLDER = new File(System.getProperty("user.dir") + File.separator + KamiMod.NAME);
    public static SavableManager INSTANCE;
    Yaml yaml;
    final List<ISavable> savables = new ArrayList<ISavable>();

    public SavableManager() {
        if (!MAIN_FOLDER.exists()) {
            MAIN_FOLDER.mkdir();
        }
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(options);
    }

    public List<ISavable> getSavables() {
        return this.savables;
    }

    public void load() {
        for (ISavable savable : this.getSavables()) {
            try {
                File file;
                File dir = new File(MAIN_FOLDER.getAbsolutePath() + File.separator + savable.getDirName());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (!(file = new File(MAIN_FOLDER.getAbsolutePath() + File.separator + savable.getDirName() + File.separator + savable.getFileName())).exists()) {
                    file.createNewFile();
                    continue;
                }
                FileInputStream inputStream = new FileInputStream(file);
                Map map = (Map)this.yaml.load(inputStream);
                savable.load(map);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save() throws IOException {
        System.out.println("Saving  your config");
        for (ISavable savable : this.getSavables()) {
            File file;
            File dir = new File(MAIN_FOLDER.getAbsolutePath() + File.separator + savable.getDirName());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!(file = new File(MAIN_FOLDER.getAbsolutePath() + File.separator + savable.getDirName() + File.separator + savable.getFileName())).exists()) {
                file.createNewFile();
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                this.yaml.dump(savable.save(), new FileWriter(file));
            }
            catch (Throwable exception) {
                exception.printStackTrace();
            }
        }
    }
}

