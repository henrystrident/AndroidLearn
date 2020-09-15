package com.bjfu.fungus.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bjfu.fungus.Data.InformationBasic;
import com.bjfu.fungus.Data.InformationCap;
import com.bjfu.fungus.Data.InformationContext;
import com.bjfu.fungus.Data.InformationLamella;
import com.bjfu.fungus.Data.InformationRest;
import com.bjfu.fungus.Data.InformationStipe;
import com.bjfu.fungus.Data.InformationTube;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	private static String[] title = {
			"采集号", "采集人", "省", "市",
			"县/区", "详细地址", "采集日期", "经度",
			"纬度", "海拔", "中文名", "拉丁名", "宿主",
			"生境", "基物", "习性", "孢子印", "菌种分离",
			"DNA组织材料", "描述或说明", "菌物类别",

			"菌盖直径", "中央颜色", "边缘颜色", "形状",
			"表面特征", "附属结构", "附属结构颜色", "边缘",

			"菌褶宽度", "主体颜色","边缘颜色", "斑纹",
			"斑纹颜色", "着生", "乳汁", "乳汁颜色",
			"形态", "褶缘", "小菌褶", "菌褶密度",
			"菌缘间隙",

			"菌管长度", "菌管直径","菌管形状", "着生",
			"管颜色", "孔颜色", "孔缘",

			"菌柄长度", "上部粗","中部粗", "下部粗",
			"形状", "着生", "上部颜色", "中部颜色",
			"基部颜色", "基部", "假根", "假根长",
			"假根形状","表面特征","附属结构", "附属结构颜色",
			"内菌幕", "质地", "菌托",

			"菌肉厚", "近盖表颜色","中央颜色", "柄颜色",
			"气味", "味道",

			"伤变色", "盖表","菌管","菌柄",
			"菌肉", "孢子印", "盖表KOH(20%)显色反应", "菌褶KOH(20%)显色反应",
			"菌柄KOH(20%)显色反应", "菌肉KOH(20%)显色反应", "盖表NH4OH显色反应", "菌褶NH4OH显色反应",
			"菌柄NH4OH显色反应", "菌肉NH4OH显色反应"
	};


	/**
	 * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
	 */
	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

			arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(Colour.GRAY_25);

			arial12font = new WritableFont(WritableFont.ARIAL, 10);
			arial12format = new WritableCellFormat(arial12font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);//对齐格式
			arial12format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //设置边框

		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Excel
	 * @param fileName
	 * @param colName
     */
	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				Log.d("excel","创建了");
				file.createNewFile();
				workbook = Workbook.createWorkbook(file);
				WritableSheet sheet = workbook.createSheet("菌物采集", 0);
				//创建标题栏
				sheet.addCell((WritableCell) new Label(0, 0, fileName,arial14format));
				for (int col = 0; col < colName.length; col++) {
					sheet.addCell(new Label(col, 0, colName[col], arial10format));
				}
				sheet.setRowView(0,340); //设置行高

				workbook.write();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void writeObjListToExcel(List<T> objList, String fileName) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName),workbook);
				WritableSheet sheet = writebook.getSheet(0);

//				sheet.mergeCells(0,1,0,objList.size()); //合并单元格
//				sheet.mergeCells()

				for (int j = 0; j < objList.size(); j++) {
					ArrayList<String> list = (ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.addCell(new Label(i, j + 1, list.get(i),arial12format));
						if (list.get(i).length() <= 5){
							sheet.setColumnView(i,list.get(i).length()+8); //设置列宽
						}else {
							sheet.setColumnView(i,list.get(i).length()+5); //设置列宽
						}
					}
					sheet.setRowView(j+1,350); //设置行高
				}

				writebook.write();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public static String getPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		String dir = sdDir.toString();
		return dir;
	}

	public static void makeDir(File dir) {
		if (!dir.exists())
		{
			dir.mkdirs();
		}
	}

	public static String[] getTitle() {
		return title;
	}

	public static ArrayList<String> addToList(String collectNumber)
	{
		ArrayList<String> beanList = new ArrayList<>();
		InformationBasic basic = LitePal.where("collectNumber=?", collectNumber).find(InformationBasic.class).get(0);
		InformationCap cap = LitePal.where("collectNumber=?", collectNumber).find(InformationCap.class).get(0);
		InformationContext context = LitePal.where("collectNumber=?", collectNumber).find(InformationContext.class).get(0);
		InformationLamella lamella = LitePal.where("collectNumber=?", collectNumber).find(InformationLamella.class).get(0);
		InformationRest rest = LitePal.where("collectNumber=?", collectNumber).find(InformationRest.class).get(0);
		InformationStipe stipe = LitePal.where("collectNumber=?", collectNumber).find(InformationStipe.class).get(0);
		InformationTube tube = LitePal.where("collectNumber=?", collectNumber).find(InformationTube.class).get(0);

		//基本信息
		beanList.add(basic.getCollectNumber()+""); beanList.add(basic.getCollector()+"");
		beanList.add(basic.getProvince()+""); beanList.add(basic.getCity()+"");
		beanList.add(basic.getCountry()+""); beanList.add(basic.getAddress()+"");
		beanList.add(basic.getDate()+"");beanList.add(basic.getLatitude()+""); beanList.add(basic.getLongitude()+"");
		beanList.add(basic.getAltitude()+""); beanList.add(basic.getChineseName()+"");
		beanList.add(basic.getScientificName()+""); beanList.add(basic.getHost()+"");
		beanList.add(basic.getGrowEnvironment()+""); beanList.add(basic.getSubstrate()+"");
		beanList.add(basic.getHabit()+""); beanList.add(basic.getSpore()+"");
		beanList.add(basic.getTissue()+"");  beanList.add(basic.getDNA()+"");
		beanList.add(basic.getDescribe()+"");  beanList.add(basic.getCategory()+"");

		// 菌盖信息
		beanList.add(cap.getCap_Diameter()+""); beanList.add(cap.getCap_Color_center()+"");
		beanList.add(cap.getCap_Color_edge()+""); beanList.add(cap.getCap_Shape()+"");
		beanList.add(cap.getCap_Surface_feature()+""); beanList.add(cap.getCap_Accessory_structure()+"");
		beanList.add(cap.getCap_Accessory_structure_color()+""); beanList.add(cap.getCap_Margin()+"");

		// 菌褶信息
		beanList.add(lamella.getLamella_width()+""); beanList.add(lamella.getLamella_body_color()+"");
		beanList.add(lamella.getLamella_edge_color()+""); beanList.add(lamella.getLamella_stripe()+"");
		beanList.add(lamella.getLamella_stripe_color()+""); beanList.add(lamella.getLamella_insertion()+"");
		beanList.add(lamella.getLamella_milk()+""); beanList.add(lamella.getLamella_milk_color()+"");;
		beanList.add(lamella.getLamella_form()+""); beanList.add(lamella.getLamella_lamella_edge()+"");
		beanList.add(lamella.getLamella_little()+""); beanList.add(lamella.getLamella_density()+"");
		beanList.add(lamella.getLamella_edge_gap()+"");

		// 菌管信息
		beanList.add(tube.getTube_length()+""); beanList.add(tube.getTube_diameter()+"");
		beanList.add(tube.getTube_shape()+""); beanList.add(tube.getTube_insertion()+"");
		beanList.add(tube.getTube_color_tube()+""); beanList.add(tube.getTube_color_hole()+"");
		beanList.add(tube.getTube_hole_edge()+"");

		// 菌柄信息
		beanList.add(stipe.getStipe_longth()+""); beanList.add(stipe.getStipe_thickness_top()+"");
		beanList.add(stipe.getStipe_thickness_middle()+""); beanList.add(stipe.getStipe_thickness_bottom()+"");
		beanList.add(stipe.getStipe_shape()+""); beanList.add(stipe.getStipe_insertion()+"");
		beanList.add(stipe.getStipe_color_top()+""); beanList.add(stipe.getStipe_color_middle()+"");
		beanList.add(stipe.getStipe_color_basis()+""); beanList.add(stipe.getStipe_base()+"");
		beanList.add(stipe.getStipe_rhizoid()+"");beanList.add(stipe.getStipe_rhizoid_length()+"");
		beanList.add(stipe.getStipe_rhizoid_shape()+""); beanList.add(stipe.getStipe_surface()+"");
		beanList.add(stipe.getStipe_accessory_structure()+""); beanList.add(stipe.getStipe_accessory_structure_color()+"");
		beanList.add(stipe.getStipe_inner_veil()+""); beanList.add(stipe.getStipe_quality()+"");
		beanList.add(stipe.getStipe_volva()+"");

		// 菌肉信息
		beanList.add(context.getContext_Thickness()+""); beanList.add(context.getContext_Color_cap()+"");
		beanList.add(context.getContext_Color_center()+""); beanList.add(context.getContext_Color_stipe()+"");
		beanList.add(context.getContext_Smell()+""); beanList.add(context.getContext_Taste()+"");

		// 其他信息
		beanList.add(rest.getRest_injury_discoloration()+""); beanList.add(rest.getRest_cap_surface()+"");
		beanList.add(rest.getRest_tube()+""); beanList.add(rest.getRest_stipe()+"");
		beanList.add(rest.getRest_context()+""); beanList.add(rest.getRest_spore()+"");
		beanList.add(rest.getRest_KOH_cap_surface()+""); beanList.add(rest.getRest_KOH_lamella()+"");
		beanList.add(rest.getRest_KOH_stipe()+""); beanList.add(rest.getRest_KOH_context()+"");
		beanList.add(rest.getRest_NH4OH_cap_surface()+""); beanList.add(rest.getRest_NH4OH_lamella()+"");
		beanList.add(rest.getRest_NH4OH_stipe()+""); beanList.add(rest.getRest_NH4OH_context()+"");


		return beanList;
	}

	//----------------------------------读------------------------------------

//	public static List<BillObject> read2DB(File f, Context con) {
//		ArrayList<BillObject> billList = new ArrayList<BillObject>();
//		try {
//			Workbook course = null;
//			course = Workbook.getWorkbook(f);
//			Sheet sheet = course.getSheet(0);
//
//			Cell cell = null;
//			for (int i = 1; i < sheet.getRows(); i++) {
//				BillObject tc = new BillObject();
//				cell = sheet.getCell(1, i);
//				tc.setFood(cell.getContents());
//				cell = sheet.getCell(2, i);
//				tc.setClothes(cell.getContents());
//				cell = sheet.getCell(3, i);
//				tc.setHouse(cell.getContents());
//				cell = sheet.getCell(4, i);
//				tc.setVehicle(cell.getContents());
//				Log.d("gaolei", "Row"+i+"---------"+tc.getFood() + tc.getClothes()
//						+ tc.getHouse() + tc.getVehicle());
//				billList.add(tc);
//
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return billList;
//	}
//
//	public static Object getValueByRef(Class cls, String fieldName) {
//		Object value = null;
//		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
//				.substring(0, 1).toUpperCase());
//		String getMethodName = "get" + fieldName;
//		try {
//			Method method = cls.getMethod(getMethodName);
//			value = method.invoke(cls);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return value;
//	}
}
