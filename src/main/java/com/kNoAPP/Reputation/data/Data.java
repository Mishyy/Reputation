package com.kNoAPP.Reputation.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kNoAPP.Reputation.Reputation;

public enum Data {
	
	CONFIG(new File(Reputation.getPlugin().getDataFolder(), "config.yml"), "config.yml"),
	MAIN(null, "main.yml");
	
	private File file;
	private FileConfiguration fc;
	private String fileName;
	
	private Data(File file, String fileName) {
		this.file = file;
		this.fc = new YamlConfiguration();
		this.fileName = fileName;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(String path) {
		if(path == "") this.file = new File(Reputation.getPlugin().getDataFolder(), this.getFileName());
		else this.file = new File(path, this.getFileName());
	}
	
	public FileConfiguration getFileConfig() {
		return fc;
	}
	
	public void saveDataFile(FileConfiguration fc) {
		this.fc = fc;
	}
	
	public void logDataFile() {
		try {
			this.fc.save(this.getFile());
		} catch (IOException e) {}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getPath() {
		return this.getFile().getAbsolutePath();
	}
	
	public void createDataFile() {
		if(!this.getFile().exists()) {
			Reputation.getPlugin().getLogger().info(this.getFileName() + " not found. Creating...");
			try {
				this.getFile().createNewFile();
			} catch (Exception e) {}
			FileConfiguration fc = this.fc;
			if(this == CONFIG) {
				fc.set("Version", "1.0.0");
				fc.set("UseMainFolder", true);
				fc.set("UseCustomFolder", "/example/path/");
			}
			if(this == MAIN) {
				fc.set("Version", "1.0.0");
				fc.set("MySQL.host", "localhost");
				fc.set("MySQL.port", 3306);
				fc.set("MySQL.database", "ExampleDB");
				fc.set("MySQL.username", "root");
				fc.set("MySQL.password", "psswd");
			}
			this.saveDataFile(fc);
			this.logDataFile();
        }
	
		try {
			this.fc.load(this.getFile());
		} catch (Exception e) {}
	}
}
