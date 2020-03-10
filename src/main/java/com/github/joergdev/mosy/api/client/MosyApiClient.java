package com.github.joergdev.mosy.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.github.joergdev.mosy.api.APIConstants;
import com.github.joergdev.mosy.api.model.BaseData;
import com.github.joergdev.mosy.api.model.Interface;
import com.github.joergdev.mosy.api.model.MockData;
import com.github.joergdev.mosy.api.model.Record;
import com.github.joergdev.mosy.api.model.RecordConfig;
import com.github.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import com.github.joergdev.mosy.api.response.AbstractResponse;
import com.github.joergdev.mosy.api.response.EmptyResponse;
import com.github.joergdev.mosy.api.response.ResponseMessage;
import com.github.joergdev.mosy.api.response.ResponseMessageLevel;
import com.github.joergdev.mosy.api.response._interface.method.LoadMockDataResponse;
import com.github.joergdev.mosy.api.response._interface.method.LoadRecordConfigsResponse;
import com.github.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import com.github.joergdev.mosy.api.response.mocksession.CreateResponse;
import com.github.joergdev.mosy.api.response.mocksession.LoadSessionsResponse;
import com.github.joergdev.mosy.api.response.record.LoadAllResponse;
import com.github.joergdev.mosy.api.response.record.LoadResponse;
import com.github.joergdev.mosy.api.response.record.SaveResponse;
import com.github.joergdev.mosy.api.response.system.LoadBaseDataResponse;
import com.github.joergdev.mosy.api.response.system.LoginResponse;

public class MosyApiClient
{
  private static final Logger LOG = Logger.getLogger(MosyApiClient.class);

  private enum HTTP_METHOD
  {
    GET, PUT, POST, DELETE;
  }

  private String token;

  public MosyApiClient()
  {

  }

  public MosyApiClient(String token)
  {
    this.token = token;
  }

  // ------------------ System -------------------------------
  public LoginResponse systemLogin(Integer hash)
  {
    LoginResponse response = invokeApiPostCall("system/login", LoginResponse.class, hash);

    this.token = response.getToken();

    return response;
  }

  public EmptyResponse systemLogout()
  {
    return invokeApiPostCall("system/logout", EmptyResponse.class, null);
  }

  public LoadBaseDataResponse systemLoadBasedata()
  {
    return invokeApiGetCall("system/load-basedata", LoadBaseDataResponse.class);
  }

  public EmptyResponse systemBoot()
  {
    return invokeApiPostCall("system/boot", EmptyResponse.class, null);
  }
  //------------------ End System -------------------------------

  //------------------ GlobalConfig -------------------------------

  public EmptyResponse globalConfigSave(BaseData basedata)
  {
    return invokeApiPostCall("globalconfig/save", EmptyResponse.class, basedata);
  }

  //------------------ End GlobalConfig ---------------------------

  //------------------ Records -------------------------------

  public LoadAllResponse loadRecords(Integer loadCount, Integer lastLoadedId)
  {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("load_count", loadCount);
    queryParams.put("last_loaded_id", lastLoadedId);

    return invokeApiGetCall("records", LoadAllResponse.class, queryParams);
  }

  public LoadResponse loadRecord(Integer id)
  {
    return invokeApiGetCall("records/" + id, LoadResponse.class);
  }

  public SaveResponse saveRecord(Record apiRecord)
  {
    return invokeApiPostCall("records/save", SaveResponse.class, apiRecord);
  }

  public EmptyResponse deleteRecord(Integer id)
  {
    return invokeApiDeleteCall("records/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End Records -------------------------------

  //------------------ Mocksessions -------------------------------

  public LoadSessionsResponse loadMocksessions()
  {
    return invokeApiGetCall("mock-sessions", LoadSessionsResponse.class);
  }

  public CreateResponse createMocksession()
  {
    return invokeApiPostCall("mock-sessions/create", CreateResponse.class, null);
  }

  public EmptyResponse deleteMocksession(Integer id)
  {
    return invokeApiDeleteCall("mock-sessions/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End Mocksessions -------------------------------

  //------------------ Interfaces -----------------------------------

  public com.github.joergdev.mosy.api.response._interface.LoadResponse loadInterface(Integer id)
  {
    return invokeApiGetCall("interfaces/" + id,
        com.github.joergdev.mosy.api.response._interface.LoadResponse.class);
  }

  public com.github.joergdev.mosy.api.response._interface.SaveResponse saveInterface(Interface apiInterface)
  {
    return invokeApiPostCall("interfaces/save",
        com.github.joergdev.mosy.api.response._interface.SaveResponse.class, apiInterface);
  }

  public EmptyResponse deleteInterface(Integer id)
  {
    return invokeApiDeleteCall("interfaces/" + id + "/delete", EmptyResponse.class);
  }

  public LoadMockDataResponse loadMethodMockData(Integer interfaceID, Integer methodID)
  {
    return invokeApiGetCall("interfaces/" + interfaceID + "/methods/" + methodID + "/mockdata",
        LoadMockDataResponse.class);
  }

  public LoadRecordConfigsResponse loadMethodRecordConfigs(Integer interfaceID, Integer methodID)
  {
    return invokeApiGetCall("interfaces/" + interfaceID + "/methods/" + methodID + "/recordconfigs",
        LoadRecordConfigsResponse.class);
  }

  //------------------ End Interfaces -------------------------------

  //------------------ RecordConfig -----------------------------------

  public com.github.joergdev.mosy.api.response.recordconfig.LoadResponse loadRecordConfig(Integer id)
  {
    return invokeApiGetCall("record-config/" + id,
        com.github.joergdev.mosy.api.response.recordconfig.LoadResponse.class);
  }

  public com.github.joergdev.mosy.api.response.recordconfig.SaveResponse saveRecordConfig(RecordConfig apiRecordConfig)
  {
    return invokeApiPostCall("record-config/save",
        com.github.joergdev.mosy.api.response.recordconfig.SaveResponse.class, apiRecordConfig);
  }

  public EmptyResponse deleteRecordConfig(Integer id)
  {
    return invokeApiDeleteCall("record-config/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End RecordConfig -------------------------------

  //------------------ MockData -----------------------------------

  public com.github.joergdev.mosy.api.response.mockdata.LoadResponse loadMockData(Integer id)
  {
    return invokeApiGetCall("mockdata/" + id,
        com.github.joergdev.mosy.api.response.mockdata.LoadResponse.class);
  }

  public com.github.joergdev.mosy.api.response.mockdata.SaveResponse saveMockData(MockData apiMockData)
  {
    return invokeApiPostCall("mockdata/save",
        com.github.joergdev.mosy.api.response.mockdata.SaveResponse.class, apiMockData);
  }

  public EmptyResponse deleteMockData(Integer id)
  {
    return invokeApiDeleteCall("mockdata/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End MockData -------------------------------

  //------------------ MockServices -----------------------------------

  public CustomRequestResponse customRequest(CustomRequestRequest request, Integer mockSessionID)
  {
    return invokeApiPostCall("mock-services/custom-request", CustomRequestResponse.class, request,
        mockSessionID);
  }

  //------------------ End MockServices -------------------------------

  // custom-request

  private <T extends AbstractResponse> T invokeApiGetCall(String path, Class<T> responseClass)
  {
    return invokeApiGetCall(path, responseClass, null);
  }

  private <T extends AbstractResponse> T invokeApiGetCall(String path, Class<T> responseClass,
                                                          Map<String, Object> queryParams)
  {
    return invokeApiCall(path, HTTP_METHOD.GET, responseClass, null, queryParams, null);
  }

  private <T extends AbstractResponse> T invokeApiPutCall(String path, Class<T> responseClass, Object entity)
  {
    return invokeApiCall(path, HTTP_METHOD.PUT, responseClass, entity, null, null);
  }

  private <T extends AbstractResponse> T invokeApiPostCall(String path, Class<T> responseClass, Object entity,
                                                           Integer mockSessionID)
  {
    return invokeApiCall(path, HTTP_METHOD.POST, responseClass, entity, null, mockSessionID);
  }

  private <T extends AbstractResponse> T invokeApiPostCall(String path, Class<T> responseClass, Object entity)
  {
    return invokeApiCall(path, HTTP_METHOD.POST, responseClass, entity, null, null);
  }

  private <T extends AbstractResponse> T invokeApiDeleteCall(String path, Class<T> responseClass)
  {
    return invokeApiCall(path, HTTP_METHOD.DELETE, responseClass, null, null, null);
  }

  private <T extends AbstractResponse> T invokeApiCall(String path, HTTP_METHOD method,
                                                       Class<T> responseClass, Object entity,
                                                       Map<String, Object> queryParams, Integer mockSessionID)
  {
    Client client = ClientBuilder.newClient();

    // Build endppoint (WebTarget)
    WebTarget webTarget = client.target(Resources.getProperty("api_endpoint"));

    StringTokenizer tok = new StringTokenizer(path, "/");
    while (tok.hasMoreTokens())
    {
      webTarget = webTarget.path(tok.nextToken());
    }

    // Query Params
    if (queryParams != null)
    {
      for (String queryParamKey : queryParams.keySet())
      {
        Object queryParamValue = queryParams.get(queryParamKey);

        if (queryParamValue != null)
        {
          webTarget = webTarget.queryParam(queryParamKey, queryParamValue);
        }
      }
    }

    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

    // set Token
    if (token != null)
    {
      invocationBuilder.header(HttpHeaders.AUTHORIZATION, token);
    }

    // set mockSessionID
    if (mockSessionID != null)
    {
      invocationBuilder.header(APIConstants.HTTP_HEADER_MOCK_SESSION_ID, String.valueOf(mockSessionID));
    }

    // invoke and check4error
    T response = invokeAndGetResponse(method, responseClass, entity, invocationBuilder,
        webTarget.getUri().toString());

    checkForError(response);

    return response;
  }

  private <T extends AbstractResponse> void checkForError(T response)
  {
    if (!response.isStateOK())
    {
      StringBuilder buiMsg = new StringBuilder();

      for (ResponseMessage rm : response.getMessagesForLevel(ResponseMessageLevel.FATAL,
          ResponseMessageLevel.ERROR))

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

      throw new IllegalStateException(buiMsg.toString());
    }
  }

  private <T extends AbstractResponse> T invokeAndGetResponse(HTTP_METHOD method, Class<T> responseClass,
                                                              Object entity,
                                                              Invocation.Builder invocationBuilder,
                                                              String endpoint)
  {
    T response = null;

    long timeStart = 0;

    if (LOG.isInfoEnabled())
    {
      LOG.info("Invoke API-Call " + endpoint);

      timeStart = System.currentTimeMillis();
    }

    if (HTTP_METHOD.GET.equals(method))
    {
      response = invocationBuilder.get(responseClass);
    }
    else if (HTTP_METHOD.PUT.equals(method))
    {
      response = invocationBuilder.put(Entity.entity(entity, MediaType.APPLICATION_JSON), responseClass);
    }
    else if (HTTP_METHOD.POST.equals(method))
    {
      response = invocationBuilder.post(Entity.entity(entity, MediaType.APPLICATION_JSON), responseClass);
    }
    else if (HTTP_METHOD.DELETE.equals(method))
    {
      response = invocationBuilder.delete(responseClass);
    }

    if (LOG.isInfoEnabled())
    {
      LOG.info("Invoked API-Call " + endpoint + "; time (ms): " + (System.currentTimeMillis() - timeStart));
    }

    return response;
  }
}