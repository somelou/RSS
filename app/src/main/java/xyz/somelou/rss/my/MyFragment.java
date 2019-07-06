package xyz.somelou.rss.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.somelou.rss.R;
import xyz.somelou.rss.my.myGroup.MyGroupRecyclerActivity;
import xyz.somelou.rss.my.myfavorite.FavoriteActivity;

public class MyFragment extends Fragment {
    private Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



//        if(getActivity()!=null){
//
//            getActivity().findViewById(R.id.layout_pinned_header_linear).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MyGroupRecyclerActivity.startUp(mContext);
//                }
//            });
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        mContext=getContext();
        view.findViewById(R.id.layout_pinned_header_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGroupRecyclerActivity.startUp(mContext);
            }
        });
        view.findViewById(R.id.myFavorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
