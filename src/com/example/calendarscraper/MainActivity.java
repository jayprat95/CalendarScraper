package com.example.calendarscraper;

import android.provider.CalendarContract.Events;
import android.content.ContentUris;
import android.net.Uri;
import java.util.ArrayList;
import android.content.ContentResolver;
import android.database.Cursor;
import java.util.Calendar;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import android.widget.Button;
import android.provider.CalendarContract.Calendars;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.provider.CalendarContract;

public class MainActivity
    extends Activity
{

    private String[] projection =
              new String[]{
                    Calendars._ID,
                    Calendars.NAME,
                    Calendars.ACCOUNT_NAME,
                    Calendars.ACCOUNT_TYPE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment()).commit();
        }

        Button button = (Button) findViewById(R.id.print);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            // function that will execute when watched button is clicked
            public void onClick(View v)
            {

                //listCalendars();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * lists all possible calendar events from start to end time for a user
     */
    public ArrayList<Event> listCalendarEventByTime(long beginTime, long endTime)
    {
        Cursor cursor = null;
        ContentResolver cr = getContentResolver();
        ArrayList<Event> completeList = new ArrayList<Event>();
        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
        //while you have more calendars
        while(cursor.moveToNext())
        {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String accountType = null;

            calID = cursor.getLong(0);
            displayName = cursor.getString(1);
            accountName = cursor.getString(2);
            accountType = cursor.getString(3);

            System.out.println("displayname: " + displayName);
            System.out.println("accountname: " + accountName);
            System.out.println("accounttype: " + accountType);
            //get list of events per calendar
            ArrayList<Event> subList = findEventsForCalendarByTime(beginTime, endTime, calID);
            completeList.addAll(subList);

        }

        cursor.close();
        return completeList;
    }

    public long findCalendar(String accountName, String accountType, String displayName)
    {
        String[] retCol = new String[] {
            CalendarContract.Calendars._ID,
        };
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND " + "(" +
        CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND " + "(" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?))";
        String[] selectionArg = {accountName, accountType, displayName};
        Cursor cursor = null;
        ContentResolver cr = getContentResolver();

        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, retCol, selection, selectionArg, null);

        long calendarID = -1;

        if(cursor.moveToNext())
        {
            calendarID = cursor.getLong(0);
        }
            return calendarID;
    }

    public ArrayList<Event> findEventsForCalendarByTime(long beginTime, long endTime, long calId)
    {
        ArrayList<Event> list = new ArrayList<Event>();
        // determine which fields we want in our events
        String[] EVENT_PROJECTION = new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.EVENT_LOCATION, CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Events.ALL_DAY};

        // retrieve the ContentResolver
        ContentResolver resolver = getContentResolver();

        // use the uri given by Instances, but in a way that we can add information to the URI
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        // add the begin and end times to the URI to use these to limit the list to events between them
        ContentUris.appendId(eventsUriBuilder, beginTime);
        ContentUris.appendId(eventsUriBuilder, endTime);

        // build the finished URI
        Uri eventUri = eventsUriBuilder.build();

        // filter the selection, like before
        String selection = "((" + Events.CALENDAR_ID + " = ?))";
        String[] selectionArgs = new String[]{"" + calId};

        // resolve the query, this time also including a sort option
        Cursor eventCursor = resolver.query(eventUri, EVENT_PROJECTION, selection, selectionArgs, CalendarContract.Instances.BEGIN + " ASC");
        //iterate through events
        while(eventCursor.moveToNext())
        {
           Event addEvent = new Event();
           TimeStamp tStamp = new TimeStamp();
           addEvent.seteName(eventCursor.getString(0));
           tStamp.setStartTime(eventCursor.getLong(2));
           tStamp.setEndTime(eventCursor.getLong(3));
           addEvent.settStamp(tStamp);
           list.add(addEvent);
        }
        eventCursor.close();
        return list;
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment
        extends Fragment
    {

        public PlaceholderFragment()
        {
        }


        @Override
        public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
        {
            View rootView =
                inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



}
