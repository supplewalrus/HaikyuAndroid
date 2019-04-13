package processor.haikyuapp.bottomNavBar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import processor.haikyuapp.R;


public class RecentEvents extends Fragment
{
    private static final String TAG = "EventsFragment";

    public static RecentEvents newInstance() {
        RecentEvents fragment = new RecentEvents();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageBitmaps();
        getActivity().setTitle("Recent Activities");
        Log.d(TAG, "onCreate: started.");
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_events,container,false);

        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new EventImageAdapter(getContext(), mNames, mImageUrls ));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Boating");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Dancing");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Hiking");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Reading");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Soccer");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Video Games");

    }

}
