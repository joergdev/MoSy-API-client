package com.github.joergdev.mosy.api.client;

import com.github.joergdev.mosy.api.model.Record;
import com.github.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import com.github.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import com.github.joergdev.mosy.api.response.record.SaveResponse;

/**
 * Abstract class that could be used to easily implement custom mocks.
 * 
 * @author Andreas Joerg
 *
 * @param <T>
 */
public abstract class AbstractCustomMockImpl<T>
{
  private MosyApiClient mosyApiClient;

  private boolean recordResponse = false;
  private Record mockRecord = null;

  public T getMockResponse()
  {
    if (isMockEnabled())
    {
      CustomRequestRequest req = new CustomRequestRequest();
      fillCustomRequestRequest(req);

      mosyApiClient = getMosyApiClient();

      CustomRequestResponse response = mosyApiClient.customRequest(req, getMockProfileName(),
          getRecordSessionID());

      // Routing
      if (response.isRoute())
      {
        preFillRecord(req, response);
      }
      // Mock
      else
      {
        return getMockResponse(response);
      }
    }

    return null;
  }

  public SaveResponse recordResponse(String response)
  {
    mockRecord.setResponse(response);

    return mosyApiClient.saveRecord(mockRecord);
  }

  public abstract boolean isMockEnabled();

  /**
   * Implementation needed:
   * 
   * Set {@link CustomRequestRequest} interface name, method name and request data.
   * 
   * @param req
   */
  public abstract void fillCustomRequestRequest(CustomRequestRequest req);

  public abstract MosyApiClient getMosyApiClient();

  public abstract String getMockProfileName();

  public abstract Integer getRecordSessionID();

  /**
   * Build Mockresponse of type T (for example your expected DTO) by {@link CustomRequestResponse}.
   * 
   * @param response
   * @return T
   */
  public abstract T getMockResponse(CustomRequestResponse response);

  private void preFillRecord(CustomRequestRequest req, CustomRequestResponse response)
  {
    // Soll record zu response erzeugt werden?
    if (response.isRecord())
    {
      setRecordResponse(true);

      mockRecord = new com.github.joergdev.mosy.api.model.Record();
      mockRecord.setInterfaceMethod(response.getInterfaceMethod());
      mockRecord.setRequestData(req.getRequest());
    }
  }

  public boolean isRecordResponse()
  {
    return recordResponse;
  }

  public void setRecordResponse(boolean recordResponse)
  {
    this.recordResponse = recordResponse;
  }

  public Record getMockRecord()
  {
    return mockRecord;
  }

  public void setMockRecord(Record mockRecord)
  {
    this.mockRecord = mockRecord;
  }
}