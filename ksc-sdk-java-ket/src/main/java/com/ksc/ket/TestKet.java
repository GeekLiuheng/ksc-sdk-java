package com.ksc.ket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ksc.KscClientException;
import com.ksc.auth.AWSCredentials;
import com.ksc.auth.BasicAWSCredentials;
import com.ksc.ket.model.DelPresetRequest;
import com.ksc.ket.model.ErrResult;
import com.ksc.ket.model.GetPresetDetailRequest;
import com.ksc.ket.model.GetPresetDetailResult;
import com.ksc.ket.model.GetPresetListRequest;
import com.ksc.ket.model.GetPresetListResult;
import com.ksc.ket.model.GetQuotaUsedRequest;
import com.ksc.ket.model.GetQuotaUsedResult;
import com.ksc.ket.model.GetStreamTranListRequest;
import com.ksc.ket.model.GetStreamTranListResult;
import com.ksc.ket.model.PresetRequest;
import com.ksc.ket.model.StartStreamPullRequest;
import com.ksc.ket.model.StopStreamPullRequest;
import com.ksc.ket.model.UpdatePresetRequest;

public class TestKet {
	public static void main(String[] args) throws JSONException {
		AWSCredentials credentials = null;
		try {
			credentials = new BasicAWSCredentials("xxxxxxxxxxxxxxxx",
					"xxxxxxxxxxxxxxxxxxxxx");
		} catch (Exception e) {
			throw new KscClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		KSCKETJsonClient ksc = new KSCKETJsonClient(credentials);
		ksc.setEndpoint("http://ket.cn-beijing-6.api.ksyun.com/");
		
		//创建模板
		PresetRequest presetRequest = new PresetRequest();
		String data = PresetSet("preset", 2);
		presetRequest.setData(data);
		System.out.println("Create Preset Json:" + data);
		ErrResult presetResult = ksc.Preset(presetRequest);
		System.out.println("ErrNum: " + presetResult.getErrNum() + ",ErrMsg: " + presetResult.getErrMsg());
		
		//更新模板
		UpdatePresetRequest updatepresetRequest = new UpdatePresetRequest();
		String udata = PresetSet("preset", 3);
		updatepresetRequest.setData(udata);
		System.out.println("Update Preset Json:" + udata);
		ErrResult updatepresetResult = ksc.UpdatePreset(updatepresetRequest);
		System.out.println("ErrNum: " + updatepresetResult.getErrNum() + ",ErrMsg: " + updatepresetResult.getErrMsg());
		
		//删除模板
		DelPresetRequest delpresetRequest = new DelPresetRequest();
		delpresetRequest.setApp("live");
		delpresetRequest.setUniqName("mytest");
		delpresetRequest.setPreset("preset");
		ErrResult delpresetResult = ksc.DelPreset(delpresetRequest);
		System.out.println("ErrNum: " + delpresetResult.getErrNum() + ",ErrMsg: " + delpresetResult.getErrMsg());
		
		//获取模板列表
		GetPresetListRequest getPresetListRequest = new GetPresetListRequest();
		getPresetListRequest.setApp("live");
		getPresetListRequest.setUniqName("mytest");
		GetPresetListResult getpresetlistResult = ksc.GetPresetList(getPresetListRequest);
		
		//获取模板详情
		GetPresetDetailRequest getPresetDetailRequest = new GetPresetDetailRequest();
		getPresetDetailRequest.setApp("live");
		getPresetDetailRequest.setUniqName("mytest");
		getPresetDetailRequest.setPreset("preset");
		GetPresetDetailResult getPresetDetailResult = ksc.GetPresetDetail(getPresetDetailRequest);
		
		//获取任务列表
		GetStreamTranListRequest getStreamTranListRequest = new GetStreamTranListRequest();
		getStreamTranListRequest.setApp("live");
		getStreamTranListRequest.setUniqName("mytest");
		GetStreamTranListResult getStreamTranListResult = ksc.GetStreamTranList(getStreamTranListRequest);
		
		//发起外网拉流
		StartStreamPullRequest startStreamPullRequest = new StartStreamPullRequest();
		String data1 =  StreamPullSet("test123",0);
		startStreamPullRequest.setData(data1);
		ErrResult startStreamPullResult = ksc.StartStreamPull(startStreamPullRequest);
		System.out.println("ErrNum: " + startStreamPullResult.getErrNum() + ",ErrMsg: " + startStreamPullResult.getErrMsg());
		
		//停止外网拉流
		StopStreamPullRequest stopStreamPullRequest = new StopStreamPullRequest();
		String data2 =  StreamPullSet("test123",1);
		stopStreamPullRequest.setData(data2);
		ErrResult stopStreamPullResult = ksc.StopStreamPull(stopStreamPullRequest);
		System.out.println("ErrNum: " + stopStreamPullResult.getErrNum() + ",ErrMsg: " + stopStreamPullResult.getErrMsg());
		
		//获取配额使用数据
		GetQuotaUsedRequest getQuotaUsedRequest = new GetQuotaUsedRequest();
		getQuotaUsedRequest.setUniqName("mytest");
		GetQuotaUsedResult getQuotaUsedResult = ksc.GetQuotaUsed(getQuotaUsedRequest);
	}

	private static String StreamPullSet(String StreamID, int type) {
		JSONObject data = new JSONObject();
		data.put("UniqName", "mytest");
		data.put("StreamID", StreamID);
		if (type == 0) {
			data.put("SrcUrl", "test.uplive.ks-cdn.com");
		}
		data.put("App", "live");
		return data.toString();
	}

	private static String PresetSet(String preset, int num) throws JSONException {
		int formatSet[] = new int[] { 256, 257, 258, 259 };
		JSONObject data = new JSONObject();
		JSONArray output = new JSONArray();
		JSONArray logo = new JSONArray();
		JSONObject video = new JSONObject();
		data.put("UniqName", "mytest");
		data.put("Preset", preset);
		data.put("Description", "desc:" + preset);
		data.put("App", "live");
		for (int i = 0; i < num; i++) {
			JSONObject output_tmp = new JSONObject();
			JSONObject format = new JSONObject();
			JSONObject _switch = new JSONObject();
			format.put("output_format", formatSet[i]);
			format.put("vbr", 800000);
			_switch.put("sn", 0);
			_switch.put("sm", 0);
			_switch.put("hv", 0);
			output_tmp.put("format", format);
			output_tmp.put("switch", _switch);
			output.put(output_tmp);
		}
		data.put("Output", output);
		video.put("orientationAdapt", 1);
		for (int i = 0; i < num; i++) {
			JSONObject logo_tmp = new JSONObject();
			logo_tmp.put("pic", "/wangshuai9/1.png");
			logo_tmp.put("short_side", 640);
			logo.put(logo_tmp);
		}
		video.put("logo", logo);
		data.put("Video", video);
		return data.toString();
	}
}