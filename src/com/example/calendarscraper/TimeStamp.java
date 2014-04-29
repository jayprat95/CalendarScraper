package com.example.calendarscraper;

public class TimeStamp
{
    private long startTime;
    private long endTime;
    public TimeStamp()
    {

    }
    public TimeStamp(int start, int end)
    {
       setStartTime(start);
       setEndTime(end);
    }
    public long getStartTime()
    {
        return startTime;
    }
    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }
    public long getEndTime()
    {
        return endTime;
    }
    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }



}
