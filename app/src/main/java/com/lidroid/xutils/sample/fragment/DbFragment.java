package com.lidroid.xutils.sample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.sample.R;
import com.lidroid.xutils.sample.entities.Child;
import com.lidroid.xutils.sample.entities.NewsDetail;
import com.lidroid.xutils.sample.entities.Parent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-9-14
 * Time: 下午3:35
 */
public class DbFragment extends Fragment {

    private DbUtils db;             //数据库设置
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.db_fragment, container, false);
        ViewUtils.inject(this, view);

        db = DbUtils.create(this.getActivity());
        db.configAllowTransaction(true);
        db.configDebug(true);

        return view;
    }

    @ViewInject(R.id.result_txt)
    private TextView resultText;

    private void orderTest() {
        String temp = "";

        Parent parent = new Parent();
        parent.name = "测试" + System.currentTimeMillis();
        parent.setAdmin(true);
        parent.setEmail("wyouflf@gmail.com");

        /*Parent parent2 = new Parent();
        parent2.name = "测试2";
        parent2.isVIP = false;*/

        try {

            //DbUtils db = DbUtils.create(this.getActivity(), "/sdcard/", "test.db");
            DbUtils db = DbUtils.create(this.getActivity());
            db.configAllowTransaction(true);
            db.configDebug(true);

            Child child = new Child();
            child.name = "child' name";
            //db.saveBindingId(parent);
            //child.parent = new ForeignLazyLoader<Parent>(Child.class, "parentId", parent.getId());
            //child.parent = parent;

            Parent test = db.findFirst(Selector.from(Parent.class).where("id", "in", new int[]{1, 3, 6}));
            //Parent test = db.findFirst(Selector.from(Parent.class).where("id", "between", new String[]{"1", "5"}));
            if (test != null) {
                child.parent = test;
                temp += "first parent:" + test + "\n";
                resultText.setText(temp);
            } else {
                child.parent = parent;
            }

            parent.setTime(new Date());
            parent.setDate(new java.sql.Date(new Date().getTime()));

            db.saveBindingId(child);//保存对象关联数据库生成的id

            List<Child> children = db.findAll(Selector.from(Child.class));//.where(WhereBuilder.b("name", "=", "child' name")));
            temp += "children size:" + children.size() + "\n";
            resultText.setText(temp);
            if (children.size() > 0) {
                temp += "last children:" + children.get(children.size() - 1) + "\n";
                resultText.setText(temp);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            calendar.add(Calendar.HOUR, 3);

            List<Parent> list = db.findAll(
                    Selector.from(Parent.class)
                            .where("id", "<", 54)
                            .and("time", ">", calendar.getTime())
                            .orderBy("id")
                            .limit(10));
            temp += "find parent size:" + list.size() + "\n";
            resultText.setText(temp);
            if (list.size() > 0) {
                temp += "last parent:" + list.get(list.size() - 1) + "\n";
                resultText.setText(temp);
            }

            //parent.name = "hahaha123";
            //db.update(parent);

            Parent entity = db.findById(Parent.class, child.parent.getId());
            temp += "find by id:" + entity.toString() + "\n";
            resultText.setText(temp);

            List<DbModel> dbModels = db.findDbModelAll(Selector.from(Parent.class)
                    .groupBy("name")
                    .select("name", "count(name) as count"));
            temp += "group by result:" + dbModels.get(0).getDataMap() + "\n";
            resultText.setText(temp);

        } catch (DbException e) {
            temp += "error :" + e.getMessage() + "\n";
            resultText.setText(temp);
        }
    }

    private void testDbName() {
        NewsDetail detail = new NewsDetail();
        detail.setNewsId("102");
        detail.setNewstime(100000000);
        detail.setNewscontent("测试测试内容<html><body><a href=\"www.baidu.com\">百度地址</a><br/><p>就这样</p></body></html>");

        try {

            db.saveBindingId(detail);
        } catch (DbException e) {
            e.printStackTrace();
        }
        ArrayList<NewsDetail> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            NewsDetail obj = new NewsDetail();
            obj.setNewsId("newsId" + i);
            obj.setNewstype(1);
            obj.setNewscontent("content" + i);
            obj.setNewstime((int) (new Date().getTime() / 1000));
            list.add(obj);
        }
        try {
            db.saveAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.db_test_btn)
    public void testDb(View view) {
        testDbName();
    }

    @OnClick(R.id.select_btn)
    public void selectResult(View view) {
        try {
            NewsDetail detail = db.findFirst(Selector.from(NewsDetail.class).where("newsid","in",new String[]{"102","newsId2"}));

            if(detail != null){
//                resultText.setText(detail.toString());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            List<NewsDetail> list = db.findAll(Selector.from(NewsDetail.class).orderBy("newsid",true).limit(10));
            if(list != null){
                int length = list.size();
                String resultStr="";
                for (int i=0;i<length;i++){
                    NewsDetail detail = list.get(i);
                    resultStr += detail.toString();
                }
                resultText.setText(resultStr);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
