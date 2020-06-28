package com.xinkao.skmvp.base;

import android.content.Context;

import com.google.gson.Gson;
import com.xinkao.skmvp.utils.SharedPreferencesUtils;

import java.lang.reflect.Field;

public class BaseBean {

    public void saveAsJson(Context context) {
        SharedPreferencesUtils.saveString(context, getKey(), new Gson().toJson(this));
    }

    public Boolean loadBean(Context context) {

        String data = SharedPreferencesUtils.getString(context,getKey(),null);

        if (data != null) {
            copy(new Gson().fromJson(data, this.getClass()));
            return true;
        }
        return false;
    }

//    public final static class Lodder {
//
//        private Context context;
//
//        public Lodder(Context context) {
//            this.context = context;
//        }
//
//        public <E extends BaseBean> E loadBean(Class<? extends BaseBean> c) {
//            try {
//                BaseBean obj = c.newInstance();
//
//                String data = SharedPreferencesUtil.getString(context,obj.getKey(),null);
//
//                if (data != null) {
////                ResponseEntityToModule.parseJsonToModule(data,obj.getClass())
//                    obj.copy(new Gson().fromJson(data, obj.getClass()));
//                    return (E) obj;
//                }
//
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }

    public static final  <E extends BaseBean> E loadBean(Class<? extends BaseBean> c, Context context) {

        try {
            BaseBean obj = c.newInstance();

            String data = SharedPreferencesUtils.getString(context,obj.getKey(),null);

            if (data != null) {
//                ResponseEntityToModule.parseJsonToModule(data,obj.getClass())
                obj.copy(new Gson().fromJson(data, obj.getClass()));
                return (E) obj;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getKey() {
        String key = getClass().getName();

        return key;
    }

    public void copy(Object object) {
        Field[] tFields = this.getClass().getDeclaredFields();
        Field[] oFields = object.getClass().getDeclaredFields();

        for (int i=0;i<tFields.length;i++) {
            Field tF = tFields[i];
            Field oF = oFields[i];
            tF.setAccessible(true);
            oF.setAccessible(true);
            try {

                tF.set(this,oF.get(object));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
