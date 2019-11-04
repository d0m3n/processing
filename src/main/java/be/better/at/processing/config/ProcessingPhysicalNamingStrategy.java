package be.better.at.processing.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class ProcessingPhysicalNamingStrategy implements PhysicalNamingStrategy {

    private static final String JPA_SUFFIX = "JPA";

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    private Identifier apply(Identifier name) {
        if (name == null) {
            return null;
        }
        StringBuilder text = new StringBuilder(name.getText().replace('.', '_'));
        for (int i = 1; i < text.length() - 1; i++) {
            if (isUnderscoreRequired(text.charAt(i - 1), text.charAt(i), text.charAt(i + 1))) {
                text.insert(i++, '_');
            }
        }
        String[] names = text.toString().toLowerCase(Locale.ENGLISH).split("_");
        for (int i = 0; i < names.length; i++) {
            names[i] = removeEnd(names[i], JPA_SUFFIX);
        }

        return new Identifier(String.join("_", names), name.isQuoted());
    }

    private static String removeEnd(final String str, final String remove) {
        if (str.isEmpty() || remove.isEmpty()) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }


    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

}
