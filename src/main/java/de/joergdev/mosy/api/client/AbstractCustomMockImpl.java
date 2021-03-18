package de.joergdev.mosy.api.client;

import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.api.model.RecordSession;
import de.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import de.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import de.joergdev.mosy.api.response.record.SaveResponse;

/**
 * <pre>
 * 
 * Abstract class that could be used to easily implement custom mocks.
 * 
 * Example:
 * 
 * public class CarService
 * {
 *  public static Car loadCar(Integer id, boolean mockEnabled, String mockProfile, Integer recordSessionID)
 *  {
 *   // ---- Mock ----
 *   CarCustomMockImpl carCustomMockImpl = new CarCustomMockImpl(mockEnabled, mockProfile, recordSessionID,
 *       id);
 *
 *   Car carMockResponse = carCustomMockImpl.getMockResponse();
 *   if (carMockResponse != null)
 *   {
 *     return carMockResponse;
 *   }
 *   // --------------
 *
 *   // ---- Real Access ----
 *   CarQueue queue = new CarQueue();
 *
 *   queue.put(id);
 *
 *   Car car = queue.read();
 *   // ---------------------
 *
 *   // ---- Record ----
 *   if (carCustomMockImpl.isRecordResponse())
 *   {
 *     carCustomMockImpl.recordResponse(Car.toXml());
 *   }
 *   // ----------------
 *
 *   return car;
 *  }
 * }
 * 
 * 
 * 
 * 
 * public class CarCustomMockImpl extends AbstractCustomMockImpl<Car>
 * {
 *  private boolean mockEnabled;
 *  private String mockProfile;
 *  private Integer recordSessionID;
 *
 *  private Integer carID;
 *
 *  public CarCustomMockImpl(boolean mockEnabled, String mockProfile, Integer recordSessionID, Integer carID)
 *  {
 *    this.mockEnabled = mockEnabled;
 *    this.mockProfile = mockProfile;
 *    this.recordSessionID = recordSessionID;
 *    this.carID = carID;
 *  }
 *
 *  public boolean isMockEnabled()
 *  {
 *    return mockEnabled;
 *  }
 *
 *  public void fillCustomRequestRequest(CustomRequestRequest req)
 *  {
 *    req.setInterfaceName("CarService");
 *    req.setInterfaceMethod("loadCar");
 *    req.setRequest("<id>" + carID + "</id>");
 *  }
 *
 *  public MosyApiClient getMosyApiClient()
 *  {
 *    MosyApiClient apiClient = new MosyApiClient();
 *    apiClient.systemLogin("m0sy".hashCode());
 *
 *    return apiClient;
 *  }
 *
 *  public String getMockProfileName()
 *  {
 *    return mockProfile;
 *  } 
 *
 *  public Integer getRecordSessionID()
 *  {
 *    return recordSessionID;
 *  }
 *
 *  public Car getMockResponse(CustomRequestResponse response)
 *  {
 *    return Car.fromXml(response.getResponse());
 *  }
 * }
 *
 *
 * </pre>
 * 
 * @author Andreas Joerg
 * @param <T> Responsetype
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

      mockRecord = new de.joergdev.mosy.api.model.Record();
      mockRecord.setInterfaceMethod(response.getInterfaceMethod());
      mockRecord.setRequestData(req.getRequest());

      Integer recordSessionID = getRecordSessionID();
      if (recordSessionID != null)
      {
        mockRecord.setRecordSession(new RecordSession(recordSessionID));
      }
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