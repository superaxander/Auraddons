package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.init.ModConfig;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class ConfigBuilder {
    public static final Map<Class<?>, Map<Field, ForgeConfigSpec.ConfigValue<?>>> values = new HashMap<>();
    private final ForgeConfigSpec.Builder builder;

    public ConfigBuilder() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        this.builder = builder;
        parseFields(ModConfig.class);


        ModLoadingContext.get().registerConfig(COMMON, builder.build());
    }

    private void parseFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            boolean ignore = false;
            String[] comment = null;
            Config.RangeInt rangeInt = null;
            Config.RangeDouble rangeDouble = null;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Config.Ignore) {
                    ignore = true;
                    break;
                } else if (annotation instanceof Config.Comment) {
                    comment = ((Config.Comment) annotation).value();
                } else if (annotation instanceof Config.RangeInt) {
                    rangeInt = (Config.RangeInt) annotation;
                } else if (annotation instanceof Config.RangeDouble) {
                    rangeDouble = (Config.RangeDouble) annotation;
                }
            }
            if (ignore) continue;
            try {
                if (field.getType() == boolean.class) {
                    this.<Boolean>addConfigValue(clazz, field, comment, field.getName(), (Boolean) field.get(null));
                } else if (field.getType() == int.class) {
                    this.addConfigValue(clazz, field, comment, field.getName(), (int) field.get(null), rangeInt);
                } else if (field.getType() == long.class) {
                    this.addConfigValue(clazz, field, comment, field.getName(), (long) field.get(null), rangeInt);
                } else if (field.getType() == double.class) {
                    this.addConfigValue(clazz, field, comment, field.getName(), (double) field.get(null), rangeDouble);
                } else if (field.getType() == float.class) {
                    this.addConfigValue(clazz, field, comment, field.getName(), (float) field.get(null), rangeDouble);
                } else if (field.getType() == String.class) {
                    this.<String>addConfigValue(clazz, field, comment, field.getName(), (String) field.get(null));
                } else {
                    builder.push(field.getName());
                    parseFields(field.getType());
                    builder.pop();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void addValue(Class<?> category, Field field, ForgeConfigSpec.ConfigValue<?> spec) {
        if (!values.containsKey(category)) {
            values.put(category, new HashMap<>());
        }
        values.get(category).put(field, spec);
    }

    private <T> void addConfigValue(Class<?> category, Field field, String[] comment, String name, T defaultValue) {
        if (comment == null) {
            addValue(category, field, builder.define(name, defaultValue));
        } else {
            addValue(category, field, builder.comment(comment).define(name, defaultValue));
        }

    }

    private void addConfigValue(Class<?> category, Field field, String[] comment, String name, int defaultValue, Config.RangeInt rangeInt) {
        addConfigValue(category, field, comment, name, (long) defaultValue, rangeInt);
    }

    private void addConfigValue(Class<?> category, Field field, String[] comment, String name, long defaultValue, Config.RangeInt rangeInt) {
        int min, max;
        if (rangeInt == null) {
            min = Integer.MIN_VALUE;
            max = Integer.MAX_VALUE;
        } else {
            min = rangeInt.min();
            max = rangeInt.max();
        }
        if (comment == null) {
            addValue(category, field, builder.defineInRange(name, defaultValue, min, max));
        } else {
            addValue(category, field, builder.comment(comment).defineInRange(name, defaultValue, min, max));
        }
    }

    private void addConfigValue(Class<?> category, Field field, String[] comment, String name, float defaultValue, Config.RangeDouble rangeDouble) {
        addConfigValue(category, field, comment, name, (double) defaultValue, rangeDouble);
    }

    private void addConfigValue(Class<?> category, Field field, String[] comment, String name, double defaultValue, Config.RangeDouble rangeDouble) {
        double min, max;
        if (rangeDouble == null) {
            min = Double.MIN_VALUE;
            max = Double.MAX_VALUE;
        } else {
            min = rangeDouble.min();
            max = rangeDouble.max();
        }
        if (comment == null) {
            addValue(category, field, builder.defineInRange(name, defaultValue, min, max));
        } else {
            addValue(category, field, builder.comment(comment).defineInRange(name, defaultValue, min, max));
        }
    }
}
