package edu.nedu.nedu_library.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import edu.nedu.nedu_library.R;

/**
 * Created by 小呓的欧尼酱 on 2017/4/17.
 */

public class FAQExpandableListAdapter implements ExpandableListAdapter {

    private Context mContext;
    private List<String> questions;
    private List<String> answers;

    public FAQExpandableListAdapter(){
        super();
    }

    public FAQExpandableListAdapter(Context mContext, List<String> questions, List<String> answers){
        this.mContext = mContext;
        this.questions = questions;
        this.answers = answers;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return questions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return questions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answers.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView tvNumber = null, tvQuestion = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_faq_group_layout, null);
            tvNumber = (TextView)view.findViewById(R.id.tvNumber);
            tvQuestion = (TextView)view.findViewById(R.id.tvQuestion);
            view.setTag(R.id.tvNumber,tvNumber);
            view.setTag(R.id.tvQuestion,tvQuestion);
        }else{
            tvNumber = (TextView)view.getTag(R.id.tvNumber);
            tvQuestion = (TextView)view.getTag(R.id.tvQuestion);
        }

        //判断是否已经打开列表
        if(isExpanded){
            //holder.arrow.setBackgroundResource(R.drawable.dowm_arrow);
        }else{
           //holder.arrow.setBackgroundResource(R.drawable.right_arrow);
        }

        tvNumber.setText((groupPosition + 1) + ".");
        tvQuestion.setText(questions.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView tvAnswer = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_faq_child_layout, null);
            tvAnswer = (TextView)view.findViewById(R.id.tvAnswer);
            view.setTag(tvAnswer);
        }else{
            tvAnswer = (TextView)view.getTag();
        }
        tvAnswer.setText(answers.get(groupPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return answers.isEmpty();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
