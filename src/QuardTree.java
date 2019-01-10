import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * ����ʱ�䣺2018-12-10
 * �������ݣ�����Դ�������Ϊ�Ϻ�
 * �������ݣ���������洢��txt�ļ��У�����ϵΪGCJ02
 * @author wangqi
 *
 */
public class QuardTree {
	
	//ͨ�����ϽǺ����½�����ȷ�����η�Χ�����ж��������
	//�������귶ΧΪWGS-84����ϵ
	static double lux = 120.852722;//��X
	static double luy = 31.874695;//����Y
	static double rdx = 122.242493;//����X
	static double rdy = 30.677917;//����Y
	
	static String[] type = {"090100","090101",
			"090102","0902","0903","0904",
			"0905","0906","0907"};//type��keyword�����ѡһ�������������ѡ��type
	static String citycode = "021";//���б���
	static String path = "C:/����/data/poi/";
	
	static int offset = 20;//ÿҳ��ʾ��POI��Ŀ������С��25,Ĭ��
	
//	static final String KEY = "71da54f45899d4587c58c4e8eda0757d";//web API��Կ
//	static final String KEY = "9184bad671b4177e101c970d145e8fe3";//�׷۱����˺�
//	static final String KEY = "8d5902a76c2feb2b84a9f6a3c8992062";//ף���˺�
//	static final String KEY = "6b5489f95aef33c3a0fe0fa96ef0f867";//mask�˺�
	static final String KEY = "916662ebb2740cedbf157d5f4dd728a4";//mask2
	static final int NUM = 800;//���Է�Χ�������Ŀ���������800���������Ŀ���᷵��v
	
	
	public static void main(String[] args) {
		try {
			for(String i:type) {
				String n_path = path + citycode+"_"+i+".txt";
				File file  = new File(n_path);
				if(!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
//				FileWriter fw = new FileWriter(file, false);
				BufferedWriter bw = new BufferedWriter(osw);
				//root
				
				Node root = getGcjNode(lux, luy, rdx, rdy);
				create(root, i, bw);
				bw.flush();
				osw.close();
				bw.close();
			}
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * positon to gcj02
	 */
	public static Node getGcjNode(double lux, double luy, double rdx, double rdy) {
		
		Gps Gps_l = PositionUtil.gps84_To_Gcj02(lux, luy);
		Gps Gps_2 = PositionUtil
				.gps84_To_Gcj02(rdx, rdy);
		Node node =  new Node(Gps_l.getWgLon(), Gps_l.getWgLat(), Gps_2.getWgLon(), Gps_2.getWgLat());
		return node;
	}
	
	/**
	 * �����ռ������Ĳ���
	 * @param root ������ڵ㣬���ڵ㸲���о���Χ
	 */
	public static void create(Node root, String type,BufferedWriter bw) {
		String param = root._lux+","+root._luy+";"+root._rdx+","+root._rdy+";";
		int page = 1;
		String result = requestGet(type, param, page).toString();
		Gson gson = new Gson();
		Count c = gson.fromJson(result, Count.class);
		if(c.status == 0) {
			System.out.println("request failed");
		}else if (c.count<NUM) {
			//��Ҫ�ķ֣���ȡÿҳ��¼
			System.out.println(c.count);
			try {
				while (true) {
					String re = requestGet(type, param, page).toString();
					Info info = gson.fromJson(re, Info.class);
					if(info.count == 0) {
						break;
					}
					for(Poi poi: info.pois) {
						String da = gson.toJson(poi);
						bw.write(da+"\n");
	 				}
					page++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}else {
			//��Ҫ�ķ�
			System.out.println("quartering...");;
			double mid_x = (root._lux+root._rdx)/2;
			double mid_y = (root._luy+root._rdy)/2;
			Node nodeLU = new Node(root._lux, root._luy, mid_x, mid_y);
			Node nodeLD = new Node(root._lux, mid_y, mid_x, root._rdy);
			Node nodeRU = new Node(mid_x, root._luy, root._rdx, mid_y);
			Node nodeRD = new Node(mid_x, mid_y, root._rdx, root._rdy);
			Node[] nodes = new Node[4];
			nodes[0] = nodeLU;
			nodes[1] = nodeLD;
			nodes[2] = nodeRU;
			nodes[3] = nodeRD;
			for(Node n:nodes) {
				create(n, type, bw);
			}
		}
	}
	
	/**
	 * ���ݽڵ�ֵ��������
	 * count����NUM�������������ӽڵ����
	 * С��NUM����ҳ���н���
	 * @param type
	 * @param param
	 * @param page ��ʼҳ��Ϊ1
	 * @return
	 */
	public static StringBuilder requestGet(String type, String param, int page) {
		String url = "http://restapi.amap.com/v3/place/polygon?polygon=";
		url = url+param+"&output=json&extensions=all&types="+type+"&key="+KEY+"&page="+page;
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			URL realurl = new URL(url);
			URLConnection con = realurl.openConnection();
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("contentType", "UTF-8");	    
			con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            con.connect();
            in = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String line;
            while((line = in.readLine()) != null) {
            	result.append(line);
            }
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(in!= null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	
}
