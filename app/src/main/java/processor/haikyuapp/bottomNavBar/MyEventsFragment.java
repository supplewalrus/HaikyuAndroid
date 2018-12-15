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


public class MyEventsFragment extends Fragment
{
    private static final String TAG = "EventsFragment";

    public static MyEventsFragment newInstance() {
        MyEventsFragment fragment = new MyEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageBitmaps();
        Log.d(TAG, "onCreate: started.");
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events,container,false);

        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerv_view);
        recyclerView.setAdapter(new EventImageAdapter(getContext(), mNames, mImageUrls ));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Adventures");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Board and Card Games");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Boating");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Cultural");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Dancing");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Football");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("Frisbee Golf");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Hiking");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Longboarding");



        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Miscellaneous");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Mountain Biking");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Movies and TV");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Reading");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Rock Climbing");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Jogging");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("Ski and Snowboarding");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Slack Lining");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Soccer");




        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Social");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Spikeball");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Study Groups");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Tennis");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Ultimate Frisbee");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Video Games");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("Yoga");
    }

}
