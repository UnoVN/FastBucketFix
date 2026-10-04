package me.nhan.fastbucketfix;

import org.bukkit.plugin.java.JavaPlugin;

public final class FastBucketFix extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BucketListener(), this);
        getLogger().info("FastBucketFix da bat!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FastBucketFix da tat!");
    }
}
