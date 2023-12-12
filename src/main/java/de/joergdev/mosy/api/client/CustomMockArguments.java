package de.joergdev.mosy.api.client;

public class CustomMockArguments
{
  private boolean mockEnabled;
  private String mockProfileName;
  private Integer recordSessionID;

  public boolean isMockEnabled()
  {
    return mockEnabled;
  }

  public void setMockEnabled(boolean mockEnabled)
  {
    this.mockEnabled = mockEnabled;
  }

  public String getMockProfileName()
  {
    return mockProfileName;
  }

  public void setMockProfileName(String mockProfileName)
  {
    this.mockProfileName = mockProfileName;
  }

  public Integer getRecordSessionID()
  {
    return recordSessionID;
  }

  public void setRecordSessionID(Integer recordSessionID)
  {
    this.recordSessionID = recordSessionID;
  }
}
