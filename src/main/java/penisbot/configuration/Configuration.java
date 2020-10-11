package penisbot.configuration;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.configuration.annotation.ConfigurationParameter;

public class Configuration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ConfigurationParameter(environmentKey="PENISBOT_SERVER_HOSTNAME")
    private String serverHostname = "localhost";

    @ConfigurationParameter(environmentKey="PENISBOT_SERVER_PORT")
    private Integer serverPort = 6667;

    @ConfigurationParameter(environmentKey="PENISBOT_SERVER_USERNAME")
    private String serverUsername = null;

    @ConfigurationParameter(environmentKey="PENISBOT_SERVER_PASSWORD", sensitive=true)
    private String serverPassword = null;

    @ConfigurationParameter(environmentKey="PENISBOT_NICKNAME")
    private String nickname = "Pen1sBot";

    @ConfigurationParameter(environmentKey="PENISBOT_LOGIN_NAME")
    private String loginName = "Pen1sBot";

    @ConfigurationParameter(environmentKey="PENISBOT_ADMIN_PASSWORD", sensitive=true)
    private String adminPassword = "penis";

    @ConfigurationParameter(environmentKey="PENISBOT_COMMAND_PREFIX")
    private String commandPrefix = "!";

    public Configuration() {

    }

    public void populateFromEnv() throws ConfigurationException {
        // Grab environment
        Map<String, String> environment = System.getenv();

        // Populate
        populateFromEnv(environment);
    }

    public void populateFromEnv(Map<String, String> environment) throws ConfigurationException {

        try {

            // Go through fields with the ConfigurationParameter annotation
            Collection<Field> fields = getConfigurationParameterFields();
            for (Field field : fields) {
                // Find setter for this field
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), this.getClass());
                Method setMethod = propertyDescriptor.getWriteMethod();

                // Check if it has an environmentKey attribute
                ConfigurationParameter configurationParameterAnnotation = (ConfigurationParameter) field.getAnnotation(ConfigurationParameter.class);
                String environmentKey = configurationParameterAnnotation.environmentKey();
                if (environmentKey != null) {
                    // Check if we have this in the environment
                    if (environment.containsKey(environmentKey)) {
                        // Grab our value as a string
                        String stringValue = environment.get(environmentKey);

                        // Do we need to convert?
                        if (field.getType().isAssignableFrom(stringValue.getClass())) {
                            // Set value using set method
                            setMethod.invoke(this, field.getType().cast(stringValue));
                        } else {
                            // Find and use static valueOf method
                            Method valueOfMethod = field.getType().getMethod("valueOf", stringValue.getClass());

                            // Set value
                            setMethod.invoke(this, field.getType().cast(valueOfMethod.invoke(field.getType(), stringValue)));
                        }
                    }
                }

                logger.info("Set " + field.getName() + "=" + ( configurationParameterAnnotation.sensitive() ? "***" : String.valueOf(field.get(this))));
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IntrospectionException e) {
            logger.error("Error parsing configuration from environment", e);
            throw new ConfigurationException(e);
        }
    }

    private Collection<Field> getConfigurationParameterFields() {
        ArrayList<Field> result = new ArrayList<>();

        // Go through declared fields
        for (Field field : getClass().getDeclaredFields()) {
            // Annotated?
            if (field.isAnnotationPresent(ConfigurationParameter.class)) {
                result.add(field);
            }
        }

        return result;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }
}
