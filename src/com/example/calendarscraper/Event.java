package com.example.calendarscraper;

public class Event
{
    private String eName;
    private TimeStamp tStamp;
    public Event()
    {

    }
    public Event(String name, TimeStamp stamp)
    {
        seteName(name);
        settStamp(stamp);
    }
    public String geteName()
    {
        return eName;
    }
    public void seteName(String eName)
    {
        this.eName = eName;
    }
    public TimeStamp gettStamp()
    {
        return tStamp;
    }
    public void settStamp(TimeStamp tStamp)
    {
        this.tStamp = tStamp;
    }


}
