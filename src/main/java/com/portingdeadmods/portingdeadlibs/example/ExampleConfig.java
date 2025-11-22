package com.portingdeadmods.portingdeadlibs.example;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;

public class ExampleConfig {
    @ConfigValue(comment = "Le i lol")
    public static double i = 0;
    @ConfigValue(comment = "Le j lol")
    public static float j = 0;
    @ConfigValue(comment = "Slay", category = "ballz")
    public static int lol = 100;
}
