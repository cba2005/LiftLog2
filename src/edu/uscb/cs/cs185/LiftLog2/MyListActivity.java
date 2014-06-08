package edu.uscb.cs.cs185.LiftLog2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Caressa on 6/8/2014.
 */
public class MyListActivity extends FragmentActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getSupportFragmentManager().findFragmentById(R.id.listView) == null) {
                MyListFragment list = new MyListFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.listView, list).commit();
            }
        }

        public class MyListFragment extends ListFragment {

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                super.onCreateView(inflater, container, savedInstanceState);
                return inflater.inflate(R.layout.row_layout, container);
            }

            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                String List[] = {"Larry", "Moe", "Curly"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, List);
                setListAdapter(adapter);
            }
        }
    }