package com.app.expandablelistviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter adapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = (ExpandableListView) findViewById(R.id.expLvData);

        expandableListDetail = ExpandableData.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        adapter = new Adapter(expandableListTitle, expandableListDetail,this);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition,
                                        int childPosition, long id) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition)+" -> "
                                +expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private class Adapter extends BaseExpandableListAdapter{

        private List<String> teams;
        private HashMap<String, List<String>> data;
        private Context context;

        Adapter(List<String> teams, HashMap<String, List<String>> data, Context context){
            this.teams = teams;
            this.data = data;
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return data.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return data.get(teams.get(i)).size();
        }


        @Override
        public Object getGroup(int listPosition) {
            return teams.get(listPosition);
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return data.get(teams.get(listPosition)).get(expandedListPosition);
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            String text = (String) getGroup(i);
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_row, viewGroup, false);
            }
            TextView tv = (TextView) view.findViewById(R.id.item);
            tv.setText(text);
            return view;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_expanded_item, parent, false);
            }
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandeditem);
            expandedListTextView.setText(expandedListText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
