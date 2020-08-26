package com.github.joergdev.mosy.api.client;

import java.util.Collection;
import com.github.joergdev.mosy.api.response.ResponseMessage;
import com.github.joergdev.mosy.api.response.ResponseMessageLevel;

public class MosyApiClientException extends RuntimeException
{
  private final Collection<ResponseMessage> responseMessages;

  public MosyApiClientException(Collection<ResponseMessage> responseMessages)
  {
    super(buildErrorMessage(responseMessages));

    this.responseMessages = responseMessages;
  }

  private static String buildErrorMessage(Collection<ResponseMessage> responseMessages)
  {
    StringBuilder buiMsg = new StringBuilder();

    for (ResponseMessage rm : responseMessages)

    {
      if (buiMsg.length() > 0)
      {
        buiMsg.append("\n");
      }

      if (ResponseMessageLevel.FATAL.equals(rm.getResponseCode().level))
      {
        buiMsg.append("FATAL: ");
      }

      buiMsg.append(rm.getFullMessage());
    }

    if (buiMsg.length() == 0)
    {
      buiMsg.append(Resources.getErrorMessage("unknown_error"));
    }

    return buiMsg.toString();
  }

  public Collection<ResponseMessage> getResponseMessages()
  {
    return responseMessages;
  }
}