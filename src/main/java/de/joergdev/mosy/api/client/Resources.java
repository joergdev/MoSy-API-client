package de.joergdev.mosy.api.client;

import java.util.ResourceBundle;
import de.joergdev.mosy.shared.Utils;

public class Resources
{
  /**
   * <pre>
   * MOSY_API_ENDPOINT
   * 
   * If system property is not set, api endpoint of mosy_api_client.properties is used.
   * </pre>
   */
  public static final String SYSTEM_PROPERTY_API_ENDPOINT = "MOSY_API_ENDPOINT";

  private static final String PROPERTY_API_ENDPOINT = "api_endpoint";

  private static final ResourceBundle MOSY_API_CLIENT = ResourceBundle.getBundle("mosy_api_client");
  private static final ResourceBundle ERROR_MESSAGES = ResourceBundle.getBundle("mosy_api_client_error_messages");

  /**
   * Get the api endpoint from system-property ({@link #SYSTEM_PROPERTY_API_ENDPOINT} or from mosy_api_client.properties.
   * 
   * @return String - API endpoint (URL)
   */
  public static String getApiEndpoint()
  {
    String sysProp = Utils.getSystemProperty(SYSTEM_PROPERTY_API_ENDPOINT);

    if (!Utils.isEmpty(sysProp))
    {
      return sysProp;
    }
    else
    {
      return getProperty(PROPERTY_API_ENDPOINT);
    }
  }

  public static String getProperty(String key)
  {
    return MOSY_API_CLIENT.getString(key);
  }

  public static String getErrorMessage(String key, String... details)
  {
    return getMessage(ERROR_MESSAGES, key, details);
  }

  private static String getMessage(ResourceBundle rb, String key, String... details)
  {
    String msg = rb.getString(key);

    if (details != null && details.length > 0)
    {
      msg = String.format(msg, (Object[]) details);
    }

    return msg;
  }
}
