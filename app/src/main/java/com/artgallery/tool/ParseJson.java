package com.artgallery.tool;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//import android.database.Cursor;
import com.artgallery.data.ClassicBookListData;
import com.artgallery.data.ClassicBookListData.Data;
import com.artgallery.data.RemenData;
import com.artgallery.data.UrlData;

public class ParseJson {

	public String parseLogin(InputStream inStream) throws Exception {
		String LoginResult;
		byte[] data = StreamTool.read(inStream);// 从输入流中读取数据
		String json = new String(data);// 转化为字符串，例如[{xxx,xx,x},{xxx,xx,x}]
		JSONObject jsonObject = new JSONObject(json);
		LoginResult = jsonObject.getString("status");
		return LoginResult;
	}

	public ArrayList<String> parseKeChengContent(String response) {
		// TODO 自动生成的方法存根
		return null;
	}

	public void parseClassicBookList(InputStream inStream,
			List<Data> iMG_DESCRIPTIONS) {
		// TODO 自动生成的方法存根

	}

	public void parseClassicBookContent(
			List<com.artgallery.data.ClassicBookListData.Data> iMG_DESCRIPTIONS,
			String response) throws Exception {
		// TODO 自动生成的方法存根
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("datas");
			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				iMG_DESCRIPTIONS.add(new ClassicBookListData.Data(jsonArray
						.getJSONObject(i).getString("bookid"), jsonArray
						.getJSONObject(i).getString("imgurl"), jsonArray
						.getJSONObject(i).getString("bookname"), jsonArray
						.getJSONObject(i).getString("author")));
			}
		}

	}

	public ArrayList<String> parseBookContent(String response) throws Exception {
		// TODO 自动生成的方法存根
		JSONObject jsonObject = new JSONObject(response);
		ArrayList<String> dataArrayList = new ArrayList<String>();
		if (jsonObject.getString("status").equals("0")) {

			String bookname;
			String author;
			String pub;
			String imgurl;
			String pubtime;
			String intro;
			String dir;
			String updatetime;
			String pdfurl;
			Boolean has;
			String aString = "0";
			has = jsonObject.has("epuburl");
			if (has) {
				aString = "1";
			}
			bookname = jsonObject.getString("bookname");
			author = jsonObject.getString("author");
			pub = jsonObject.getString("pub");
			imgurl = jsonObject.getString("imgurl");
			pubtime = jsonObject.getString("pubtime");
			intro = jsonObject.getString("intro");
			dir = jsonObject.getString("dir");
			updatetime = jsonObject.getString("updatetime");
			pdfurl = jsonObject.getString("pdfurl");
			dataArrayList.add(bookname);
			dataArrayList.add(author);
			dataArrayList.add(pub);
			dataArrayList.add(imgurl);
			dataArrayList.add(pubtime);
			dataArrayList.add(intro);
			dataArrayList.add(dir);
			dataArrayList.add(updatetime);
			dataArrayList.add(pdfurl);
			dataArrayList.add(aString);

		}
		return dataArrayList;

	}

	public ArrayList<String> parseKcContent(String response) throws Exception {
		// TODO 自动生成的方法存根
		JSONObject jsonObject = new JSONObject(response);
		ArrayList<String> dataArrayList = new ArrayList<String>();
		if (jsonObject.getString("status").equals("0")) {

			String name;
			String thief;
			String imgurl;
			String id;
			String info;
			String chapters;
			String cat;

			name = jsonObject.getString("name");
			thief = jsonObject.getString("chief");
			id = jsonObject.getString("id");
			imgurl = jsonObject.getString("imgurl");
			chapters = jsonObject.getString("chapters");
			info = jsonObject.getString("info");
			cat = jsonObject.getString("cat");
			dataArrayList.add(name);
			dataArrayList.add(thief);
			dataArrayList.add(imgurl);
			dataArrayList.add(id);
			dataArrayList.add(info);
			dataArrayList.add(chapters);
			dataArrayList.add(cat);
		}
		return dataArrayList;

	}

	public void parseRemenContent(
            List<com.artgallery.data.RemenData.Data> iMG_DESCRIPTIONS, String response)
			throws Exception {
		// TODO 自动生成的方法存根
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("datas");

			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				iMG_DESCRIPTIONS.add(new RemenData.Data(jsonArray
						.getJSONObject(i).getString("name"), jsonArray
						.getJSONObject(i).getString("chief"), jsonArray
						.getJSONObject(i).getString("imgurl"), jsonArray
						.getJSONObject(i).getString("id")));
			}
		}

	}

	public ArrayList<String> parseKechengFather(String response)
			throws Exception {
		ArrayList<String> arrayList = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("chapters");
			for (int i = 0; i < jsonArray.length(); i++) {
				arrayList.add(jsonArray.getJSONObject(i).getString("name"));
			}
		}
		return arrayList;
	}

	public List<UrlData> parseKechengChild(String response, int num)
			throws Exception {
		List<UrlData> arrayList = new ArrayList<UrlData>();
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("chapters")
					.getJSONObject(num).getJSONArray("datas");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				UrlData urlData = new UrlData(object.getString("name"),
						object.getString("url"));
				arrayList.add(urlData);
			}
		}
		return arrayList;
	}

	public ArrayList<String> parseNewBookContent(String response)
			throws Exception {
		// TODO 自动生成的方法存根
		JSONObject jsonObject = new JSONObject(response);
		ArrayList<String> dataArrayList = new ArrayList<String>();
		if (jsonObject.getString("status").equals("0")) {

			String name;
			String author;
			String imgurl;
			String pub;
			String pubtime;
			String pages;
			String isbn;
			String theclass;
			String bjtj;
			String nrtj;
			String zztj;
			String dirtj;

			name = jsonObject.getString("bookname");
			author = jsonObject.getString("author");
			pub = jsonObject.getString("pub");
			imgurl = jsonObject.getString("imgurl");
			pubtime = jsonObject.getString("pubtime");
			pages = jsonObject.getString("pages");
			isbn = jsonObject.getString("isbn");
			theclass = jsonObject.getString("class");
			bjtj = jsonObject.getString("bjtj");
			nrtj = jsonObject.getString("nrtj");
			zztj = jsonObject.getString("intro");
			dirtj = jsonObject.getString("dir");
			dataArrayList.add(name);
			dataArrayList.add(author);
			dataArrayList.add(imgurl);
			dataArrayList.add(pub);
			dataArrayList.add(pubtime);
			dataArrayList.add(pages);
			dataArrayList.add(isbn);
			dataArrayList.add(theclass);
			dataArrayList.add(bjtj);
			dataArrayList.add(nrtj);
			dataArrayList.add(zztj);
			dataArrayList.add(dirtj);

		}
		return dataArrayList;

	}

	public void parseHuoDongList(String response,
			List<com.artgallery.data.HuoDongListData.Data> listdata) throws Exception {
		JSONObject object = new JSONObject(response);
		JSONArray jsonArray = object.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			listdata.add(new com.artgallery.data.HuoDongListData.Data(jsonObject
					.getString("cover"), jsonObject.getString("title"),
					jsonObject.getString("dtBegin"), jsonObject
							.getString("unit"), jsonObject
							.getString("registCount"), jsonObject
							.getString("credit"), jsonObject.getString("id")));
		}
	}

	public void parseHuoDongDetail(String response, Map<String, String> data)
			throws Exception {
		JSONObject jsonObject = new JSONObject(response);
		data.put("status", jsonObject.getString("status"));
		data.put("begin_at", jsonObject.getString("begin_at"));
		data.put("qr_checkin", jsonObject.getString("qr_checkin"));
		data.put("deadline", jsonObject.getString("deadline"));

		JSONObject user = jsonObject.getJSONObject("user");
		data.put("attend", user.getString("attend"));
		data.put("access", user.getString("access"));
		// data.put("sign_at", user.getString("sign_at"));
		// data.put("checkin_at", user.getString("checkin_at"));

		data.put("id", jsonObject.getString("id"));
		data.put("huodong_name", jsonObject.getString("name"));

		JSONObject author = jsonObject.getJSONObject("author");
		data.put("account", author.getString("account"));
		data.put("author_name", author.getString("name"));

		JSONObject counter = jsonObject.getJSONObject("counter");
		data.put("access_c_num", counter.getString("access_c_num"));
		data.put("checkin_num", counter.getString("checkin_num"));
		data.put("access_b_num", counter.getString("access_b_num"));
		data.put("access_a_num", counter.getString("access_a_num"));
		data.put("signup_num", counter.getString("signup_num"));

		data.put("cover", jsonObject.getString("cover"));
		data.put("detail", jsonObject.getString("detail"));
		data.put("end_at", jsonObject.getString("end_at"));
		data.put("credit", jsonObject.getString("credit"));
		data.put("limit", jsonObject.getString("limit"));
	}

	public ArrayList<String> parseActiveContent(String response)
			throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(response);
		ArrayList<String> dataArrayList = new ArrayList<String>();

		dataArrayList.add(jsonObject.getString("cover"));
		dataArrayList.add(jsonObject.getString("registImg"));
		dataArrayList.add(jsonObject.getString("title"));
		dataArrayList.add(jsonObject.getString("dtBegin"));
		dataArrayList.add(jsonObject.getString("dtEnd"));
		dataArrayList.add(jsonObject.getString("dtEnrollEnd"));
		dataArrayList.add(jsonObject.getString("unit"));
		dataArrayList.add(jsonObject.getString("class1"));
		dataArrayList.add(jsonObject.getJSONArray("rateList").length() + "");
		dataArrayList.add(jsonObject.getString("limit"));
		dataArrayList.add(jsonObject.getString("credit"));

		dataArrayList.add(jsonObject.getString("intro"));

		JSONArray rateList = jsonObject.getJSONArray("rateList");
		int rate1 = 0, rate2 = 0, rate3 = 0;
		for (int i = 0; i < rateList.length(); i++) {
			JSONObject rate = rateList.getJSONObject(i);

			if (rate.getString("score").equals("10")) {
				rate1++;
			} else if (rate.getString("score").equals("0")) {
				rate2++;
			} else if (rate.getString("score").equals("-10")) {
				rate3++;
			}
		}

		dataArrayList.add(rate1 + "");
		dataArrayList.add(rate2 + "");
		dataArrayList.add(rate3 + "");
		dataArrayList.add(rate3 + rate2 + rate1 + "");
		dataArrayList.add(jsonObject.getString("id"));

		return dataArrayList;

	}

	public void parseHuoDongContentList(String response,
			List<com.artgallery.data.HuoDongContentListData.Data> list_Data)
			throws Exception {
		// TODO Auto-generated method stub
		JSONObject object = new JSONObject(response);
		JSONArray jsonArray = object.getJSONArray("data");
		ArrayList<String> a = new ArrayList<String>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			list_Data.add(new com.artgallery.data.HuoDongContentListData.Data(
					jsonObject.getString("userName"), jsonObject
							.getString("dt"), jsonObject.getString("content"),
					object.getString("page"), jsonObject
							.getJSONArray("imageList")));

		}

	}

	public void parseMsgdata(String response, Context context) throws Exception {
		// TODO Auto-generated method stub
		JSONObject obj = new JSONObject(response);

		EmergencyDBHelper emergencyDBHelper = new EmergencyDBHelper(context);
		FullscreenDBHelper fullscreenDBHelper = new FullscreenDBHelper(context);
		PermanentDBHelper permanentDBHelper = new PermanentDBHelper(context);

		SharedPreferences sp = context.getSharedPreferences("SP",
				context.MODE_PRIVATE);
		String etaskid = "";
		String econtent = "";
		Editor editor = sp.edit();
		// Cursor cursor;
		// 2
		editor.putBoolean("fullChange", false);
		editor.commit();
		if (obj.getJSONObject("fullscreen").length() > 0) {

			String keepid = sp.getString("ftaskid", "null");
			String ftaskid = obj.getJSONObject("fullscreen")
					.getString("taskid");
			System.out.println("检查taskid是否重复:");
			System.out.println("keepid =" + keepid);
			System.out.println("ftaskid=" + ftaskid);

			if (keepid.equals(ftaskid)) {
				// 无变动时
				System.out.println("taskid没变");
				
			} else {
				// 变动时
				System.out.println("taskid改变");
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
				// Intent reset = new Intent();
				// reset.setAction("com.fy.CloseMSG");
				// context.sendBroadcast(reset);

				System.out.println("清空已有的全屏数据，再录入");
				fullscreenDBHelper.delete();
				JSONArray b = new JSONArray();
				b = obj.getJSONObject("fullscreen").getJSONArray("material");// 素材解析
				for (int j = 0; j < b.length(); j++) {
					fmaterialid = j;
					int model = 0;
					if (b.getJSONObject(j).getString("model").equals("moban1")) {
						model = 1;
					} else if (b.getJSONObject(j).getString("model")
							.equals("moban2")) {
						model = 2;
					} else if (b.getJSONObject(j).getString("model")
							.equals("moban3")) {
						model = 3;
					} else if (b.getJSONObject(j).getString("model")
							.equals("moban4")) {
						model = 4;
					} else if (b.getJSONObject(j).getString("model")
							.equals("moban5")) {
						model = 5;
					}

					fmodel = model + "";
					fduration = b.getJSONObject(j).getString("duration");// 时长
					JSONArray b1 = new JSONArray();
					b1 = b.getJSONObject(j).getJSONArray("content");
					for (int j1 = 0; j1 < b1.length(); j1++) {
						fcontentid = j1;
						ftype = b1.getJSONObject(j1).getString("type");
						fareaid = b1.getJSONObject(j1).getString("areaid");
						fcount = b1.getJSONObject(j1).getString("count");
						switch (model) {
						case 1:
							fsrc = b1.getJSONObject(j1).getString("src");
							fsrc = new String(fsrc.getBytes("UTF-8"), "UTF-8");
							fsrcid = 0;
							fullscreenDBHelper.insert(ftaskid, fmaterialid,
									fmodel, fduration, fcontentid, ftype,
									fcount, fareaid, fsrcid, fsrc, fdlpath,
									flink);
							break;
						case 2:
							if (!fareaid.equals("0")) {
								JSONArray ja = new JSONArray();
								ja = b1.getJSONObject(j1).getJSONArray("src");
								for (int j2 = 0; j2 < ja.length(); j2++) {
									fsrcid = j2;
									fsrc = ja.getString(j2);
									fsrc = new String(fsrc.getBytes("UTF-8"),
											"UTF-8");
									fullscreenDBHelper.insert(ftaskid,
											fmaterialid, fmodel, fduration,
											fcontentid, ftype, fcount, fareaid,
											fsrcid, fsrc, fdlpath, flink);
								}
							} else {
								// 视频资源
								fsrc = b1.getJSONObject(j1).getString("src");
								fsrc = new String(fsrc.getBytes("UTF-8"),
										"UTF-8");
								fullscreenDBHelper.insert(ftaskid, fmaterialid,
										fmodel, fduration, fcontentid, ftype,
										fcount, fareaid, fsrcid, fsrc, fdlpath,
										flink);
								// downloadfile(fsrc, editor, "aa.mp4");
							}

							break;
						case 3:
							fsrc = b1.getJSONObject(j1).getString("src");
							fsrc = new String(fsrc.getBytes("UTF-8"), "UTF-8");
							fsrcid = 0;
							fullscreenDBHelper.insert(ftaskid, fmaterialid,
									fmodel, fduration, fcontentid, ftype,
									fcount, fareaid, fsrcid, fsrc, fdlpath,
									flink);
							break;
						case 4:
							JSONArray jb = new JSONArray();
							jb = b1.getJSONObject(j1).getJSONArray("src");// 视频资源
							for (int j4 = 0; j4 < jb.length(); j4++) {
								fsrcid = j4;
								fsrc = jb.getJSONObject(j4).getString("image");
								fsrc = new String(fsrc.getBytes("UTF-8"),
										"UTF-8");
								flink = jb.getJSONObject(j4).getString("link");
								fullscreenDBHelper.insert(ftaskid, fmaterialid,
										fmodel, fduration, fcontentid, ftype,
										fcount, fareaid, fsrcid, fsrc, fdlpath,
										flink);
							}
							break;
						case 5:
							fsrc = b1.getJSONObject(j1).getString("src");
							fsrc = new String(fsrc.getBytes("UTF-8"), "UTF-8");
							fsrcid = 0;
							fullscreenDBHelper.insert(ftaskid, fmaterialid,
									fmodel, fduration, fcontentid, ftype,
									fcount, fareaid, fsrcid, fsrc, fdlpath,
									flink);
							break;
						}

					}
					// }
				}

				editor.putBoolean("NULL", false);// 全屏是否为空 不为空
				// cursor.close();
				System.out.println("全屏录入完成parse_json");
			}
		} else {
			System.out.println("全屏无数据parse_json");
			editor.putBoolean("fullChange", true);
			editor.putString("ftaskid", "null");
			fullscreenDBHelper.delete();// 清空全屏数据库表
			editor.putBoolean("NULL", true);// 全屏是否为空 为空

		}

		// 3

		// 数据录入
		String ptaskid = "";
		String psrc = "";
		String plink = "";
		if (obj.getJSONObject("permanent").length() > 0) {
			permanentDBHelper.delete();
			System.out.println("permanent clean");
			ptaskid = obj.getJSONObject("permanent").getString("taskid");// 首页任务id

			editor.putInt("imghas", 1);

			JSONArray a = new JSONArray();
			a = obj.getJSONObject("permanent").getJSONObject("data")
					.getJSONArray("index");
			for (int i = 0; i < a.length(); i++) {
				psrc = a.getJSONObject(i).getString("image");// 图片资源url
				psrc = new String(psrc.getBytes("UTF-8"), "UTF-8");
				plink = a.getJSONObject(i).getString("link");// 图片跳转url
				permanentDBHelper.insert(ptaskid, i, psrc, plink);
			}

			// }
		}

		// 紧急
		emergencyDBHelper.delete();
		if (obj.getJSONObject("emergency").length() > 0) {
			etaskid = obj.getJSONObject("emergency").getString("taskid");
			econtent = obj.getJSONObject("emergency").getString("content");
		}

		if (!etaskid.equals("")) {
			// 数据录入
			System.out.println("紧急任务数据录入完成");
			emergencyDBHelper.insert(etaskid, econtent);
			editor.putBoolean("jinji", true);// 是否有紧急任务
		} else {
			emergencyDBHelper.delete();// 清空紧急数据库表
			editor.putBoolean("jinji", false);// 是否有紧急任务
		}
		editor.commit();

	}

	public void parseZhiChangList(String response,
			List<com.artgallery.data.ZhiChangListData.Data> list_Data) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(response);
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject data = jsonArray.getJSONObject(i);
			list_Data.add(new com.artgallery.data.ZhiChangListData.Data(data
					.getString("position"), data.getString("company"), data
					.getString("cover"), data.getString("limit"), data
					.getString("dt"), data.getString("pay"), data
					.getString("id")));
		}
	}

	public void parseZhiChangDetail(String response,
			Map<String, String> detailsData, String user) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(response);
		String focusflag = "false";
		detailsData.put("id", jsonObject.getString("id"));
		detailsData.put("unit", jsonObject.getString("unit"));
		detailsData.put("userName", jsonObject.getString("userName"));
		detailsData.put("realName", jsonObject.getString("realName"));
		detailsData.put("position", jsonObject.getString("position"));
		detailsData.put("cover", jsonObject.getString("cover"));
		detailsData.put("type", jsonObject.getString("type"));
		detailsData.put("company", jsonObject.getString("company"));
		detailsData.put("pay", jsonObject.getString("pay"));
		detailsData.put("limit", jsonObject.getString("limit"));
		detailsData.put("intro", jsonObject.getString("intro"));
		detailsData.put("focusImg", jsonObject.getString("focusImg"));
		detailsData.put("dt", jsonObject.getString("dt"));
		JSONArray jsonArray = jsonObject.getJSONArray("focus");
		for (int i = 0; i < jsonArray.length(); i++) {
			if (user.equals(jsonArray.getString(i))) {
				focusflag = "true";
				break;
			}
		}
		detailsData.put("focusflag", focusflag);
	}

	public void parseDepartmentData(String response,
			List<com.artgallery.data.DepartmentData.Data> list_Data,
			List<com.artgallery.data.DepartmentTypeData.Data> list_Data2)
			throws Exception {

		JSONObject jsonObject = new JSONObject(response);
		JSONArray a = new JSONArray();
		a = jsonObject.getJSONArray("informations");
		for (int i = 0; i < a.length(); i++) {
			// JSONObject object = a.getJSONObject(i);
			list_Data.add(new com.artgallery.data.DepartmentData.Data(a
					.getJSONObject(i).getString("title"), a.getJSONObject(i)
					.getString("college"), a.getJSONObject(i).getString(
					"createTime"), a.getJSONObject(i).getString("id"), a
					.getJSONObject(i).getString("url"))

			);
		}

		JSONArray b = new JSONArray();
		b = jsonObject.getJSONArray("departments");
		for (int i = 0; i < b.length(); i++) {
			list_Data2.add(new com.artgallery.data.DepartmentTypeData.Data(b
					.getJSONObject(i).getString("departName"), b.getJSONObject(
					i).getString("id")));
		}

	}
	
	public void parseFloor(String response, List<com.artgallery.data.FloorData.Data> data) throws Exception {
		JSONObject jsonObject = new JSONObject(response);
		String http = jsonObject.getString("httpPhotoUrl");
		JSONArray jsonArray = jsonObject.getJSONArray("rows");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject j_data = jsonArray.getJSONObject(i);
			data.add(new com.artgallery.data.FloorData.Data(j_data.getString("cultureLouName"), http + j_data.getString("cultureLouPhoto")));
		}
	}

	public void parseKCB(String response, List<com.artgallery.data.KCBData.Data> Monday_am, List<com.artgallery.data.KCBData.Data> Monday_pm
			, List<com.artgallery.data.KCBData.Data> Tuesday_am, List<com.artgallery.data.KCBData.Data> Tuesday_pm
			, List<com.artgallery.data.KCBData.Data> Wednesday_am, List<com.artgallery.data.KCBData.Data> Wednesday_pm
			, List<com.artgallery.data.KCBData.Data> Thursday_am, List<com.artgallery.data.KCBData.Data> Thursday_pm
			, List<com.artgallery.data.KCBData.Data> Friday_am, List<com.artgallery.data.KCBData.Data> Friday_pm) throws Exception {
		JSONObject jsonObject = new JSONObject(response);
		JSONArray jsonArray = jsonObject.getJSONArray("rows");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			String dayOfTime = jsonObject2.getString("dayOfTime");
			String weekOfDay = jsonObject2.getString("weekOfDay");
			String classHour = jsonObject2.getString("classHour");
			String subject = jsonObject2.getString("subject");
			int classNum = jsonObject2.getInt("few");
			if (weekOfDay.equals("周一") && dayOfTime.equals("上午")) {
				Monday_am.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周一") && dayOfTime.equals("下午")) {
				Monday_pm.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周二") && dayOfTime.equals("上午")) {
				Tuesday_am.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周二") && dayOfTime.equals("下午")) {
				Tuesday_pm.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周三") && dayOfTime.equals("上午")) {
				Wednesday_am.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周三") && dayOfTime.equals("下午")) {
				Wednesday_pm.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周四") && dayOfTime.equals("上午")) {
				Thursday_am.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周四") && dayOfTime.equals("下午")) {
				Thursday_pm.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周五") && dayOfTime.equals("上午")) {
				Friday_am.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}else if (weekOfDay.equals("周五") && dayOfTime.equals("下午")) {
				Friday_pm.add(new com.artgallery.data.KCBData.Data(subject, classNum, dayOfTime, classHour));
			}
			
		}
	}
}
