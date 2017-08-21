package com.artgallery.tool;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseJsonCopy {

    public  void getData(String response, Context context) throws Exception {
        JSONObject obj = new JSONObject(response);

        EmergencyDBHelper emergencyDBHelper = new EmergencyDBHelper(context);
        FullscreenDBHelper fullscreenDBHelper = new FullscreenDBHelper(context);
        PermanentDBHelper permanentDBHelper = new PermanentDBHelper(context);

        SharedPreferences sp = context.getSharedPreferences("SP",
                context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("fullChange", false);
        editor.commit();
        String etaskid = "";
        String econtent = "";

        // 数据录入
        String ptaskid = "";
        String psrc = "";
        String plink = "";
        String httpUrl=obj.getString("httpUrl");
        if (obj.getJSONObject("permanent").length() > 0) {
            permanentDBHelper.delete();
            System.out.println("permanent clean");
            ptaskid = obj.getJSONObject("permanent").getString("taskid");// 首页任务id

            editor.putInt("imghas", 1);
            JSONArray a = obj.getJSONObject("permanent").getJSONObject("data")
                    .getJSONArray("index");
            for (int i = 0; i < a.length(); i++) {
                psrc = httpUrl+a.getJSONObject(i).getString("photo");// 图片资源url
                psrc = new String(psrc.getBytes("UTF-8"), "UTF-8");
                plink = a.getJSONObject(i).getString("url");// 图片跳转url
                permanentDBHelper.insert(ptaskid, i, psrc, plink);
            }

        }

        // 紧急
        emergencyDBHelper.delete();
        if (obj.getJSONObject("emergency").length() > 0) {
              etaskid = obj.getJSONObject("emergency").getString("taskid");
              econtent = obj.getJSONObject("emergency").getString("content");
        }
        if (!etaskid.equals("")) {
            // 数据录入
            Log.e("ParseJsonCopy","紧急任务数据录入完成");
            emergencyDBHelper.insert(etaskid, econtent);
            editor.putBoolean("jinji", true);// 是否有紧急任务
            editor.putBoolean("NULL", true);// 全屏是否为空 为空

            //只要有紧急就把全屏任务数据删除掉
            editor.putBoolean("fullChange", false);
            editor.putString("ftaskid", "null");
            fullscreenDBHelper.delete();
        } else {
            emergencyDBHelper.delete();// 清空紧急数据库表
            editor.putBoolean("jinji", false);// 是否有紧急任务
            Log.e("ParseJsonCopy","没有紧急任务数据");
            //全屏任务
            if (obj.getJSONObject("fullscreen").length() > 0) {
                String keepid = sp.getString("ftaskid", "null");
                String ftaskid = obj.getJSONObject("fullscreen")
                        .getString("taskid");
                if (keepid.equals(ftaskid)) {
                    // 无变动时
                    Log.e("ParseJsonCopy","taskid没变");
                } else {
                    editor.putBoolean("fullChange", true);
                    editor.putString("ftaskid", ftaskid);
                    editor.commit();

                    int fmaterialid = 0;
                    int fcontentid = 0;
                    int fsrcid = 0;

                    String fmodel = "";
                    String fduration = "";
                    String ftype = "";
                    String fareaid = "";
                    String fcount = "";
                    String fsrc = "";
                    String flink = "";
                    String fdlpath = "false";
                    fullscreenDBHelper.delete();

                    JSONObject fullJob = obj.getJSONObject("fullscreen");
                    JSONArray materialArray = fullJob.getJSONArray("material");
                    for (int i = 0; i < materialArray.length(); i++) {
                        Log.e("ParseJsonCopy","解析全屏任务数据");
                        fmaterialid = i;
                        int model = 0;
                        if (materialArray.getJSONObject(i).getString("model").equals("moban1")) {
                            model = 1;
                        } else if (materialArray.getJSONObject(i).getString("model")
                                .equals("moban2")) {
                            model = 2;
                        } else if (materialArray.getJSONObject(i).getString("model")
                                .equals("moban3")) {
                            model = 3;
                        } else if (materialArray.getJSONObject(i).getString("model")
                                .equals("moban4")) {
                            model = 4;
                        } else if (materialArray.getJSONObject(i).getString("model")
                                .equals("moban5")) {
                            model = 5;
                        }
                        fmodel = model + "";
                        fduration = materialArray.getJSONObject(i).getString("duration");// 时长
                        JSONObject contentJob ;

                        switch (model) {
                            case 1:
                                Log.e("ParseJsonCopy","模板1-->富文本");
                                contentJob = materialArray.getJSONObject(i).getJSONObject("content");
                                fcount = contentJob.getString("count");
                                fareaid = contentJob.getString("areaid");
                                ftype = contentJob.getString("type");
                                fsrcid = 0;
                                String model1_src = sp.getString("IPaddress","http://192.168.20.160:8088")+"/cirmSystem"+contentJob.getString("src");
                                fsrc = new String(model1_src.getBytes("UTF-8"), "UTF-8");
                                fcontentid=fmaterialid;
                                fullscreenDBHelper.insert(ftaskid, fmaterialid,
                                        fmodel, fduration, fcontentid, ftype,
                                        fcount, fareaid, fsrcid, fsrc, fdlpath,
                                        flink);
                                break;
                            case 2:
                                Log.e("ParseJsonCopy","模板2-->图片-视频-图片");
                                JSONArray contentJa = materialArray.getJSONObject(i).getJSONArray("content");
                                for (int k = 0; k < contentJa.length(); k++) {
                                    fcontentid=k;
                                    JSONObject jsonObject = contentJa.getJSONObject(k);
                                    fcount = jsonObject.getString("count");
                                    fareaid = jsonObject.getString("areaid");
                                    ftype = jsonObject.getString("type");
                                    JSONArray srcJa = jsonObject.getJSONArray("src");
                                    for (int l = 0; l <srcJa.length(); l++) {
                                        fsrcid=l;
                                        String path = httpUrl+srcJa.getString(l);
                                        fsrc = new String(path.getBytes("UTF-8"), "UTF-8");
                                        fullscreenDBHelper.insert(ftaskid, fmaterialid,
                                                fmodel, fduration, fcontentid, ftype,
                                                fcount, fareaid, fsrcid, fsrc, fdlpath,
                                                flink);
                                    }

                                }
                                break;
                            case 3:
                                Log.e("ParseJsonCopy","模板3-->富文本");
                                contentJob = materialArray.getJSONObject(i).getJSONObject("content");
                                fcount = contentJob.getString("count");
                                fareaid = contentJob.getString("areaid");
                                ftype = contentJob.getString("type");
                                fsrcid = 0;
                                String model3_src = sp.getString("IPaddress","http://192.168.20.160:8088")+"/cirmSystem"+contentJob.getString("src");
                                fsrc = new String(model3_src.getBytes("UTF-8"), "UTF-8");
                                fcontentid=fmaterialid;
                                fullscreenDBHelper.insert(ftaskid, fmaterialid,
                                        fmodel, fduration, fcontentid, ftype,
                                        fcount, fareaid, fsrcid, fsrc, fdlpath,
                                        flink);

                                break;
                            case 4:
                                Log.e("ParseJsonCopy","模板4-->5图");
                                contentJob = materialArray.getJSONObject(i).getJSONObject("content");
                                fcount = contentJob.getString("count");
                                fareaid = contentJob.getString("areaid");
                                ftype = contentJob.getString("type");

                                JSONArray model4_srcJa = contentJob.getJSONArray("src");
                                for (int k = 0; k < model4_srcJa.length(); k++) {
                                    fsrcid = k;
                                    JSONObject jo =  model4_srcJa.getJSONObject(k);
                                    String url=httpUrl+jo.getString("photo");
                                    if(jo.has("url")){
                                        flink=httpUrl+jo.getString("url");
                                        fsrc = new String(url.getBytes("UTF-8"), "UTF-8");
                                    }
                                    fcontentid=fmaterialid;
                                    fullscreenDBHelper.insert(ftaskid, fmaterialid,
                                            fmodel, fduration, fcontentid, ftype,
                                            fcount, fareaid, fsrcid, fsrc, fdlpath,
                                            flink);
                                }
                                break;
                            case 5:
                                Log.e("ParseJsonCopy","模板5-->富文本");
                                contentJob = materialArray.getJSONObject(i).getJSONObject("content");
                                String model5_src =sp.getString("IPaddress","http://192.168.20.160:8088")+"/cirmSystem"+contentJob.getString("src");
                                fcount = contentJob.getString("count");
                                fareaid = contentJob.getString("areaid");
                                ftype = contentJob.getString("type");
                                fsrcid = 0;
                                fsrc = new String(model5_src.getBytes("UTF-8"), "UTF-8");
                                fcontentid=fmaterialid;
                                fullscreenDBHelper.insert(ftaskid, fmaterialid,
                                        fmodel, fduration, fcontentid, ftype,
                                        fcount, fareaid, fsrcid, fsrc, fdlpath,
                                        flink);
                                break;

                        }

                    }
                    editor.putBoolean("NULL", false);// 全屏是否为空 不为空
                    Log.e("ParseJsonCopy","全屏录入完成parse_json");
                }
            }else{
                Log.e("ParseJsonCopy","全屏无数据parse_json");
                editor.putBoolean("fullChange", true);
                editor.putString("ftaskid", "null");
                fullscreenDBHelper.delete();// 清空全屏数据库表
                editor.putBoolean("NULL", true);// 全屏是否为空 为空
            }
        }
        editor.commit();
    }

}
