package de.joergdev.mosy.api.client;

import org.junit.Ignore;
import org.junit.Test;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.Record;

@Ignore
public class MosyApiClientAdhocTest
{
  @Test
  public void saveRecord()
  {
    MosyApiClient apiClient = new MosyApiClient();

    String token = apiClient.systemLogin("m0sy".hashCode()).getToken();

    apiClient = new MosyApiClient(token);

    Record record = new Record();
    record.setRequestData("Req" + System.currentTimeMillis());
    record.setResponse("Resp" + System.currentTimeMillis());

    InterfaceMethod apiMethod = new InterfaceMethod();
    apiMethod.setInterfaceMethodId(97);

    record.setInterfaceMethod(apiMethod);

    Integer id = apiClient.saveRecord(record).getRecordID();
    System.out.println("ID Saved: " + id);

    apiClient.systemLogout();
  }
}