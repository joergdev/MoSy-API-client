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
 *  public static Car loadCar(Integer id, CustomMockArguments customMockArguments)
 *  {
 *   // ---- Mock ----
 *   CarCustomMockImpl carCustomMockImpl = new CarCustomMockImpl(customMockArguments, id);
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
 *     carCustomMockImpl.recordResponse(car.toXml());
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
 * public class CarCustomMockImpl extends AbstractCustomMockImpl&lt;Car&gt;
 * {
 *  private Integer carID;
 *  
 *  public CarCustomMockImpl(CustomMockArguments customMockArguments, Integer carID)
 *  {
 *    super(customMockArguments);
 *    
 *    this.carID = carID;
 *  }
 *
 *  public void fillCustomRequestRequest(CustomRequestRequest req)
 *  {
 *    req.setInterfaceName("CarService");
 *    req.setInterfaceMethod("loadCar");
 *    req.setRequest("&lt;id&gt;" + carID + "&lt;/id&gt;");
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
 * @param <T> Responsetype - dynmaic return type for mock response
 */
public abstract class AbstractCustomMockImpl<T>
{
  protected final CustomMockArguments customMockArguments;

  private MosyApiClient mosyApiClient;

  private boolean recordResponse = false;
  private Record mockRecord = null;

  public AbstractCustomMockImpl(CustomMockArguments customMockArguments)
  {
    this.customMockArguments = customMockArguments;
  }

  public T getMockResponse()
  {
    if (customMockArguments.isMockEnabled())
    {
      CustomRequestRequest req = new CustomRequestRequest();
      fillCustomRequestRequest(req);

      mosyApiClient = getMosyApiClient();

      CustomRequestResponse response = mosyApiClient.customRequest(req, customMockArguments.getMockProfileName(), customMockArguments.getRecordSessionID());

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

  /**
   * Implementation needed:
   * 
   * Set {@link CustomRequestRequest} interface name, method name and request data.
   * 
   * @param req - CustomRequestRequest
   */
  public abstract void fillCustomRequestRequest(CustomRequestRequest req);

  public abstract MosyApiClient getMosyApiClient();

  /**
   * Build Mockresponse of type T (for example your expected DTO) by {@link CustomRequestResponse}.
   * 
   * @param response - CustomRequestResponse
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

      Integer recordSessionID = customMockArguments.getRecordSessionID();
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
