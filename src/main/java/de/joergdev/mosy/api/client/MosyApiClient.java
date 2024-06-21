package de.joergdev.mosy.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import de.joergdev.mosy.api.APIConstants;
import de.joergdev.mosy.api.model.BaseData;
import de.joergdev.mosy.api.model.HttpMethod;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.MockData;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.api.model.RecordConfig;
import de.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import de.joergdev.mosy.api.response.AbstractResponse;
import de.joergdev.mosy.api.response.EmptyResponse;
import de.joergdev.mosy.api.response.ResponseMessageLevel;
import de.joergdev.mosy.api.response._interface.method.LoadMockDataResponse;
import de.joergdev.mosy.api.response._interface.method.LoadRecordConfigsResponse;
import de.joergdev.mosy.api.response.mockprofile.LoadProfilesResponse;
import de.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import de.joergdev.mosy.api.response.record.LoadAllResponse;
import de.joergdev.mosy.api.response.record.LoadResponse;
import de.joergdev.mosy.api.response.record.SaveResponse;
import de.joergdev.mosy.api.response.record.session.CreateResponse;
import de.joergdev.mosy.api.response.record.session.LoadSessionsResponse;
import de.joergdev.mosy.api.response.system.LoadBaseDataResponse;
import de.joergdev.mosy.api.response.system.LoginResponse;

public class MosyApiClient
{
  private static final Logger LOG = Logger.getLogger(MosyApiClient.class);

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

  public LoadAllResponse loadRecords(Integer loadCount, Integer lastLoadedId, Integer recordSessionID)
  {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("load_count", loadCount);
    queryParams.put("last_loaded_id", lastLoadedId);
    queryParams.put("record_session_id", recordSessionID);

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

  public EmptyResponse deleteRecords()
  {
    return invokeApiDeleteCall("records/delete", EmptyResponse.class);
  }

  //------------------ End Records -------------------------------

  //------------------ Recordsessions -------------------------------

  public LoadSessionsResponse loadRecordSessions()
  {
    return invokeApiGetCall("record-sessions", LoadSessionsResponse.class);
  }

  public CreateResponse createRecordSession()
  {
    return invokeApiPostCall("record-sessions/create", CreateResponse.class, null);
  }

  public EmptyResponse deleteRecordSession(Integer id)
  {
    return invokeApiDeleteCall("record-sessions/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End Recordsessions -------------------------------

  //------------------ Mockprofiles -------------------------------

  public LoadProfilesResponse loadMockProfiles()
  {
    return invokeApiGetCall("mock-profiles", LoadProfilesResponse.class);
  }

  public de.joergdev.mosy.api.response.mockprofile.SaveResponse saveMockProfile(MockProfile apiMockProfile)
  {
    return invokeApiPostCall("mock-profiles/save", de.joergdev.mosy.api.response.mockprofile.SaveResponse.class, apiMockProfile);
  }

  public de.joergdev.mosy.api.response.mockprofile.LoadResponse loadMockProfile(Integer id)
  {
    return invokeApiGetCall("mock-profiles/" + id, de.joergdev.mosy.api.response.mockprofile.LoadResponse.class);
  }

  public EmptyResponse deleteMockProfile(Integer id)
  {
    return invokeApiDeleteCall("mock-profiles/" + id + "/delete", EmptyResponse.class);
  }

  public de.joergdev.mosy.api.response.mockprofile.LoadMockDataResponse loadMockProfileMockData(String name)
  {
    return loadMockProfileMockData((Object) name);
  }

  public de.joergdev.mosy.api.response.mockprofile.LoadMockDataResponse loadMockProfileMockData(Integer id)
  {
    return loadMockProfileMockData((Object) id);
  }

  private de.joergdev.mosy.api.response.mockprofile.LoadMockDataResponse loadMockProfileMockData(Object key)
  {
    return invokeApiGetCall("mock-profiles/" + key + "/mockdata", de.joergdev.mosy.api.response.mockprofile.LoadMockDataResponse.class);
  }

  //------------------ End Mockprofiles -------------------------------

  //------------------ Interfaces -----------------------------------

  public de.joergdev.mosy.api.response._interface.LoadResponse loadInterface(Integer id)
  {
    return invokeApiGetCall("interfaces/" + id, de.joergdev.mosy.api.response._interface.LoadResponse.class);
  }

  public de.joergdev.mosy.api.response._interface.SaveResponse saveInterface(Interface apiInterface)
  {
    return invokeApiPostCall("interfaces/save", de.joergdev.mosy.api.response._interface.SaveResponse.class, apiInterface);
  }

  public EmptyResponse deleteInterface(Integer id)
  {
    return invokeApiDeleteCall("interfaces/" + id + "/delete", EmptyResponse.class);
  }

  public LoadMockDataResponse loadMethodMockData(Integer interfaceID, Integer methodID)
  {
    return invokeApiGetCall("interfaces/" + interfaceID + "/methods/" + methodID + "/mockdata", LoadMockDataResponse.class);
  }

  public LoadRecordConfigsResponse loadMethodRecordConfigs(Integer interfaceID, Integer methodID)
  {
    return invokeApiGetCall("interfaces/" + interfaceID + "/methods/" + methodID + "/recordconfigs", LoadRecordConfigsResponse.class);
  }

  //------------------ End Interfaces -------------------------------

  //------------------ RecordConfig -----------------------------------

  public de.joergdev.mosy.api.response.recordconfig.LoadResponse loadRecordConfig(Integer id)
  {
    return invokeApiGetCall("record-config/" + id, de.joergdev.mosy.api.response.recordconfig.LoadResponse.class);
  }

  public de.joergdev.mosy.api.response.recordconfig.SaveResponse saveRecordConfig(RecordConfig apiRecordConfig)
  {
    return invokeApiPostCall("record-config/save", de.joergdev.mosy.api.response.recordconfig.SaveResponse.class, apiRecordConfig);
  }

  public EmptyResponse deleteRecordConfig(Integer id)
  {
    return invokeApiDeleteCall("record-config/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End RecordConfig -------------------------------

  //------------------ MockData -----------------------------------

  public de.joergdev.mosy.api.response.mockdata.LoadResponse loadMockData(Integer id)
  {
    return invokeApiGetCall("mockdata/" + id, de.joergdev.mosy.api.response.mockdata.LoadResponse.class);
  }

  public de.joergdev.mosy.api.response.mockdata.SaveResponse saveMockData(MockData apiMockData)
  {
    return invokeApiPostCall("mockdata/save", de.joergdev.mosy.api.response.mockdata.SaveResponse.class, apiMockData);
  }

  public EmptyResponse deleteMockData(Integer id)
  {
    return invokeApiDeleteCall("mockdata/" + id + "/delete", EmptyResponse.class);
  }

  //------------------ End MockData -------------------------------

  //------------------ MockServices -----------------------------------

  public CustomRequestResponse customRequest(CustomRequestRequest request, String mockProfileName, Integer recordSessionID)
  {
    return invokeApiPostCall("mock-services/custom-request", CustomRequestResponse.class, request, mockProfileName, recordSessionID);
  }

  //------------------ End MockServices -------------------------------

  // custom-request

  private <T extends AbstractResponse> T invokeApiGetCall(String path, Class<T> responseClass)
  {
    return invokeApiGetCall(path, responseClass, null);
  }

  private <T extends AbstractResponse> T invokeApiGetCall(String path, Class<T> responseClass, Map<String, Object> queryParams)
  {
    return invokeApiCall(path, HttpMethod.GET, responseClass, null, queryParams, null, null);
  }

  private <T extends AbstractResponse> T invokeApiPutCall(String path, Class<T> responseClass, Object entity)
  {
    return invokeApiCall(path, HttpMethod.PUT, responseClass, entity, null, null, null);
  }

  private <T extends AbstractResponse> T invokeApiPostCall(String path, Class<T> responseClass, Object entity, String mockProfileName, Integer recordSessionID)
  {
    return invokeApiCall(path, HttpMethod.POST, responseClass, entity, null, mockProfileName, recordSessionID);
  }

  private <T extends AbstractResponse> T invokeApiPostCall(String path, Class<T> responseClass, Object entity)
  {
    return invokeApiCall(path, HttpMethod.POST, responseClass, entity, null, null, null);
  }

  private <T extends AbstractResponse> T invokeApiDeleteCall(String path, Class<T> responseClass)
  {
    return invokeApiCall(path, HttpMethod.DELETE, responseClass, null, null, null, null);
  }

  private <T extends AbstractResponse> T invokeApiCall(String path, HttpMethod method, Class<T> responseClass, Object entity, Map<String, Object> queryParams,
                                                       String mockProfileName, Integer recordSessionID)
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

    // set mockProfileID
    if (mockProfileName != null)
    {
      invocationBuilder.header(APIConstants.HTTP_HEADER_MOCK_PROFILE_NAME, mockProfileName);
    }

    // set recordSessionID
    if (recordSessionID != null)
    {
      invocationBuilder.header(APIConstants.HTTP_HEADER_RECORD_SESSION_ID, String.valueOf(recordSessionID));
    }

    // invoke and check4error
    T response = invokeAndGetResponse(method, responseClass, entity, invocationBuilder, webTarget.getUri().toString());

    checkForError(response);

    return response;
  }

  private <T extends AbstractResponse> void checkForError(T response)
  {
    Objects.requireNonNull(response);

    if (!response.isStateOK())
    {
      throw new MosyApiClientException(response.getMessagesForLevel(ResponseMessageLevel.FATAL, ResponseMessageLevel.ERROR));
    }
  }

  private <T extends AbstractResponse> T invokeAndGetResponse(HttpMethod method, Class<T> responseClass, Object entity, Invocation.Builder invocationBuilder,
                                                              String endpoint)
  {
    T response = null;

    long timeStart = 0;

    if (LOG.isInfoEnabled())
    {
      LOG.info("Invoke API-Call " + endpoint);

      timeStart = System.currentTimeMillis();
    }

    if (HttpMethod.GET.equals(method))
    {
      response = invocationBuilder.get(responseClass);
    }
    else if (HttpMethod.PUT.equals(method))
    {
      response = invocationBuilder.put(Entity.entity(entity, MediaType.APPLICATION_JSON), responseClass);
    }
    else if (HttpMethod.POST.equals(method))
    {
      response = invocationBuilder.post(Entity.entity(entity, MediaType.APPLICATION_JSON), responseClass);
    }
    else if (HttpMethod.DELETE.equals(method))
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
