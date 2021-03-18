package de.joergdev.mosy.api.client;

import java.util.ResourceBundle;

public class Resources
{
  private static final ResourceBundle MOSY_API_CLIENT = ResourceBundle.getBundle("mosy_api_client");
  private static final ResourceBundle ERROR_MESSAGES = ResourceBundle
      .getBundle("mosy_api_client_error_messages");

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